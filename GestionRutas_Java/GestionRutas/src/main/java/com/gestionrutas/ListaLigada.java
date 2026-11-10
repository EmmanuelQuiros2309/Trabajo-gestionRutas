package com.gestionrutas;

import java.util.ArrayList;
import java.util.List;

public class ListaLigada {
    private Nodo cabeza;
    private int tamanio;

    public ListaLigada() {
        cabeza = null;
        tamanio = 0;
    }

    public void agregar(Parada parada) {
        Nodo nuevo = new Nodo(parada);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamanio++;
    }

    public Parada obtener(int indice) {
        if (indice < 0 || indice >= tamanio) return null;
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) actual = actual.getSiguiente();
        return actual.getDato();
    }

    public boolean modificar(int indice, Parada nuevaParada) {
        if (indice < 0 || indice >= tamanio) return false;
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) actual = actual.getSiguiente();
        actual.setDato(nuevaParada);
        return true;
    }

    public boolean eliminar(int indice) {
        if (indice < 0 || indice >= tamanio) return false;
        if (indice == 0) {
            cabeza = cabeza.getSiguiente();
        } else {
            Nodo anterior = cabeza;
            for (int i = 0; i < indice - 1; i++) anterior = anterior.getSiguiente();
            anterior.setSiguiente(anterior.getSiguiente().getSiguiente());
        }
        tamanio--;
        return true;
    }

    public int getTamanio() { return tamanio; }

    public void limpiar() {
        cabeza = null;
        tamanio = 0;
    }

    public List<Parada> aListaJava() {
        List<Parada> resultado = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            resultado.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    public void cargarDesdeLista(List<Parada> paradas) {
        limpiar();
        if (paradas != null) {
            for (Parada parada : paradas) agregar(parada);
        }
    }

    public double calcularDistanciaTotalKm() {
        if (tamanio < 2) return 0.0;
        double total = 0.0;
        Nodo actual = cabeza;
        while (actual.getSiguiente() != null) {
            Parada origen = actual.getDato();
            Parada destino = actual.getSiguiente().getDato();
            total += haversine(origen.getLatitud(), origen.getLongitud(),
                               destino.getLatitud(), destino.getLongitud());
            actual = actual.getSiguiente();
        }
        return total;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double RADIO_TIERRA_KM = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA_KM * c;
    }
}
