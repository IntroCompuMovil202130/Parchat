package com.cm4pks.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
    }

    public void iniciarSesion(View v){
        Intent intent = new Intent(v.getContext(),iniciarsesion.class);
        startActivity(intent);
    }

    public void main(View v){
        Intent intent = new Intent(v.getContext(),MainActivity.class);
        startActivity(intent);
    }
}