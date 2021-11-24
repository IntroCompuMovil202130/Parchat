package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parchat.databinding.ActivityMatchBinding;
import com.example.parchat.utilities.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class Match extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;

    private ActivityMatchBinding binding;

    private boolean menu = true;
    private String interes;
    private List<Usuario> usuarios;
    private List<String> solicitudes;
    private TreeSet<String> usuariosUsados;
    private Usuario u;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        usuarios = new ArrayList<>();
        solicitudes = new ArrayList<>();
        usuariosUsados = new TreeSet<>();
        createListeners();
        getToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.frameLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        usuarios.clear();
        usuariosUsados.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(auth.getCurrentUser()).getUid() + "/perfil");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String perfil = snapshot.getValue(String.class);
                assert perfil != null;
                if(perfil.equals("0")){
                    showToast("Debe completar su perfil");
                    Intent intent = new Intent(Match.this, EditarPerfil.class);
                    intent.putExtra("perfil", false);
                    startActivity(intent);
                }else{
                    init();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("No se pudo leer la base de datos");
            }
        });
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
            Intent intent = new Intent(Match.this, EditarPerfil.class);
            intent.putExtra("perfil", true);
            startActivity(intent);
        });
        binding.signOutOption.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(Match.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        binding.matchsOption.setOnClickListener(v -> {
            Intent intent = new Intent(Match.this, ListaMatchs.class);
            startActivity(intent);
        });

        binding.rechazarButton.setOnClickListener(v -> {
            HashMap<String, Object> usuario = new HashMap<>();
            usuario.put(u.id, u.nombre);
            loadProfile();
            dbRef = database.getReference("eliminados/" + Objects.requireNonNull(auth.getCurrentUser()).getUid());
            dbRef.updateChildren(usuario);
        });
        binding.perfilButton.setOnClickListener(v -> {
            Intent intent = new Intent(Match.this, Perfil.class);
            intent.putExtra("user", u);
            startActivity(intent);
        });

        binding.aceptarButton.setOnClickListener(v -> {
            boolean match = false;
            for(int i = 0; i < solicitudes.size(); i++){
                if( solicitudes.get(i).equals(u.id) ){
                    showToast("Felicitaciones han hecho MATCH con " + u.nombre);
                    match = true;
                    dbRef = database.getReference("chats/" + Objects.requireNonNull(auth.getCurrentUser()).getUid() + "/" + u.id);
                    dbRef.setValue(u.nombre);
                    dbRef = database.getReference("chats/" + u.id + "/" + auth.getCurrentUser().getUid());
                    dbRef.setValue(nombre);
                    dbRef = database.getReference("solicitudes/" + auth.getCurrentUser().getUid() + "/" + u.id);
                    dbRef.removeValue();
                }
            }
            if(!match) {
                dbRef = database.getReference("solicitudes/" + u.id);
                HashMap<String, Object> solicitud = new HashMap<>();
                solicitud.put(Objects.requireNonNull(auth.getCurrentUser()).getUid(), nombre);
                dbRef.updateChildren(solicitud);
            }
            HashMap<String, Object> usuario = new HashMap<>();
            usuario.put(u.id, u.nombre);
            loadProfile();
            dbRef = database.getReference("eliminados/" + Objects.requireNonNull(auth.getCurrentUser()).getUid());
            dbRef.updateChildren(usuario);
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void init(){
        dbRef = database.getReference("eliminados/" + Objects.requireNonNull(auth.getCurrentUser()).getUid());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    usuariosUsados.add(child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("No se pueden leer los eliminados");
            }
        });
        dbRef = database.getReference("Users/" + Objects.requireNonNull(auth.getCurrentUser()).getUid());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                interes = Objects.requireNonNull(snapshot.getValue(Usuario.class)).interes;
                nombre = Objects.requireNonNull(snapshot.getValue(Usuario.class)).nombre;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                interes = null;
                System.out.println(error);
            }
        });
        dbRef = database.getReference("Users");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    if(Objects.requireNonNull(child.getValue(Usuario.class)).genero.equals(interes)){
                        if( !usuariosUsados.contains( Objects.requireNonNull(child.getValue(Usuario.class)).id ) ) {
                            usuarios.add(child.getValue(Usuario.class));
                        }
                    }
                }
                loadProfile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("ERROR " + error.toString());
            }
        });

        dbRef = FirebaseDatabase.getInstance().getReference("solicitudes/" + Objects.requireNonNull(auth.getCurrentUser()).getUid());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    solicitudes.add(child.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("No se pueden leer las solicitudes");
            }
        });
    }

    private void loadProfile(){
        binding.noHayPerfilesText.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.frameLayout.setVisibility(View.GONE);
        if(usuarios.size() > 0){
            u = usuarios.get(0);
            usuarios.remove(0);

            StorageReference storage = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storage.child("ProfileImages/" + u.id + "/image.jpg");
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(Match.this).load(uri).into(binding.imagenMatch);
                        binding.nombreMatch.setText(u.nombre);
                        binding.lugarMatch.setText(u.ciudad);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.frameLayout.setVisibility(View.VISIBLE);
                    })
                    .addOnFailureListener(e -> {
                        showToast("Error al cargar la imagen");
                        System.out.println(e.toString());
                    });
        }else{
            binding.progressBar.setVisibility(View.GONE);
            binding.noHayPerfilesText.setVisibility(View.VISIBLE);
        }
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        String id = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        FirebaseFirestore databaseStorage  = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                databaseStorage.collection(Constants.KEY_COLLECTION_USER).document(
                        id
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
        dbRef = database.getReference("Users/" + id + "/token");
        dbRef.setValue(token);
    }

    public void goToChats(View v){
        startActivity(new Intent(v.getContext(), Chats.class));
    }

    public void goToProfile(View v){
        startActivity(new Intent(v.getContext(), Perfil.class));
    }

    public void goToCreateEvent(View v){
        startActivity(new Intent(v.getContext(), CrearEvento.class));
    }
}