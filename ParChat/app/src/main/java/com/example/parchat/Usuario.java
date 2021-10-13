package com.example.parchat;

import java.util.ArrayList;

public class Usuario {
    private String id;
    private String nombre;
    private String foto;
    private ArrayList<String> chats;
    //private ArrayList<Evento> enventos; TODO Aqui va la lista de eventos del usuario

    public Usuario(String id, String nombre, String foto, ArrayList<String> chats) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.chats = chats;
    }

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void crearChat(String id){
        this.chats.add(id);
    }
}
