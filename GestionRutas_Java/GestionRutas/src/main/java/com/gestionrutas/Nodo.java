package com.gestionrutas;

public class Nodo {
    private Parada dato;
    private Nodo siguiente;

    public Nodo(Parada dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public Parada getDato() { return dato; }
    public Nodo getSiguiente() { return siguiente; }
    public void setDato(Parada dato) { this.dato = dato; }
    public void setSiguiente(Nodo siguiente) { this.siguiente = siguiente; }
}
