package com.example.parchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class chatsAdapter extends ArrayAdapter<Usuario> {

    Context context;
    List<Usuario> usuarios;
    int resourceLayout;

    TextView nombreU;
    RoundedImageView fotoU;

    public chatsAdapter(@NonNull Context context, int resource, List<Usuario> usuarios) {
        super(context, resource, usuarios);
        this.context = context;
        this.usuarios = usuarios;
        this.resourceLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if( view == null ){
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }
        nombreU = view.findViewById(R.id.nombreChat);
        nombreU.setText( usuarios.get(position).nombre );

        fotoU = view.findViewById(R.id.fotochat);
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storage.child("ProfileImages/" + usuarios.get(position).id + "/image.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri).into(fotoU))
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "No se pudo descargar la imagen de perfil", Toast.LENGTH_LONG).show();
                    System.out.println(e.toString());
                });
        return view;
    }

}
