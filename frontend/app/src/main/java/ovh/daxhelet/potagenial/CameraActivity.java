package ovh.daxhelet.potagenial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.HashMap;
import java.util.Map;


public class CameraActivity extends AppCompatActivity
{
    private String rtspUrl;

    private LibVLC libVlc;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        volleyGetCamera();

        setContentView(R.layout.activity_camera);

        libVlc = new LibVLC(this);
        mediaPlayer = new MediaPlayer(libVlc);
        videoLayout = findViewById(R.id.videoLayout);
    }

    @Override
    protected void onStart()
    {
        super.onStart();


    }

    @Override
    protected void onStop()
    {
        super.onStop();

        mediaPlayer.stop();
        mediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mediaPlayer.release();
        libVlc.release();
    }

    public void volleyGetCamera(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://daxhelet.ovh:3535/camera/1";

        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();

        CustomJsonArrayRequest jsonArrayRequest = new CustomJsonArrayRequest(Request.Method.GET,
                url, null, response -> {
            try {
                String camera_ip = response.getJSONObject(0).getString("camera_ip");
                String camera_username = response.getJSONObject(0).getString("camera_username");
                String camera_password = response.getJSONObject(0).getString("camera_password");
                rtspUrl = "rtsp://" + camera_username + ":" + camera_password + "@" +
                        camera_ip + "/live.sdp";

                mediaPlayer.attachViews(videoLayout, null, false, false);

                Media media = new Media(libVlc, Uri.parse(rtspUrl));
                media.setHWDecoderEnabled(true, false);
                media.addOption(":network-caching=600");

                mediaPlayer.setMedia(media);
                media.release();
                mediaPlayer.play();
            } catch (JSONException e) {
                userLocalStore.setUserLoggedIn(false);
                volleyRefreshToken();
            }
        }, error -> {
            userLocalStore.setUserLoggedIn(false);
            volleyRefreshToken();
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer " + user.access_token);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    public void volleyRefreshToken(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        String url = "https://daxhelet.ovh:3535/authorization/token";

        JSONObject params = new JSONObject();
        try {
            params.put("token", user.refresh_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userLocalStore.setAccessToken(response.getString("accessToken"));
                } catch (JSONException e) {
                    userLocalStore.setUserLoggedIn(false);
                    Intent login = new Intent(CameraActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userLocalStore.setUserLoggedIn(false);
                Intent login = new Intent(CameraActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}