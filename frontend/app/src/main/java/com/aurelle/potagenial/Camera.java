package com.aurelle.potagenial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Camera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}