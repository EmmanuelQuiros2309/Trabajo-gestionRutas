package com.gestionrutas;

public class Parada {
    private String nombre;
    private double latitud;
    private double longitud;
    private int tiempoLlegada;
    private String descripcion;

    public Parada() { }

    public Parada(String nombre, double latitud, double longitud,
                  int tiempoLlegada, String descripcion) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tiempoLlegada = tiempoLlegada;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public int getTiempoLlegada() { return tiempoLlegada; }
    public String getDescripcion() { return descripcion; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public void setTiempoLlegada(int tiempoLlegada) { this.tiempoLlegada = tiempoLlegada; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
