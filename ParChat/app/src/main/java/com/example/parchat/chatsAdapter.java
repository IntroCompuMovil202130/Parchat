package com.example.parchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ChatsAdapter extends ArrayAdapter<Usuario> {

    Context context;
    List<Usuario> usuarios;
    int resourceLayout;
    TextView nombreU;
    ImageView fotoU;
    Usuario u;

    public ChatsAdapter(@NonNull Context context, int resource, List<Usuario> usuarios) {
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
        this.u = usuarios.get(position);
        nombreU = view.findViewById(R.id.nombreChat);
        nombreU.setText(u.getNombre());

        fotoU = view.findViewById(R.id.fotochat);
        int fotousu = Integer.parseInt(u.getFoto());
        fotoU.setImageResource(fotousu);

        return view;
    }
}
