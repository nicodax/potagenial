package com.aurelle.potagenial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Panier extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}