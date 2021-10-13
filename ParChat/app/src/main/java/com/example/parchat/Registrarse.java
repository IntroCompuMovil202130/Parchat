package com.example.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
    }

    public void iniciarSesion(View v){
        onBackPressed();
    }

    public void registrarse(View v){
        //TODO Registrar Usuario Firebase Auth

        startActivity(new Intent(v.getContext(), EditarPerfil.class));
    }
}