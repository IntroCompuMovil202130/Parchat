package com.example.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Chats extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView chats;
    chatsAdapter adapter;
    Servicios s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        crearChats();
    }

    public void crearChats(){
        s = new Servicios();
        chats = findViewById(R.id.chats);
        chats.setOnItemClickListener(this);

        //Obtener chats del usuario actual

        adapter = new chatsAdapter(this, R.layout.chat, s.usuarios);
        chats.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("id", s.usuarios.get(i).getId() );
        startActivity( intent );
    }

    public void goToMatchs(View v){
        startActivity(new Intent(v.getContext(), Match.class));
    }

    public void goToProfile(View v){
        startActivity(new Intent(v.getContext(), Perfil.class));
    }

    public void goToCreateEvent(View v){
        startActivity(new Intent(v.getContext(), CrearEvento.class));
    }
}