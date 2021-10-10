package com.example.parchat;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nombre;
    private int foto;
    private ArrayList<Integer> chats;

    public Usuario(int id, String nombre, int foto, ArrayList<Integer> chats) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.chats = chats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public void crearChat(int id){
        this.chats.add(id);
    }
}
