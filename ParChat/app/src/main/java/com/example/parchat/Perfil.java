package com.example.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Perfil extends AppCompatActivity {

    private ListView listaEventos;
    private ArrayList eventos;
    private ArrayAdapter adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        listaEventos = findViewById(R.id.listaEventos);
        eventos = new ArrayList();
        adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1,eventos);
        listaEventos.setAdapter(adaptador);
        eventos.add("Evento1");
        adaptador.notifyDataSetChanged();
        eventos.add("Evento2");
        adaptador.notifyDataSetChanged();
    }

    public void goToMatchs(View v){
        startActivity(new Intent(v.getContext(), Match.class));
    }

    public void goToChats(View v){
        startActivity(new Intent(v.getContext(), Chats.class));
    }

    public void goToCreateEvent(View v){
        startActivity(new Intent(v.getContext(), CrearEvento.class));
    }

}