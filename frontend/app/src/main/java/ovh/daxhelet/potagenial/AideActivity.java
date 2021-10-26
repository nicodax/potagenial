package ovh.daxhelet.potagenial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.lang.String;

public class AideActivity extends AppCompatActivity {
    EditText _txtEmail, _txtMessage;
    Button _bntSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aide);

        Log.d("test", "la page aide est ok!");

        _txtEmail = findViewById(R.id.txtEmail);
        _txtMessage = findViewById(R.id.txtMessage);
        _bntSend = findViewById(R.id.btnSend);
        _bntSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = "potagenial@gmail.com";
                final String password = "pot4geni4l**";
                String messageToSend = _txtMessage.getText().toString();
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                Session session=Session.getInstance(props,
                        new javax.mail.Authenticator(){
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(_txtEmail.getText().toString()));
                    message.setSubject("Email envoyé ! ");
                    message.setText(messageToSend);
                    Transport.send(message);
                    Toast.makeText(getApplicationContext(),"Email envoyé avec succés !", Toast.LENGTH_LONG).show();

                }catch (MessagingException e){
                    throw new RuntimeException(e);
                }


            }
        });
        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}