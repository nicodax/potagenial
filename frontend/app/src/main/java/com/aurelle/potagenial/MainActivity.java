package com.aurelle.potagenial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView aide;
    private ImageView profil;
    private ImageView parametre;
    private ImageView services;
    private ImageView panier;
    private ImageView camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


// ajouter l'action qui dirige vers la page d'aide quand on appui l'image

        this.aide = (ImageView) findViewById(R.id.aide);
        aide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent help = new Intent(getApplicationContext(),Aide.class);
                startActivity(help);
                finish();
            }
        });

// ajouter l'action qui dirige vers la page du profilde l'utilisateur quand on appuis l'image

        this.profil = (ImageView)findViewById(R.id.profil);

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(getApplicationContext(),Profil.class);
                startActivity(profile);
                finish();
            }
        });

// ajouter l'action qui dirige vers la page des parametres de l'application quand on appuis l'image

        this.parametre = (ImageView)findViewById(R.id.parametre);

        parametre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(getApplicationContext(), Parametres.class);
                startActivity(settings);
                finish();
            }
        });

// ajouter l'action qui dirige vers la page du profilde l'utilisateur quand on appuis l'image

        this.services = (ImageView)findViewById(R.id.services);

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent services = new Intent(getApplicationContext(), Services.class);
                startActivity(services);
                finish();
            }
        });

// ajouter l'action qui dirige vers le panier quand on appuis l'image

        this.panier = (ImageView)findViewById(R.id.panier);

        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eshop = new Intent(getApplicationContext(), Panier.class);
                startActivity(eshop);
                finish();
            }
        });

// ajouter l'action qui dirige vers la camera quand on appuis l'image

        this.camera = (ImageView)findViewById(R.id.camera);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent livecamera = new Intent(getApplicationContext(), Camera.class);
                startActivity(livecamera);
                finish();
            }
        });
// mettre le bouton de retour de la camera vers le dashboard
        camera.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Camera.class)));

// mettre le bouton de retour  du profil vers le dashboard
        profil.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Profil.class)));

// mettre le bouton de retour de l'aide  vers le dashboard
        aide.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Aide.class)));

// mettre le bouton de retour des paramètres  vers le dashboard
        parametre.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Parametres.class)));

// mettre le bouton de retour du panier  vers le dashboard
        panier.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Panier.class)));

// mettre le bouton de retour des services  vers le dashboard
        services.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Services.class)));
    }
}