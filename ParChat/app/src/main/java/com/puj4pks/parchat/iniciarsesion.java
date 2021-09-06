package com.puj4pks.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class iniciarsesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciarsesion2);
    }

    public void registrarse(View v){
        Intent intent = new Intent(v.getContext(),registrarse.class);
        startActivity(intent);
    }

    public void recContrasena(View v){
        Intent intent = new Intent(v.getContext(),recuperarClave.class);
        startActivity(intent);
    }
}