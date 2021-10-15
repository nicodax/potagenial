package ovh.daxhelet.potagenial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText eUsername;
    private EditText ePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eUsername = findViewById(R.id.etUsername);
        ePassword = findViewById(R.id.etPassword);
        Button eLogin = findViewById(R.id.btnLogin);

        eLogin.setOnClickListener(v -> {
            String inputUsername = eUsername.getText().toString();
            String inputPassword = ePassword.getText().toString();

            if(inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all the details " +
                        "correctly!", Toast.LENGTH_SHORT).show();
            } else {
                volleyLogin(inputUsername, inputPassword);
            }
        });
    }

    public void volleyLogin(String username, String password){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://daxhelet.ovh:3535/user/login";

        UserLocalStore userLocalStore = new UserLocalStore(this);

        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonArrayRequest jsonArrayRequest = new CustomJsonArrayRequest(Request.Method.POST,
                url, params, response -> {
            try {
                if(response.getJSONObject(0).getString("user_username")
                        .equals(username)) {
                    Toast.makeText(LoginActivity.this, "Login successful!",
                            Toast.LENGTH_SHORT).show();

                    User user = new User(username, password, response.getJSONObject(0)
                            .getString("user_email"));
                    userLocalStore.storeUserData(user);
                    userLocalStore.setUserLoggedIn(true);

                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "Incorrect credentials " +
                        "entered!", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(LoginActivity.this, "An unexpected error occurred",
                Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonArrayRequest);
    }
}