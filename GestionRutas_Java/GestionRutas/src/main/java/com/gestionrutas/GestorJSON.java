package com.gestionrutas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class GestorJSON {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void guardar(ListaLigada ruta, Path archivo) throws IOException {
        String json = gson.toJson(ruta.aListaJava());
        Files.writeString(archivo, json, StandardCharsets.UTF_8);
    }

    public void cargar(ListaLigada ruta, Path archivo) throws IOException {
        String json = Files.readString(archivo, StandardCharsets.UTF_8);
        Parada[] datos = gson.fromJson(json, Parada[].class);
        List<Parada> paradas = datos == null ? List.of() : Arrays.asList(datos);
        ruta.cargarDesdeLista(paradas);
    }
}
