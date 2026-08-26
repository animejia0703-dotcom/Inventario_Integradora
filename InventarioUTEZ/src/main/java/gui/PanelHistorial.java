package gui;

import dao.EquipoDAO;
import dao.HistorialEstadoDAO;
import dao.HistorialUbicacionDAO;
import modelo.Equipo;
import modelo.HistorialEstado;
import modelo.HistorialUbicacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PanelHistorial extends JPanel {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final HistorialUbicacionDAO historialUbicacionDAO = new HistorialUbicacionDAO();
    private final HistorialEstadoDAO historialEstadoDAO = new HistorialEstadoDAO();
    private final EquipoDAO equipoDAO = new EquipoDAO();

    private final DefaultTableModel modeloMovimientos = new DefaultTableModel(
            new Object[]{"ID", "Equipo", "Tipo de cambio", "Nuevo valor", "Fecha del cambio"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaMovimientos = new JTable(modeloMovimientos);

    private final DefaultTableModel modeloEliminados = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Marca", "Modelo", "N. Serie", "Fecha de baja"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaEliminados = new JTable(modeloEliminados);

    public PanelHistorial() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirPanelSuperior(), BorderLayout.NORTH);
        add(construirSplitPrincipal(), BorderLayout.CENTER);

        cargarHistorial();
    }

    private JPanel construirPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Cambios de ubicacion y de estado de los equipos:"));

        JButton botonActualizar = new JButton("Actualizar");
        botonActualizar.addActionListener(e -> cargarHistorial());
        panel.add(botonActualizar);

        return panel;
    }

    private JSplitPane construirSplitPrincipal() {
        JPanel panelEliminados = new JPanel(new BorderLayout(5, 5));
        panelEliminados.add(new JLabel("Equipos eliminados"), BorderLayout.NORTH);
        panelEliminados.add(new JScrollPane(tablaEliminados), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tablaMovimientos), panelEliminados);
        split.setResizeWeight(0.65);
        return split;
    }

    // Se llama al entrar a esta pestana, para reflejar cambios hechos en "Equipos".
    public void refrescarAlEntrar() {
        cargarHistorial();
    }

    private void cargarHistorial() {
        cargarMovimientos();
        cargarEliminados();
    }

    private void cargarMovimientos() {
        modeloMovimientos.setRowCount(0);
        try {
            List<FilaHistorial> filas = new ArrayList<>();

            for (HistorialUbicacion h : historialUbicacionDAO.listarTodo()) {
                filas.add(new FilaHistorial(h.getIdHistorial(), h.getNombreEquipo(), "Ubicacion",
                        h.getUbicacion().toString(), h.getFechaCambio()));
            }
            for (HistorialEstado h : historialEstadoDAO.listarTodo()) {
                filas.add(new FilaHistorial(h.getIdHistorial(), h.getNombreEquipo(), "Estado",
                        h.getEstado().toString(), h.getFechaCambio()));
            }
            filas.sort(Comparator.comparing(FilaHistorial::fecha).reversed());

            for (FilaHistorial fila : filas) {
                modeloMovimientos.addRow(new Object[]{
                        fila.id(), fila.equipo(), fila.tipo(), fila.valor(), fila.fecha().format(FORMATO_FECHA)
                });
            }
        } catch (SQLException e) {
            mostrarError("No se pudo cargar el historial:\n" + e.getMessage());
        }
    }

    private void cargarEliminados() {
        modeloEliminados.setRowCount(0);
        try {
            List<Equipo> eliminados = equipoDAO.listarEliminados();
            for (Equipo eq : eliminados) {
                String fecha = eq.getFechaBaja() != null ? eq.getFechaBaja().format(FORMATO_FECHA) : "";
                modeloEliminados.addRow(new Object[]{
                        eq.getIdEquipo(), eq.getNombre(), eq.getMarca(), eq.getModelo(), eq.getNumeroSerie(), fecha
                });
            }
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los equipos eliminados:\n" + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Fila comun para poder mezclar y ordenar juntos los dos tipos de historial.
    private record FilaHistorial(int id, String equipo, String tipo, String valor, LocalDateTime fecha) {
    }
}
