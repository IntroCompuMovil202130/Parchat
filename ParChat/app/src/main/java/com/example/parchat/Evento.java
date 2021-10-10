package com.example.parchat;

public class Evento {
    private String nombreEvento;
    private String lugar;
    private String fecha;
    private double latitud;
    private double longitud;

    public Evento(String nombreEvento, String lugar, String fecha, double latitud, double longitud) {
        this.nombreEvento = nombreEvento;
        this.lugar = lugar;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
