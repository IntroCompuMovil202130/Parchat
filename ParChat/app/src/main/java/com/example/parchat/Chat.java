package com.example.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Chat extends AppCompatActivity {

    Usuario usuario;
    TextView nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Servicios s = new Servicios();
        usuario = s.buscarUsuario( getIntent().getIntExtra("id", 0) );

        nombre = findViewById(R.id.nombreChat);
        nombre.setText(usuario.getNombre());

    }
}