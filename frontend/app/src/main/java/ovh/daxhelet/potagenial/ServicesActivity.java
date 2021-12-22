package ovh.daxhelet.potagenial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class ServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        String urlCGU = "https://drive.google.com/file/d/1ptIY3_jzH1k5lQZvWJrNrnWeGkRhWosb/view?usp=sharing";
        String urlMentionsLegales = "https://www.generer-mentions-legales.com/monfichier-fabk3d2vvlyvoji573crocdr4wgwmd.html";
        Button eCGU = findViewById(R.id.btCGU);
        Button eMentionsLegales = findViewById(R.id.btMentionsLegales);

        eCGU.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urlCGU));
            startActivity(i);
        });

        eMentionsLegales.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urlMentionsLegales));
            startActivity(i);
        });
    }
}