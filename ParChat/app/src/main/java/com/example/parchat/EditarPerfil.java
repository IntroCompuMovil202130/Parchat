package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.parchat.Camara;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditarPerfil extends AppCompatActivity {

    String permisoGaleria = Manifest.permission.READ_EXTERNAL_STORAGE;
    int idGaleria = 4;
    private static final int IMAGEN_R = 0;

    RoundedImageView foto;
    EditText nombre, edad, ciudad, desc, fb, ig, tk, tw;
    Usuario usuario;

    DatabaseReference myRef;
    FirebaseAuth mAuth;

    Uri uriImage;
    String uriValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        inflarVariables();

        nombre.setText(getIntent().getStringExtra("nombre"));
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
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
            requestPermission(this,permisoGaleria,"",idGaleria);
        }else{
            galleryImage();
        }
    }

    private void requestPermission(Activity context, String permission, String justification, int id){
        if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
                Toast.makeText(context,justification,Toast.LENGTH_SHORT).show();
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        };
    }

    public void guardarPerfil(View v){
        StorageReference folder = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        final StorageReference file_name = folder.child("Imagen" + uriImage.getLastPathSegment());
        file_name.putFile(uriImage).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener( uri -> {
            uriValue = String.valueOf(uri);
            usuario = new Usuario(mAuth.getCurrentUser().getUid(), nombre.getText().toString(), uriValue, desc.getText().toString(), edad.getText().toString(),
                    ciudad.getText().toString(), fb.getText().toString(), ig.getText().toString(), tk.getText().toString(), tw.getText().toString());

            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(usuario);

            startActivity(new Intent(EditarPerfil.this, Perfil.class));
        }));
    }
}