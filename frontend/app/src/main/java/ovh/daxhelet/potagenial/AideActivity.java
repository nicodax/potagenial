package ovh.daxhelet.potagenial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
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
    EditText email, imessage, subject;
    Button bouton;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aide);

        Log.d("test", "la page aide est ok!");

        email = findViewById(R.id.email);
        imessage = findViewById(R.id.message);
        subject = findViewById(R.id.subject);
        bouton = findViewById(R.id.bouton);
        username = "potagenial@gmail.com";
        password = "pot4geni4l**";

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
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getText().toString().trim()));
                    message.setSubject(subject.getText().toString().trim());
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
                        email.setText("");
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
}
