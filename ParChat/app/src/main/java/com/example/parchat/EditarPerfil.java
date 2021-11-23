package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parchat.databinding.ActivityEditarPerfilBinding;
import com.example.parchat.utilities.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class EditarPerfil extends AppCompatActivity {

    String permisoGaleria = Manifest.permission.READ_EXTERNAL_STORAGE;
    int idGaleria = 4;
    private static final int IMAGEN_R = 0;
    private boolean menu = true;
    private boolean imageEdited = false;
    private boolean perfil;

    private ActivityEditarPerfilBinding binding;

    RoundedImageView foto;
    EditText nombre, edad, ciudad, desc, fb, ig, tk, tw;
    Usuario usuario;
    Spinner generos, interes;

    DatabaseReference myRef;
    FirebaseDatabase db;
    FirebaseAuth mAuth;

    Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inflarVariables();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        perfil = getIntent().getBooleanExtra("perfil", false);
        if(perfil){
            cargarPerfil();
        }
        createListeners();
    }

    public void inflarVariables(){
        foto = findViewById(R.id.imagenPerfil);
        nombre = findViewById(R.id.nombreEditText);
        edad = findViewById(R.id.edadEditText);
        ciudad = findViewById(R.id.ciudadEditText);
        desc = findViewById(R.id.descEditText);
        fb = findViewById(R.id.fbEditText);
        ig = findViewById(R.id.igEditText);
        tk = findViewById(R.id.tkEditText);
        tw = findViewById(R.id.twEditText);
        generos = findViewById(R.id.spinnerGeneros);
        interes = findViewById(R.id.spinnerIntereses);
    }

    public void createListeners(){
        binding.menu.setOnClickListener(v -> {
            if(menu){
                binding.editProfileOption.setVisibility(View.VISIBLE);
                binding.signOutOption.setVisibility(View.VISIBLE);
                menu = false;
            }else{
                binding.editProfileOption.setVisibility(View.GONE);
                binding.signOutOption.setVisibility(View.GONE);
                menu = true;
            }
        });

        binding.signOutOption.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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

    public void subirImagen(View v){
        if(ActivityCompat.checkSelfPermission(this,permisoGaleria)!= PackageManager.PERMISSION_GRANTED){
            requestPermission(this,permisoGaleria, idGaleria);
        }else{
            galleryImage();
        }
    }

    private void requestPermission(Activity context, String permission, int id){
        if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
                Toast.makeText(context, "No podra subir imagenes",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission},id);
        }
    }

    private void galleryImage(){
        if(ActivityCompat.checkSelfPermission(this,permisoGaleria)== PackageManager.PERMISSION_GRANTED){
            Intent pickImg = new Intent(Intent.ACTION_PICK);
            pickImg.setType("image/*");
            startActivityForResult(pickImg,IMAGEN_R);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == idGaleria){
            galleryImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            try {
                final Uri imageUri= data.getData();
                this.uriImage = imageUri;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                foto.setImageBitmap(selectedImage);
                imageEdited = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void guardarPerfil(View v){
        myRef = db.getReference();
        binding.botonGuardar.setVisibility(View.GONE);
        if(imageEdited){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageReference.child("ProfileImages/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + "/image.jpg");
            imageRef.putFile(uriImage)
                    .addOnSuccessListener(taskSnapshot -> {
                        usuario = new Usuario(mAuth.getCurrentUser().getUid(), nombre.getText().toString(),  desc.getText().toString(), edad.getText().toString(),
                                ciudad.getText().toString(), fb.getText().toString(), ig.getText().toString(), tk.getText().toString(), tw.getText().toString());
                        usuario.genero = generos.getSelectedItem().toString();
                        usuario.interes = interes.getSelectedItem().toString();

                        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(usuario);
                        Toast.makeText(EditarPerfil.this, "Datos guardados", Toast.LENGTH_SHORT).show();
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        HashMap<String, Object> user = new HashMap<>();
                        user.put(Constants.KEY_NAME, nombre.getText().toString());
                        database.collection(Constants.KEY_COLLECTION_USER).document(mAuth.getCurrentUser().getUid())
                                .set(user)
                                .addOnSuccessListener(documentReference -> showToast("Usuario guardado correctamente"))
                                .addOnFailureListener(exception -> showToast(exception.getMessage()));
                        startActivity(new Intent(EditarPerfil.this, Perfil.class));
                    })
                    .addOnFailureListener(e -> {
                        binding.botonGuardar.setVisibility(View.VISIBLE);
                        Toast.makeText(EditarPerfil.this, "No se pudo guardar la imagen", Toast.LENGTH_SHORT).show();
                    });
        }else{
            usuario = new Usuario(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), nombre.getText().toString(),  desc.getText().toString(), edad.getText().toString(),
                    ciudad.getText().toString(), fb.getText().toString(), ig.getText().toString(), tk.getText().toString(), tw.getText().toString());
            usuario.genero = generos.getSelectedItem().toString();
            usuario.interes = interes.getSelectedItem().toString();

            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(usuario);
            Toast.makeText(EditarPerfil.this, "Datos guardados", Toast.LENGTH_SHORT).show();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_NAME, nombre.getText().toString());
            database.collection(Constants.KEY_COLLECTION_USER).document(mAuth.getCurrentUser().getUid())
                    .set(user)
                    .addOnSuccessListener(documentReference -> showToast("Usuario registrado correctamente"))
                    .addOnFailureListener(exception -> showToast(exception.getMessage()));
            startActivity(new Intent(EditarPerfil.this, Perfil.class));
        }

    }

    private void showToast(String message){
        Toast.makeText(EditarPerfil.this, message, Toast.LENGTH_LONG).show();
    }

    private void cargarPerfil(){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storage.child("ProfileImages/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + "/image.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(EditarPerfil.this).load(uri).into(foto))
                .addOnFailureListener(e -> {
                    Toast.makeText(EditarPerfil.this, "No se pudo descargar la imagen de perfil", Toast.LENGTH_LONG).show();
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

                fb.setText(usuario.facebook);
                ig.setText(usuario.instagram);
                tk.setText(usuario.tiktok);
                tw.setText(usuario.twitter);
                if(usuario.genero.equals("Hombre")){
                    generos.setSelection(1);
                }else{
                    generos.setSelection(2);
                }if(usuario.interes.equals("Hombre")){
                    interes.setSelection(1);
                }else{
                    interes.setSelection(2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditarPerfil.this, "Error al intentar leer la base de datos", Toast.LENGTH_LONG).show();
            }
        });
    }
}