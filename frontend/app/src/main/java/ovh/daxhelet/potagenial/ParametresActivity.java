package ovh.daxhelet.potagenial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class ParametresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        Button camera = (Button) findViewById(R.id.btCamera);

        camera.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(getApplicationContext(),CameraActivity.class);
            startActivity(cameraIntent);
            finish();
        });
    }
}