package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Perfil extends AppCompatActivity {

    private ListView listaEventos;
    private ArrayList eventos;
    private ArrayAdapter adaptador;

    private ArrayList<Evento> allEventos;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        listaEventos = findViewById(R.id.listaEventos);
        allEventos = new ArrayList<Evento>();
        eventos = new ArrayList();
        adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1,eventos);
        listaEventos.setAdapter(adaptador);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onResume() {
        super.onResume();
        eventos.clear();
        adaptador.notifyDataSetChanged();

        cargarEventos();

        listaEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listaEventos.getItemAtPosition(i).toString();
                Evento eventoS = allEventos.get(i);
                Intent intent = new Intent(view.getContext(),opcionesEvento.class);
                intent.putExtra("eventoM", eventoS.toString());
                startActivity(intent);
            }
        });
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

    public void cargarEventos(){
        String uid = mAuth.getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference("Users/" + uid + "/eventos");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allEventos.clear();
                for(DataSnapshot singleShot: dataSnapshot.getChildren()){
                    Evento evento = singleShot.getValue(Evento.class);
                    evento.setId(singleShot.getKey());
                    eventos.add(evento.getNombreEvento() + " " + evento.getLugar() + " " + evento.getFecha());
                    adaptador.notifyDataSetChanged();
                    allEventos.add(evento);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("lista de eventos", "error en la consulta", databaseError.toException());
            }
        });
    }

}