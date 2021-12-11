package ovh.daxhelet.potagenial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AideActivity extends AppCompatActivity {
    EditText  imessage, subject;
    Button bouton;
    String username, password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aide);

        Log.d("test", "la page aide est ok!");

        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();

        volleyGetEmail(user);

        imessage = findViewById(R.id.message);
        subject = findViewById(R.id.subject);
        bouton = findViewById(R.id.bouton);

        Log.d("connection", "connection ok !");

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //initialiser les properties pour le compte gmail
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // initialiser la session

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });



                try {

                    // pour le style de message du mail

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(email));
                    message.setSubject(user.username + " " + subject.getText().toString().trim());
                    message.setText(imessage.getText().toString().trim());

                    new SendEmail().execute(message);


                } catch (MessagingException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    
// Pour envoyer l'email

    private class SendEmail  extends AsyncTask<Message, String, String> { //class pour effectuer des tâches sans manipuler des threads

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() { //avant que la tâche ne soit exécutée
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AideActivity.this, "Veuillez patienter", "Envoie de l'email", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) { //les paramètre de la classe sont passées ici pour fonctionner en fond
            try {
                Transport.send(messages[0]);
                return "Success !"; // plutot mettre une variable 
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error !";
            }

        }

        @Override
        protected void onPostExecute(String s) { //fin du calcul. C'est ici que sont transmis les résultats
            super.onPostExecute(s);

            progressDialog.dismiss();
            if (s.equals("Success !")){
                AlertDialog.Builder builder = new AlertDialog.Builder(AideActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font>Success</font>"));
                builder.setMessage("Email envoyé avec succès !");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        imessage.setText("");
                        subject.setText("");

                    }
                });

                builder.show();

            }else{
                Toast.makeText(getApplicationContext(), "une erreur s'est produite", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void volleyGetEmail(User user){
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String url = "https://daxhelet.ovh:3535/emails"  ;

        CustomJsonArrayRequest jsonArrayRequest = new CustomJsonArrayRequest(Request.Method.GET,
                url, null, response -> {
            try {
                if(response.getJSONObject(0).getString("email_client")
                        .equals("potagenial@gmail.com")) {
                    username = response.getJSONObject(0).getString("email_client");
                    password = response.getJSONObject(0).getString("password_client");
                    email = response.getJSONObject(0).getString("email_user");
                }
            } catch (JSONException e) {
                Log.d("testdebug", e.toString());
                Toast.makeText(AideActivity.this, "An unexpected error" +
                        " occured", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            int httpStatusResponse = error.networkResponse.statusCode;
            if (httpStatusResponse == 403){
                volleyRefreshToken();
                AlertDialog.Builder builder = new AlertDialog.Builder(AideActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font>Mise à jour de la session</font>"));
                builder.setMessage("Cliquer sur 'OK' pour relancer l'activité");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        recreate();
                    }
                });

                builder.show();
            }
            else {
                Toast.makeText(AideActivity.this, "An unexpected error " +
                        "occurred", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer " + user.access_token);
                return headers;
            }
        };;

        requestQueue.add(jsonArrayRequest);
    }

    public void volleyRefreshToken(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        String url = "https://daxhelet.ovh:3535/token";

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
                    Intent login = new Intent(AideActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userLocalStore.setUserLoggedIn(false);
                Intent login = new Intent(AideActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
