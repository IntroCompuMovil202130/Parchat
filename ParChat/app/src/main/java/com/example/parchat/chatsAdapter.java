package com.example.parchat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class chatsAdapter extends ArrayAdapter {

    Activity context;
    TextView nombreView;
    ArrayList<String> nombres;

    public chatsAdapter(@NonNull Activity context, int resource, ArrayList<String> nombres) {
        super(context, resource);
        this.context = context;
        this.nombres = nombres;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.chat, null);

        nombreView = item.findViewById(R.id.nombreChat);
        nombreView.setText(nombres.get(position));

        return item;
    }
}
