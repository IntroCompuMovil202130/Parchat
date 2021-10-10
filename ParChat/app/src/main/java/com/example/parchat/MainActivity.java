package com.example.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registrarse(View v){
        startActivity(new Intent(v.getContext(),Registrarse.class));
    }

    public void recContrasena(View v){
        startActivity( new Intent(v.getContext(),RecuperarContrasena.class) );
    }

    public void iniciarSesion(View v){
        //TODO Firebase Authentication Log In

        startActivity(new Intent(v.getContext(),Perfil.class));
    }
}