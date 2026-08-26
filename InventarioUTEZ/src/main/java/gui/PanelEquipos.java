package gui;

import dao.EquipoDAO;
import dao.UbicacionDAO;
import modelo.Equipo;
import modelo.Ubicacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelEquipos extends JPanel {

    private final EquipoDAO equipoDAO = new EquipoDAO();

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Tipo", "Marca", "Modelo", "N. Serie", "Estado", "Ubicacion", "Responsable"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // La tabla es solo de lectura, la edicion se hace por el boton "Editar".
        }
    };
    private final JTable tablaEquipos = new JTable(modeloTabla);

    private final JComboBox<String> comboFiltroUbicacion = new JComboBox<>();

    public PanelEquipos(Frame ventanaPrincipal) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirPanelSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(tablaEquipos), BorderLayout.CENTER);
        add(construirPanelBotones(ventanaPrincipal), BorderLayout.SOUTH);

        cargarFiltroUbicaciones();
        cargarEquipos(null);
    }

    private JPanel construirPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Consultar por laboratorio:"));
        panel.add(comboFiltroUbicacion);

        comboFiltroUbicacion.addActionListener(e -> {
            String seleccion = (String) comboFiltroUbicacion.getSelectedItem();
            if ("Todos".equals(seleccion) || seleccion == null) {
                cargarEquipos(null);
            } else {
                cargarEquipos(seleccion);
            }
        });
        return panel;
    }

    private JPanel construirPanelBotones(Frame ventanaPrincipal) {
        JButton botonAgregar = new JButton("Registrar equipo");
        JButton botonEditar = new JButton("Editar");
        JButton botonCambiarUbicacion = new JButton("Cambiar ubicacion");
        JButton botonEliminar = new JButton("Eliminar");

        botonAgregar.addActionListener(e -> {
            DialogoEquipo dialogo = new DialogoEquipo(ventanaPrincipal, null);
            dialogo.setVisible(true);
            if (dialogo.isGuardado()) {
                guardarNuevoEquipo(dialogo.obtenerEquipoCapturado());
            }
        });

        botonEditar.addActionListener(e -> {
            Equipo seleccionado = obtenerEquipoSeleccionado();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un equipo de la tabla primero.");
                return;
            }
            DialogoEquipo dialogo = new DialogoEquipo(ventanaPrincipal, seleccionado);
            dialogo.setVisible(true);
            if (dialogo.isGuardado()) {
                actualizarEquipo(dialogo.obtenerEquipoCapturado());
            }
        });

        botonCambiarUbicacion.addActionListener(e -> {
            Equipo seleccionado = obtenerEquipoSeleccionado();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un equipo de la tabla primero.");
                return;
            }
            DialogoCambioUbicacion dialogo = new DialogoCambioUbicacion(ventanaPrincipal, seleccionado.getUbicacion());
            dialogo.setVisible(true);
            if (dialogo.isConfirmado()) {
                Ubicacion nueva = dialogo.obtenerUbicacionSeleccionada();
                cambiarUbicacionEquipo(seleccionado.getIdEquipo(), nueva.getIdUbicacion());
            }
        });

        botonEliminar.addActionListener(e -> {
            Equipo seleccionado = obtenerEquipoSeleccionado();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un equipo de la tabla primero.");
                return;
            }
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que quieres eliminar \"" + seleccionado.getNombre() + "\"?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                eliminarEquipo(seleccionado.getIdEquipo());
            }
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(botonAgregar);
        panel.add(botonEditar);
        panel.add(botonCambiarUbicacion);
        panel.add(botonEliminar);
        return panel;
    }

    // Refresca el filtro y la tabla al entrar a esta pestana, por si se
    // agrego una ubicacion nueva desde la pestana "Ubicaciones".
    public void refrescarAlEntrar() {
        comboFiltroUbicacion.removeAllItems();
        cargarFiltroUbicaciones();
        cargarEquipos(null);
    }

    private void cargarFiltroUbicaciones() {
        comboFiltroUbicacion.addItem("Todos");
        try {
            List<Ubicacion> ubicaciones = new UbicacionDAO().listarTodas();
            for (Ubicacion u : ubicaciones) {
                comboFiltroUbicacion.addItem(u.toString());
            }
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las ubicaciones para el filtro", e);
        }
    }

    private void cargarEquipos(String textoUbicacion) {
        modeloTabla.setRowCount(0);
        try {
            List<Equipo> equipos = equipoDAO.listarTodos();
            for (Equipo eq : equipos) {
                if (textoUbicacion != null && !eq.getUbicacion().toString().equals(textoUbicacion)) {
                    continue;
                }
                modeloTabla.addRow(new Object[]{
                        eq.getIdEquipo(), eq.getNombre(), eq.getTipo(), eq.getMarca(), eq.getModelo(),
                        eq.getNumeroSerie(), eq.getEstado(), eq.getUbicacion(), eq.getResponsable()
                });
            }
        } catch (SQLException e) {
            mostrarError("No se pudo cargar el listado de equipos", e);
        }
    }

    private Equipo obtenerEquipoSeleccionado() {
        int fila = tablaEquipos.getSelectedRow();
        if (fila == -1) return null;

        try {
            int idEquipo = (int) modeloTabla.getValueAt(fila, 0);
            // La tabla solo tiene texto, pedimos la lista real para poder editar.
            for (Equipo eq : equipoDAO.listarTodos()) {
                if (eq.getIdEquipo() == idEquipo) return eq;
            }
        } catch (SQLException e) {
            mostrarError("No se pudo obtener el equipo seleccionado", e);
        }
        return null;
    }

    private void guardarNuevoEquipo(Equipo equipo) {
        try {
            equipoDAO.registrar(equipo);
            cargarEquipos(null);
            JOptionPane.showMessageDialog(this, "Equipo registrado correctamente.");
        } catch (SQLException e) {
            mostrarError("No se pudo registrar el equipo", e);
        }
    }

    private void actualizarEquipo(Equipo equipo) {
        try {
            equipoDAO.actualizarDatos(equipo);
            cargarEquipos(null);
            JOptionPane.showMessageDialog(this, "Equipo actualizado correctamente.");
        } catch (SQLException e) {
            mostrarError("No se pudo actualizar el equipo", e);
        }
    }

    private void cambiarUbicacionEquipo(int idEquipo, int idNuevaUbicacion) {
        try {
            equipoDAO.cambiarUbicacion(idEquipo, idNuevaUbicacion);
            cargarEquipos(null);
            JOptionPane.showMessageDialog(this, "Ubicacion actualizada y guardada en el historial.");
        } catch (SQLException e) {
            mostrarError("No se pudo cambiar la ubicacion", e);
        }
    }

    private void eliminarEquipo(int idEquipo) {
        try {
            equipoDAO.eliminar(idEquipo);
            cargarEquipos(null);
        } catch (SQLException e) {
            mostrarError("No se pudo eliminar el equipo", e);
        }
    }

    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(this, mensaje + ":\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
