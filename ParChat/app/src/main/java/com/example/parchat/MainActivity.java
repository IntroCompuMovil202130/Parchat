package com.example.parchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciarSesion(View v){
        startActivity(new Intent(v.getContext(), Perfil.class));
    }

    public void recContrasena(View v){
        startActivity(new Intent(v.getContext(), RecuperarContrasena.class));
    }

    public void registrarse(View v){
        startActivity(new Intent(v.getContext(), Registrarse.class));
    }
}
