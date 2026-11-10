package com.gestionrutas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class VentanaPrincipal extends JFrame {
    private final ListaLigada ruta = new ListaLigada();
    private final GestorJSON gestorJSON = new GestorJSON();

    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtLatitud = new JTextField(8);
    private final JTextField txtLongitud = new JTextField(8);
    private final JTextField txtTiempo = new JTextField(8);
    private final JTextField txtDescripcion = new JTextField(20);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Nombre", "Latitud", "Longitud", "Tiempo (min)", "Descripción"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private final JLabel lblDistancia = new JLabel("Distancia total aproximada: 0.00 km");

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Rutas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 570);
        setLocationRelativeTo(null);
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel formulario = new JPanel(new GridLayout(2, 5, 8, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Información de la parada"));
        formulario.add(new JLabel("Nombre"));
        formulario.add(new JLabel("Latitud"));
        formulario.add(new JLabel("Longitud"));
        formulario.add(new JLabel("Tiempo (min)"));
        formulario.add(new JLabel("Descripción"));
        formulario.add(txtNombre);
        formulario.add(txtLatitud);
        formulario.add(txtLongitud);
        formulario.add(txtTiempo);
        formulario.add(txtDescripcion);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnSeleccionar = new JButton("Seleccionar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnMostrar = new JButton("Mostrar paradas");
        JButton btnGuardar = new JButton("Guardar ruta");
        JButton btnCargar = new JButton("Cargar ruta");
        JButton btnDistancia = new JButton("Calcular distancia");
        JButton btnLimpiar = new JButton("Limpiar campos");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        for (JButton b : new JButton[]{btnAgregar, btnSeleccionar, btnModificar,
                btnEliminar, btnMostrar, btnGuardar, btnCargar, btnDistancia, btnLimpiar}) {
            acciones.add(b);
        }

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Paradas registradas"));

        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inferior.add(lblDistancia);

        setLayout(new BorderLayout(10, 10));
        add(formulario, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        JPanel sur = new JPanel(new BorderLayout());
        sur.add(acciones, BorderLayout.CENTER);
        sur.add(inferior, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarParada());
        btnSeleccionar.addActionListener(e -> seleccionarParada());
        btnModificar.addActionListener(e -> modificarParada());
        btnEliminar.addActionListener(e -> eliminarParada());
        btnMostrar.addActionListener(e -> actualizarTabla());
        btnGuardar.addActionListener(e -> guardarRuta());
        btnCargar.addActionListener(e -> cargarRuta());
        btnDistancia.addActionListener(e -> calcularDistancia());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                cargarCamposDesdeSeleccion();
            }
        });
    }

    private Parada leerFormulario() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");

        double lat = Double.parseDouble(txtLatitud.getText().trim().replace(',', '.'));
        double lon = Double.parseDouble(txtLongitud.getText().trim().replace(',', '.'));
        int tiempo = Integer.parseInt(txtTiempo.getText().trim());
        String descripcion = txtDescripcion.getText().trim();

        if (lat < -90 || lat > 90) throw new IllegalArgumentException("La latitud debe estar entre -90 y 90.");
        if (lon < -180 || lon > 180) throw new IllegalArgumentException("La longitud debe estar entre -180 y 180.");
        if (tiempo < 0) throw new IllegalArgumentException("El tiempo no puede ser negativo.");

        return new Parada(nombre, lat, lon, tiempo, descripcion);
    }

    private void agregarParada() {
        try {
            ruta.agregar(leerFormulario());
            actualizarTabla();
            limpiarCampos();
            mensaje("Parada agregada correctamente.");
        } catch (NumberFormatException ex) {
            error("Latitud y longitud deben ser números; el tiempo debe ser un entero.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void seleccionarParada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { error("Selecciona una parada de la tabla."); return; }
        cargarCamposDesdeSeleccion();
    }

    private void modificarParada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { error("Selecciona la parada que deseas modificar."); return; }
        try {
            ruta.modificar(fila, leerFormulario());
            actualizarTabla();
            tabla.setRowSelectionInterval(fila, fila);
            mensaje("Parada modificada correctamente.");
        } catch (NumberFormatException ex) {
            error("Verifica los valores numéricos.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void eliminarParada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { error("Selecciona la parada que deseas eliminar."); return; }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Deseas eliminar la parada seleccionada?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            ruta.eliminar(fila);
            actualizarTabla();
            limpiarCampos();
            mensaje("Parada eliminada.");
        }
    }

    private void cargarCamposDesdeSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        Parada p = ruta.obtener(fila);
        txtNombre.setText(p.getNombre());
        txtLatitud.setText(String.valueOf(p.getLatitud()));
        txtLongitud.setText(String.valueOf(p.getLongitud()));
        txtTiempo.setText(String.valueOf(p.getTiempoLlegada()));
        txtDescripcion.setText(p.getDescripcion() == null ? "" : p.getDescripcion());
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        for (Parada p : ruta.aListaJava()) {
            modelo.addRow(new Object[]{p.getNombre(), p.getLatitud(), p.getLongitud(),
                    p.getTiempoLlegada(), p.getDescripcion()});
        }
        lblDistancia.setText(String.format("Distancia total aproximada: %.2f km",
                ruta.calcularDistanciaTotalKm()));
    }

    private void calcularDistancia() {
        double distancia = ruta.calcularDistanciaTotalKm();
        lblDistancia.setText(String.format("Distancia total aproximada: %.2f km", distancia));
        mensaje(String.format("La distancia total aproximada es %.2f km.", distancia));
    }

    private void guardarRuta() {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new java.io.File("rutas.json"));
        if (selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path archivo = selector.getSelectedFile().toPath();
            try {
                gestorJSON.guardar(ruta, archivo);
                mensaje("Ruta guardada en:\n" + archivo.toAbsolutePath());
            } catch (IOException ex) {
                error("No fue posible guardar el archivo: " + ex.getMessage());
            }
        }
    }

    private void cargarRuta() {
        JFileChooser selector = new JFileChooser();
        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path archivo = selector.getSelectedFile().toPath();
            try {
                gestorJSON.cargar(ruta, archivo);
                actualizarTabla();
                limpiarCampos();
                mensaje("Ruta cargada correctamente.");
            } catch (IOException | com.google.gson.JsonSyntaxException ex) {
                error("No fue posible cargar el JSON: " + ex.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtTiempo.setText("");
        txtDescripcion.setText("");
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
