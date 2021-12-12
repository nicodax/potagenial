package ovh.daxhelet.potagenial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView eDashboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // id du mail




        eDashboard = findViewById(R.id.tvDashboardText);

        // ajouter l'action qui dirige vers la page d'aide quand on appui l'image

        ImageView aide = (ImageView) findViewById(R.id.ivAide);
        aide.setOnClickListener(view -> {
            Intent help = new Intent(getApplicationContext(),AideActivity.class);
            startActivity(help);
            finish();
        });

        // ajouter l'action qui dirige vers la page du profilde l'utilisateur quand on appuis l'image

        ImageView profil = (ImageView) findViewById(R.id.ivProfil);

        profil.setOnClickListener(view -> {
            Intent profile = new Intent(getApplicationContext(),ProfilActivity.class);
            startActivity(profile);
            finish();
        });

        // ajouter l'action qui dirige vers la page des parametres de l'application quand on appuis l'image

        ImageView parametre = (ImageView) findViewById(R.id.ivParametres);

        parametre.setOnClickListener(view -> {
            Intent settings = new Intent(getApplicationContext(), ParametresActivity.class);
            startActivity(settings);
            finish();
        });

        // ajouter l'action qui dirige vers la page du profilde l'utilisateur quand on appuis l'image

        ImageView services = (ImageView) findViewById(R.id.ivServices);

        services.setOnClickListener(view -> {
            Intent services1 = new Intent(getApplicationContext(), ServicesActivity.class);
            startActivity(services1);
            finish();
        });

        // ajouter l'action qui dirige vers le panier quand on appuis l'image

        ImageView panier = (ImageView) findViewById(R.id.ivPanier);

        panier.setOnClickListener(view -> {
            Intent eshop = new Intent(getApplicationContext(), PanierActivity.class);
            startActivity(eshop);
            finish();
        });

        // ajouter l'action qui dirige vers la camera quand on appuis l'image

        ImageView camera = (ImageView) findViewById(R.id.ivCamera);

        camera.setOnClickListener(view -> {
            Intent livecamera = new Intent(getApplicationContext(), CameraActivity.class);
            startActivity(livecamera);
            finish();
        });

        // TEMPORAIRE
        // ajouter l'action qui dirige vers le login quand on appuis l'image

        ImageView logo = (ImageView) findViewById(R.id.ivLogo);

        logo.setOnClickListener(view -> {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        });

        // mettre le bouton de retour de la camera vers le dashboard
        camera.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CameraActivity.class)));

        // mettre le bouton de retour  du profil vers le dashboard
        profil.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProfilActivity.class)));

        // mettre le bouton de retour de l'aide  vers le dashboard
        aide.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AideActivity.class)));

        // mettre le bouton de retour des paramètres  vers le dashboard
        parametre.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ParametresActivity.class)));

        // mettre le bouton de retour du panier  vers le dashboard
        panier.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PanierActivity.class)));

        // mettre le bouton de retour des services  vers le dashboard
        services.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ServicesActivity.class)));

        // TEMPORAIRE
        // mettre le bouton de retour de la camera vers le dashboard
        logo.setOnClickListener(v ->
                logOut());
    }


    @Override
    protected void onStart() {
        super.onStart();
        volleyAuthenticated();
    }

    public void volleyAuthenticated(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        String url = "https://daxhelet.ovh:3535/authorization/authenticated";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userLocalStore.setUserLoggedIn(response.getBoolean("authenticated"));
                } catch (JSONException e) {
                    userLocalStore.setUserLoggedIn(false);
                    volleyRefreshToken();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userLocalStore.setUserLoggedIn(false);
                volleyRefreshToken();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer " + user.access_token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
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
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userLocalStore.setUserLoggedIn(false);
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void logOut() {
        volleyLogout();
        UserLocalStore userLocalStore = new UserLocalStore(this);
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void volleyLogout(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        String url = "https://daxhelet.ovh:3535/user/logout";

        JSONObject params = new JSONObject();
        try {
            params.put("refreshToken", user.refresh_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        requestQueue.add(jsonObjectRequest);
    }
}