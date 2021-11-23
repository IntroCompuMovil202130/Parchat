package com.example.parchat;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

public class Evento implements Serializable {

    public String id;
    public String idorganizador;
    public boolean organizador;
    public String nombreEvento;
    public String lugar;
    public String fecha;
    public double latitud;
    public double longitud;
    public HashMap<String,Posicion> participantes;

    public Evento() {
        participantes = new HashMap<String,Posicion>();
    }

    public Evento(String id,String nombreEvento, String lugar,
                  String fecha, double latitud, double longitud,boolean organizador,String idOrganizador) {
        this.id = id;
        this.idorganizador = idOrganizador;
        this.nombreEvento = nombreEvento;
        this.lugar = lugar;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.participantes = new HashMap<String,Posicion>();
        this.organizador = organizador;
    }

    @NonNull
    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", organizador=" + organizador +
                ", nombreEvento='" + nombreEvento + '\'' +
                ", lugar='" + lugar + '\'' +
                ", fecha='" + fecha + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", participantes=" + participantes +
                '}';
    }
}
