package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class Perfil extends AppCompatActivity {

    private ListView listaEventos;
    private ArrayList<String> eventos;
    private ArrayAdapter<String> adaptador;

    Usuario usuario;
    TextView nombre, desc, edad, ciudad;
    RoundedImageView imagen;

    private ArrayList<Evento> allEventos;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        inflarVariables();
        eventos = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,eventos);
        listaEventos = findViewById(R.id.listaEventos);
        allEventos = new ArrayList<Evento>();
        eventos = new ArrayList();
        adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1,eventos);
        listaEventos.setAdapter(adaptador);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        cargarInfo();
    }

    public void inflarVariables(){
        listaEventos = findViewById(R.id.listaEventos);
        nombre = findViewById(R.id.textView4);
        desc = findViewById(R.id.textView9);
        edad = findViewById(R.id.textView12);
        ciudad = findViewById(R.id.textView13);
        imagen = findViewById(R.id.imageView);
    }

    public void cargarInfo(){
        myRef = db.getReference("Users/" + mAuth.getCurrentUser().getUid());
        usuario = new Usuario();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                System.out.println(usuario.toString());
                nombre.setText(usuario.nombre);
                edad.setText(usuario.edad);
                desc.setText(usuario.desc);
                ciudad.setText(usuario.ciudad);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Perfil.this, "Error al intentar leer la base de datos", Toast.LENGTH_LONG);
            }
        });
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