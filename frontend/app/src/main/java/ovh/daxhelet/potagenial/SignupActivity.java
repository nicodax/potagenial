package ovh.daxhelet.potagenial;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText eUsername;
    private EditText eEmail;
    private EditText ePassword;
    private EditText eConfirmPassword;
    private EditText eFirstname;
    private EditText eLastname;
    private TextView eConditions;
    private TextView ePolitique;
    private CheckBox eCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        eConditions = findViewById(R.id.tvConditions);
        ePolitique = findViewById(R.id.tvPolitique);
        eCheckBox = findViewById(R.id.cbAccept);


        eConditions.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacypolicies.com/live/64674c1b-519d-443f-9967-574825f9b3eb"));
            startActivity(i);

        });

        ePolitique.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacypolicies.com/live/035980b0-0c72-490c-b4b8-88f78f1cbcfb"));
            startActivity(i);

        });




        eSignup.setOnClickListener(v -> {
            String inputUsername = eUsername.getText().toString();
            String inputEmail = eEmail.getText().toString();
            String inputConfirmPassword = eConfirmPassword.getText().toString();
            String inputPassword = ePassword.getText().toString();
            String inputFirstname = eFirstname.getText().toString();
            String inputLastname = eLastname.getText().toString();

            if (!eCheckBox.isChecked()){
                Toast.makeText(SignupActivity.this, "You have to read and accept " +
                        "the app conditions", Toast.LENGTH_SHORT).show();
            }
            else if(inputUsername.isEmpty() || inputEmail.isEmpty() || inputPassword.isEmpty() ||
                    inputFirstname.isEmpty() || inputLastname.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter all the details " +
                        "correctly!", Toast.LENGTH_SHORT).show();
            } else if (!(inputPassword.equals(inputConfirmPassword))) {
                Toast.makeText(SignupActivity.this, "Passwords must match!",
                        Toast.LENGTH_SHORT).show();
            } else if (!validPassword(inputPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords must contain at least " +
                        "8 characters, with at least one capital letter, one number and one special " +
                        "character (!@#$%^&*)!", Toast.LENGTH_SHORT).show();
            } else if (!inputEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                Toast.makeText(SignupActivity.this, inputEmail + " is not a valid " +
                        "email format", Toast.LENGTH_SHORT).show();
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

    public static boolean validPassword(String password){
        if (password.length()>7){
            Pattern loCaseLetter = Pattern.compile("[a-z]");
            Pattern upCaseLetter = Pattern.compile("[A-Z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%^&*]");

            Matcher hasLoCaseLetter = loCaseLetter.matcher(password);
            Matcher hasUpCaseLetter = upCaseLetter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            Boolean loCase = hasLoCaseLetter.find();
            Boolean upCase = hasUpCaseLetter.find();
            Boolean isDigit = hasDigit.find();
            Boolean spChar = hasSpecial.find();

            return loCase && upCase && isDigit && spChar;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void volleySignup(String username, String password, String email, String firstname,
                              String lastname) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://daxhelet.ovh:3535/user";

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
                }, error -> {
                    Toast.makeText(SignupActivity.this,
                            "Cannot create user", Toast.LENGTH_SHORT).show();
                    Log.d("test", error.toString());
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void signup(JSONObject response, String username) throws JSONException {
        if(response.length() == 2) {
            UserLocalStore userLocalStore = new UserLocalStore(this);

            Toast.makeText(SignupActivity.this, "Sign up successful!",
                    Toast.LENGTH_SHORT).show();

            User user = new User(username, response.getString("accessToken"), response.getString("refreshToken"));
            userLocalStore.storeUserData(user);
            userLocalStore.setUserLoggedIn(true);

            Intent intent = new Intent(SignupActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
    }
}