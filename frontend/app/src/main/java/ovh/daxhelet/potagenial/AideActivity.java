package ovh.daxhelet.potagenial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

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

        volleyGetEmail();

        imessage = findViewById(R.id.message);
        subject = findViewById(R.id.subject);
        bouton = findViewById(R.id.bouton);

        Log.d("connection", "connection ok !");
        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();

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

    public void volleyGetEmail(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String url = "http://daxhelet.ovh:3535/emails"  ;

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
        }, error -> Toast.makeText(AideActivity.this, "An unexpected error " +
                "occurred", Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonArrayRequest);
    }
}
