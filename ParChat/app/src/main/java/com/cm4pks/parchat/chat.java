package com.cm4pks.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class chat extends AppCompatActivity {
    private TextView nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle info = getIntent().getExtras();
        setContentView(R.layout.activity_chat);
        nombre = (TextView) findViewById(R.id.nameTitle);
        nombre.setText(info.getString("nombre"));
    }
}