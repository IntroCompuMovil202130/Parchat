package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
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

import com.bumptech.glide.Glide;
import com.example.parchat.databinding.ActivityPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Perfil extends AppCompatActivity {

    private ActivityPerfilBinding binding;

    private ListView listaEventos;
    private ArrayList<String> eventos;
    private ArrayAdapter<String> adaptador;

    private ListView listaEventosHoy;
    private ArrayList<String> eventosHoy;
    private ArrayAdapter<String> adaptadorHoy;
    private boolean menu = true;
    private boolean otroU = false;
    private boolean amigos = false;

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
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inflarVariables();
        eventos = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,eventos);
        allEventos = new ArrayList<>();
        listaEventos.setAdapter(adaptador);

        eventosHoy = new ArrayList<>();
        adaptadorHoy = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,eventosHoy);
        listaEventosHoy.setAdapter(adaptadorHoy);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        createListeners();
        usuario = (Usuario) getIntent().getSerializableExtra("user");
        try {
            cargarInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inflarVariables(){
        listaEventos = findViewById(R.id.listaEventos);
        listaEventosHoy = findViewById(R.id.listEventoHoy);
        nombre = findViewById(R.id.textView4);
        desc = findViewById(R.id.textView9);
        edad = findViewById(R.id.textView12);
        ciudad = findViewById(R.id.textView13);
        imagen = findViewById(R.id.imageView);
    }

    public void cargarInfo() throws IOException {
        if(usuario != null){ //Usuario perfil del match
            otroU = true;
            StorageReference storage = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storage.child("ProfileImages/" + usuario.id + "/image.jpg");
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(Perfil.this).load(uri).into(imagen))
                    .addOnFailureListener(e -> {
                        Toast.makeText(Perfil.this, "No se pudo descargar la imagen de perfil", Toast.LENGTH_LONG).show();
                        System.out.println(e.toString());
                    });
            nombre.setText(usuario.nombre);
            edad.setText(usuario.edad);
            desc.setText(usuario.desc);
            ciudad.setText(usuario.ciudad);
            verificarAmistad(mAuth.getCurrentUser().getUid(),usuario.id,usuario.nombre);
            cargarEventos(usuario.id);

        }else{ // Usuario actual logeado
            otroU = false;
            StorageReference storage = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storage.child("ProfileImages/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + "/image.jpg");
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(Perfil.this).load(uri).into(imagen))
                    .addOnFailureListener(e -> {
                        Toast.makeText(Perfil.this, "No se pudo descargar la imagen de perfil", Toast.LENGTH_LONG).show();
                        System.out.println(e.toString());
                    });
            myRef = db.getReference("Users/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            usuario = new Usuario();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    //System.out.println(usuario.toString());
                    assert usuario != null;
                    nombre.setText(usuario.nombre);
                    edad.setText(usuario.edad);
                    desc.setText(usuario.desc);
                    ciudad.setText(usuario.ciudad);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Perfil.this, "Error al intentar leer la base de datos", Toast.LENGTH_LONG).show();
                }
            });
            cargarEventos(mAuth.getCurrentUser().getUid());
        }
    }

    private void verificarAmistad(String uid, String otroId, String nombreOtro) {
        myRef = db.getReference("chats/" + uid);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot singleShot: snapshot.getChildren()){
                    if(singleShot.getKey().equals(otroId)){
                        Log.i("amigos", "son socios");
                        amigos = true;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventos.clear();
        adaptador.notifyDataSetChanged();

        listaEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listaEventos.getItemAtPosition(i).toString();
                Evento eventoS = allEventos.get(i);
                Bundle extras = new Bundle();

                Intent intent = new Intent(view.getContext(),opcionesEvento.class);
                extras.putSerializable("eventoM", eventoS);
                if(otroU){
                    extras.putBoolean("oUsuario",true);
                    extras.putString("oUsuId",usuario.id);
                }
                else{
                    extras.putBoolean("oUsuario",false);
                }
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        listaEventosHoy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle extras = new Bundle();
                Intent intent = new Intent(view.getContext(),DiaEvento.class);
                String[] eventoHoy = listaEventosHoy.getItemAtPosition(i).toString().split(" ");
                for (Evento evento: allEventos) {
                    if(eventoHoy[0].equals(evento.nombreEvento)){
                        if(evento.organizador){
                            Log.i("org", "onItemClick: " + "soy organizador");
                            extras.putBoolean("oUsuario",false);
                            extras.putSerializable("eventoH",evento);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                        else{
                            Log.i("org", "onItemClick: " + "me uni");
                            extras.putBoolean("oUsuario",true);
                            extras.putSerializable("eventoH",evento);
                            extras.putString("oUsuId",evento.idorganizador);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }

                    }
                }
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

    public void cargarEventos(String id){
        myRef = db.getReference("eventos/" + id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allEventos.clear();
               if(amigos){
                    for(DataSnapshot singleShot: snapshot.getChildren()){

                        String idEvento = singleShot.child("id").getValue(String.class);
                        String idOrg = singleShot.child("idorganizador").getValue(String.class);
                        String fechaEv = singleShot.child("fecha").getValue(String.class);
                        String nombre = singleShot.child("nombreEvento").getValue(String.class);
                        String lugar = singleShot.child("lugar").getValue(String.class);
                        double latitud = singleShot.child("latitud").getValue(Double.class);
                        double longitud = singleShot.child("longitud").getValue(Double.class);
                        boolean organizador = singleShot.child("organizador").getValue(Boolean.class);

                        Evento evento = new Evento(idEvento,nombre,lugar,fechaEv,latitud,longitud,organizador,idOrg);
                        eventos.add(evento.nombreEvento + " " + evento.lugar + " " + evento.fecha);
                        adaptador.notifyDataSetChanged();
                        allEventos.add(evento);
                    }
                }
               if(!otroU){
                   for(DataSnapshot singleShot: snapshot.getChildren()){

                       String idEvento = singleShot.child("id").getValue(String.class);
                       String idOrg = singleShot.child("idorganizador").getValue(String.class);
                       String fechaEv = singleShot.child("fecha").getValue(String.class);
                       String nombre = singleShot.child("nombreEvento").getValue(String.class);
                       String lugar = singleShot.child("lugar").getValue(String.class);
                       double latitud = singleShot.child("latitud").getValue(Double.class);
                       double longitud = singleShot.child("longitud").getValue(Double.class);
                       boolean organizador = singleShot.child("organizador").getValue(Boolean.class);

                       Evento evento = new Evento(idEvento,nombre,lugar,fechaEv,latitud,longitud,organizador,idOrg);

                       if(verificarFecha(evento)){
                           eventosHoy.add(evento.nombreEvento + " " + evento.lugar + " " + evento.fecha);
                           adaptadorHoy.notifyDataSetChanged();
                           allEventos.add(evento);
                       }
                       else {
                           eventos.add(evento.nombreEvento + " " + evento.lugar + " " + evento.fecha);
                           adaptador.notifyDataSetChanged();
                           allEventos.add(evento);
                       }
                   }

               }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean verificarFecha(Evento evento){
        boolean resultado = false;
        Date fechaAct = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaEventoDate = null;

        try {
            fechaEventoDate = date.parse(evento.fecha);
            if((fechaAct.getDay() == fechaEventoDate.getDay()) &
                    (fechaAct.getMonth() == fechaEventoDate.getMonth()) &
                    (fechaAct.getYear() == fechaEventoDate.getYear())){

                resultado = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void createListeners(){
        binding.menu.setOnClickListener(v -> {
            if(menu){
                binding.editProfileOption.setVisibility(View.VISIBLE);
                binding.signOutOption.setVisibility(View.VISIBLE);
                binding.matchsOption.setVisibility(View.VISIBLE);
                menu = false;
            }else{
                binding.editProfileOption.setVisibility(View.GONE);
                binding.signOutOption.setVisibility(View.GONE);
                binding.matchsOption.setVisibility(View.GONE);
                menu = true;
            }
        });

        binding.editProfileOption.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, EditarPerfil.class);
            intent.putExtra("perfil", true);
            startActivity(intent);
        });
        binding.signOutOption.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(Perfil.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        binding.matchsOption.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, ListaMatchs.class);
            startActivity(intent);
        });

        binding.imageButton.setOnClickListener(v -> {
            if(!usuario.id.equals(mAuth.getCurrentUser().getUid())){
                startActivity(new Intent(Perfil.this, Perfil.class));
            }
        });

        binding.botonFb.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(usuario.facebook)));
            }catch (ActivityNotFoundException e){
                Toast.makeText(Perfil.this, "No hay una cuenta", Toast.LENGTH_SHORT).show();
            }
        });

        binding.botonIg.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(usuario.instagram)));
            }catch (ActivityNotFoundException e){
                Toast.makeText(Perfil.this, "No hay una cuenta", Toast.LENGTH_SHORT).show();
            }
        });

        binding.botonTk.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(usuario.tiktok)));
            }catch (ActivityNotFoundException e){
                Toast.makeText(Perfil.this, "No hay una cuenta", Toast.LENGTH_SHORT).show();
            }
        });

        binding.botonTw.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(usuario.twitter)));
            }catch (ActivityNotFoundException e){
                Toast.makeText(Perfil.this, "No hay una cuenta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}