package com.cm4pks.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class iniciarsesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciarsesion);
    }

    public void registrarse(View v){
        Intent intent = new Intent(v.getContext(),registrarse.class);
        startActivity(intent);
    }

    public void recContrasena(View v){
        Intent intent = new Intent(v.getContext(),recuperarClave.class);
        startActivity(intent);
    }

    public void main(View v){
        Intent intent = new Intent(v.getContext(),MainActivity.class);
        startActivity(intent);
    }
}