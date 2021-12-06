package ovh.daxhelet.potagenial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParametresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        volleyGetSettings();

        Button camera = (Button) findViewById(R.id.btCamera);

        camera.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(getApplicationContext(),CameraActivity.class);
            startActivity(cameraIntent);
            finish();
        });
    }

    public void volleyGetSettings(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        String url = "http://daxhelet.ovh:3535/user/settings/" + user.username;

        CustomJsonArrayRequest jsonArrayRequest = new CustomJsonArrayRequest(Request.Method.GET,
                url, null, response -> {
            try {
                if(response.getJSONObject(0).getString("user_username")
                        .equals(user.username)) {
                    TextView tAmb = (TextView) findViewById(R.id.tvValeurTAmb);
                    TextView tSol = (TextView) findViewById(R.id.tvValeurTSol);
                    TextView hSol = (TextView) findViewById(R.id.tvValeurHSol);
                    TextView dernierArrosage = (TextView) findViewById(R.id.tvDernierArrosageDate);
                    TextView quantiteAdministree = (TextView)
                            findViewById(R.id.tvValeurEauAdministree);
                    CheckBox arrosageAuto = (CheckBox) findViewById(R.id.cbArrosageAutomatique);
                    TextView frequenceArrosageAuto = (TextView) findViewById(R.id.etNombreHeures);
                    TextView cameraId = (TextView) findViewById(R.id.tvValeurCameraId);
                    TextView sondeId = (TextView) findViewById(R.id.tvValeurSondeId);

                    tAmb.setText(response.getJSONObject(0)
                            .getString("settings_temperature_outside") + " " +
                            tAmb.getText());
                    tSol.setText(response.getJSONObject(0)
                            .getString("settings_temperature_ground") + " " + tSol.getText());
                    hSol.setText(response.getJSONObject(0)
                            .getString("settings_humidity") + " " + hSol.getText());
                    dernierArrosage.setText(response.getJSONObject(0)
                            .getString("settings_last_sprinkling"));
                    quantiteAdministree.setText(response.getJSONObject(0)
                            .getString("settings_last_sprinkling_quantity") + " " +
                            quantiteAdministree.getText());
                    if (response.getJSONObject(0)
                            .getString("settings_automatic_sprinkling").equals("1")) {
                        arrosageAuto.setChecked(true);
                    }
                    frequenceArrosageAuto.setText(response.getJSONObject(0)
                            .getString("settings_automatic_sprinkling_frequency"));
                    cameraId.setText(response.getJSONObject(0).getString("camera_id"));
                    sondeId.setText(response.getJSONObject(0).getString("sonde_id"));
                }
            } catch (JSONException e) {
                Toast.makeText(ParametresActivity.this, "Incorrect credentials " +
                        "entered!", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(ParametresActivity.this, "An unexpected error occurred",
                Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonArrayRequest);
    }
}