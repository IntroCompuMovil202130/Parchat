package com.example.parchat;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Usuario implements Serializable {
    public String id, nombre,  desc, edad, ciudad, facebook, instagram, tiktok, twitter, genero, interes, perfil, token;
    public Uri foto;

    public Usuario(){}

    public Usuario(String id, String nombre, String desc, String edad, String ciudad, String facebook, String instagram, String tiktok, String twitter) {
        this.id = id;
        this.nombre = nombre;
        this.desc = desc;
        this.edad = edad;
        this.ciudad = ciudad;
        this.facebook = facebook;
        this.instagram = instagram;
        this.tiktok = tiktok;
        this.twitter = twitter;
        this.perfil = "1";
    }

    @NonNull
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", desc='" + desc + '\'' +
                ", edad='" + edad + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", facebook='" + facebook + '\'' +
                ", instagram='" + instagram + '\'' +
                ", tiktok='" + tiktok + '\'' +
                ", twitter='" + twitter + '\'' +
                ", genero='" + genero + '\'' +
                ", interes='" + interes + '\'' +
                ", perfil='" + perfil + '\'' +
                '}';
    }

    public static Usuario makeUsuario(String id, String nombre){
        Usuario u = new Usuario();
        u.id = id;
        u.nombre = nombre;
        return u;
    }
}
