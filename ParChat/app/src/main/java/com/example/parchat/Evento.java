package com.example.parchat;

public class Evento {
    private String id;
    private String nombreEvento;
    private String lugar;
    private String fecha;
    private double latitud;
    private double longitud;

    public Evento() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return id +"__"+ nombreEvento +"__"+lugar+"__"+fecha+"__"+latitud+"__"+longitud;
    }

    public void convertirString(String eventoString){
        String partes[] = eventoString.split("__");
        this.id = partes[0];
        this.nombreEvento = partes[1];
        this.lugar = partes[2];
        this.fecha = partes[3];
        this.latitud = Double.valueOf(partes[4]);
        this.longitud = Double.valueOf(partes[5]);
    }
}
