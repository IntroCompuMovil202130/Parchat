package com.cm4pks.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class amigos extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private List<String> names = new ArrayList<>();
    private ListView nombresAmigos;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        nombresAmigos =(ListView)findViewById(R.id.nombresAmigos);
        String nombre = "Edwin Vesga";
        names.add(nombre);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
        nombresAmigos.setAdapter(adapter);
        nombresAmigos.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println(names.get(i));
        Intent intent = new Intent();
        intent.setClass(this, chat.class);
        intent.putExtra("nombre",names.get(i));
        startActivity(intent);
    }
}