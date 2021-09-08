package com.cm4pks.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void buscarAmigos(View v){
        Intent intent = new Intent(v.getContext(),buscarAmigos.class);
        startActivity(intent);
    }

    public void perfil(View v){
        Intent intent = new Intent(v.getContext(),perfil.class);
        startActivity(intent);
    }

    public void chat(View v){
        Intent intent = new Intent(v.getContext(),chat.class);
        startActivity(intent);
    }
}