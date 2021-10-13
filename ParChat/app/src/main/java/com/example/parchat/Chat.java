package com.example.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    Usuario usuario;
    TextView nombre;
    private List<MensajeChat> mensajesChats;
    private MensajeAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Servicios s = new Servicios();
        usuario = s.buscarUsuario( getIntent().getStringExtra("id"));

        nombre = findViewById(R.id.nombreChat);
        nombre.setText(usuario.getNombre());

    }

    private void init(){
        //preferenceManager = new PreferenceManager(getApplicationContext());
        mensajesChats = new ArrayList<>();
        chatAdapter = new MensajeAdapter(
                mensajesChats, usuario.getId()
        );
    }
}