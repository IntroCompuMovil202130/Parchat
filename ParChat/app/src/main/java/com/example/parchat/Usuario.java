package com.example.parchat;

import java.io.Serializable;

public class Usuario implements Serializable {
    public String id, nombre, foto, desc, edad, ciudad, facebook, instagram, tiktok, twitter, genero, interes, perfil;

    public Usuario(){}

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
        this.perfil = "1";
    }
}
