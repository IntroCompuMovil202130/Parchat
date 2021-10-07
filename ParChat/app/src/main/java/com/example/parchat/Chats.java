package com.example.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Chats extends AppCompatActivity {

    ListView chats;
    ArrayList<String> usuarios_chats;
    chatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usuarios_chats = new ArrayList<>();

        usuarios_chats.add("Mia Khalifa");

        chats = findViewById(R.id.chats);
        adapter = new chatsAdapter(this, R.layout.chat, usuarios_chats);
        chats.setAdapter(adapter);
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