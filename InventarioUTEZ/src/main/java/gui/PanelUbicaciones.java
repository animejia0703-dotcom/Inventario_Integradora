package gui;

import dao.UbicacionDAO;
import modelo.Ubicacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelUbicaciones extends JPanel {

    private final UbicacionDAO ubicacionDAO = new UbicacionDAO();

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Edificio", "Aula"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaUbicaciones = new JTable(modeloTabla);

    // Edificios fijos de la institucion; el aula queda libre porque varia mucho.
    private static final String[] EDIFICIOS = {
            "Docencia 1", "Docencia 2", "Docencia 3", "Docencia 4", "Docencia 5",
            "Cecadec", "Ceviset", "Taller Pesado 1", "Taller Pesado 2", "Rectoria", "Cedim"
    };

    private final JComboBox<String> comboEdificio = new JComboBox<>(EDIFICIOS);
    private final JTextField campoAula = new JTextField(12);

    public PanelUbicaciones() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirFormularioAlta(), BorderLayout.NORTH);
        add(new JScrollPane(tablaUbicaciones), BorderLayout.CENTER);

        cargarUbicaciones();
    }

    private JPanel construirFormularioAlta() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Edificio:"));
        panel.add(comboEdificio);
        panel.add(new JLabel("Aula:"));
        panel.add(campoAula);

        JButton botonAgregar = new JButton("Agregar ubicacion");
        botonAgregar.addActionListener(e -> agregarUbicacion());
        panel.add(botonAgregar);

        JButton botonEditar = new JButton("Editar ubicacion");
        botonEditar.addActionListener(e -> editarUbicacion());
        panel.add(botonEditar);

        JButton botonEliminar = new JButton("Eliminar ubicacion");
        botonEliminar.addActionListener(e -> eliminarUbicacion());
        panel.add(botonEliminar);

        return panel;
    }

    private Ubicacion obtenerUbicacionSeleccionada() {
        int fila = tablaUbicaciones.getSelectedRow();
        if (fila == -1) return null;

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String edificio = (String) modeloTabla.getValueAt(fila, 1);
        String aula = (String) modeloTabla.getValueAt(fila, 2);
        return new Ubicacion(id, edificio, aula);
    }

    private void editarUbicacion() {
        Ubicacion seleccionada = obtenerUbicacionSeleccionada();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una ubicacion de la tabla primero.");
            return;
        }

        Frame ventana = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogoUbicacion dialogo = new DialogoUbicacion(ventana, seleccionada);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            try {
                ubicacionDAO.actualizar(dialogo.obtenerUbicacionCapturada());
                cargarUbicaciones();
            } catch (SQLException e) {
                mostrarError("No se pudo editar la ubicacion:\n" + e.getMessage());
            }
        }
    }

    private void eliminarUbicacion() {
        int fila = tablaUbicaciones.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una ubicacion de la tabla primero.");
            return;
        }

        int idUbicacion = (int) modeloTabla.getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar esta ubicacion?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            ubicacionDAO.eliminar(idUbicacion);
            cargarUbicaciones();
        } catch (SQLException e) {
            mostrarError("No se pudo eliminar la ubicacion:\n" + e.getMessage());
        }
    }

    private void agregarUbicacion() {
        String edificio = (String) comboEdificio.getSelectedItem();
        String aula = campoAula.getText().trim();

        if (aula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el aula.");
            return;
        }

        try {
            ubicacionDAO.agregar(new Ubicacion(0, edificio, aula));
            campoAula.setText("");
            cargarUbicaciones();
        } catch (SQLException e) {
            mostrarError("No se pudo agregar la ubicacion:\n" + e.getMessage());
        }
    }

    private void cargarUbicaciones() {
        modeloTabla.setRowCount(0);
        try {
            List<Ubicacion> ubicaciones = ubicacionDAO.listarTodas();
            for (Ubicacion u : ubicaciones) {
                modeloTabla.addRow(new Object[]{u.getIdUbicacion(), u.getEdificio(), u.getAula()});
            }
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las ubicaciones:\n" + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
