package com.example.parchat;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String id;
    private String nombre;
    private String foto;
    private String desc;
    private String edad;
    private String ciudad;
    private String facebook;
    private String instagram;
    private String tiktok;
    private String twitter;
    private List<String> chats;
    private List<Evento> eventos;

    public Usuario(){}

    public Usuario(String id, String nombre, String foto, String desc, String edad, String ciudad, String facebook, String instagram, String tiktok, String twitter, ArrayList<String> chats, ArrayList<Evento> eventos) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.desc = desc;
        this.edad = edad;
        this.ciudad = ciudad;
        this.facebook = facebook;
        this.instagram = instagram;
        this.tiktok = tiktok;
        this.twitter = twitter;
        this.chats = chats;
        this.eventos = eventos;
    }

    public Usuario(String id, String nombre, String foto, String desc, String edad, String ciudad, String facebook, String instagram, String tiktok, String twitter) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.desc = desc;
        this.edad = edad;
        this.ciudad = ciudad;
        this.facebook = facebook;
        this.instagram = instagram;
        this.tiktok = tiktok;
        this.twitter = twitter;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTiktok() {
        return tiktok;
    }

    public void setTiktok(String tiktok) {
        this.tiktok = tiktok;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public void agregarEvento(Evento nEvento){
        eventos.add(nEvento);
    }

    public List<String> getChats() {
        return chats;
    }

    public void setChats(ArrayList<String> chats) {
        this.chats = chats;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(ArrayList<Evento> eventos) {
        this.eventos = eventos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", foto='" + foto + '\'' +
                ", edad='" + edad + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", facebook='" + facebook + '\'' +
                ", instagram='" + instagram + '\'' +
                ", tiktok='" + tiktok + '\'' +
                ", twitter='" + twitter + '\'' +
                '}';
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
