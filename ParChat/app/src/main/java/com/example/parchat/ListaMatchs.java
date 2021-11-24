package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.parchat.databinding.ActivityListaMatchsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaMatchs extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ActivityListaMatchsBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private String currentUserid;
    private boolean menu = true;

    private chatsAdapter adapter;
    private List< Usuario > usuarios;
    private List<Usuario> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaMatchsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usuarios = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        allUsers = new ArrayList<>();

        init();
        createListeners();
    }

    private void init(){
        DatabaseReference dbRef = database.getReference("chats/" + currentUserid);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot child: snapshot.getChildren() ){
                    usuarios.add( Usuario.makeUsuario(child.getKey(), child.getValue(String.class)) );
                }
                crearChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("No se pueden leer los matchs");
            }
        });
        dbRef = database.getReference("Users");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot child: snapshot.getChildren() ){
                    allUsers.add( child.getValue(Usuario.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("No se pueden leer los matchs");
            }
        });
    }

    public void crearChats(){
        binding.lista.setOnItemClickListener(this);
        adapter = new chatsAdapter(this, R.layout.chat, usuarios);
        binding.lista.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ListaMatchs.this, Perfil.class);
        for(Usuario u: allUsers){
            if(u.id.equals(usuarios.get(position).id)){
                intent.putExtra("user", u);
                showToast(u.toString());
            }
        };
        startActivity(intent);
    }

    public void goToChats(View v){
        startActivity(new Intent(v.getContext(), Chats.class));
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
            Intent intent = new Intent(ListaMatchs.this, EditarPerfil.class);
            intent.putExtra("perfil", true);
            startActivity(intent);
        });
        binding.signOutOption.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(ListaMatchs.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}