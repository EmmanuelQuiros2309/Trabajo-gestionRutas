# Sistema de Gestión de Rutas Java
# Integrantes
-Emmanuel Quiros
-Sai Galvan 

Proyecto académico Java 17 + Swing + lista ligada propia + Gson + Maven.

## Ejecutar en VS Code
1. Instalar JDK 17 o superior.
2. Instalar Visual Studio Code y la extensión **Extension Pack for Java**.
3. Abrir en VS Code la carpeta que contiene `pom.xml`.
4. Esperar a que Maven descargue Gson.
5. Abrir `src/main/java/com/gestionrutas/Main.java`.
6. Pulsar **Run** sobre el método `main`.

También se puede ejecutar desde una terminal ubicada en la carpeta del proyecto:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.gestionrutas.Main"
```
Si Maven no tiene configurado el plugin exec, ejecutar desde VS Code con el botón Run del método `main`.

## Funcionamiento
- Las paradas se almacenan en una lista ligada simple implementada con `Nodo` y `ListaLigada`.
- La interfaz Swing permite agregar, seleccionar, modificar, eliminar y mostrar paradas.
- `GestorJSON` usa Gson para guardar/cargar un arreglo JSON.
- La distancia se calcula sumando la distancia Haversine entre paradas consecutivas.
