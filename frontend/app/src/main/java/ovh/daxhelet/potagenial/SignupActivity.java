package ovh.daxhelet.potagenial;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText eUsername;
    private EditText eEmail;
    private EditText ePassword;
    private EditText eConfirmPassword;
    private EditText eFirstname;
    private EditText eLastname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("create", "created!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        eUsername = findViewById(R.id.etSignupUsername);
        eEmail  = findViewById(R.id.etSignupEmail);
        ePassword = findViewById(R.id.etSignupPassword);
        eConfirmPassword = findViewById(R.id.etSignupConfirmPassword);
        eFirstname = findViewById(R.id.etFirstname);
        eLastname = findViewById(R.id.etLastname);
        Button eSignup = findViewById(R.id.btnSignup);
        Button eToLogin = findViewById(R.id.btnToLogin);

        eSignup.setOnClickListener(v -> {
            String inputUsername = eUsername.getText().toString();
            String inputEmail = eEmail.getText().toString();
            String inputConfirmPassword = eConfirmPassword.getText().toString();
            String inputPassword = ePassword.getText().toString();
            String inputFirstname = eFirstname.getText().toString();
            String inputLastname = eLastname.getText().toString();

            if(inputUsername.isEmpty() || inputEmail.isEmpty() || inputPassword.isEmpty() ||
                    inputFirstname.isEmpty() || inputLastname.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter all the details " +
                        "correctly!", Toast.LENGTH_SHORT).show();
            } else if (!(inputPassword.equals(inputConfirmPassword))) {
                Toast.makeText(SignupActivity.this, "Passwords must match!",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                volleySignup(inputUsername, inputPassword, inputEmail, inputFirstname,
                        inputLastname);
            }
        });

        eToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void volleySignup(String username, String password, String email, String firstname,
                              String lastname) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://daxhelet.ovh:3535/user";

        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("password", password);
            params.put("firstname", firstname);
            params.put("lastname", lastname);
            params.put("email", email);
            params.put("birthdate", "2021-09-01");
            params.put("sexe", "X");
            params.put("country", "tmp");
            params.put("city", "tmp");
            params.put("address", "tmp");
            params.put("house_number", 88);
            params.put("zipcode", 8888);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, params, response -> {
                    try {
                        signup(response, username);
                    } catch (JSONException e) {
                        Toast.makeText(SignupActivity.this,
                                "Cannot create user", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(SignupActivity.this,
                                "Cannot create user", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void signup(JSONObject response, String username) throws JSONException {
        if(response.length() == 1) {
            UserLocalStore userLocalStore = new UserLocalStore(this);

            Toast.makeText(SignupActivity.this, "Sign up successful!",
                    Toast.LENGTH_SHORT).show();

            User user = new User(username, response.getString("accessToken"));
            userLocalStore.storeUserData(user);
            userLocalStore.setUserLoggedIn(true);

            Intent intent = new Intent(SignupActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
    }
}