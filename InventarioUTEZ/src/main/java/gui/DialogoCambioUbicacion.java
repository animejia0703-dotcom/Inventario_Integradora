package gui;

import dao.UbicacionDAO;
import modelo.Ubicacion;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DialogoCambioUbicacion extends JDialog {

    private final JComboBox<Ubicacion> comboUbicacion = new JComboBox<>();
    private boolean confirmado = false;

    public DialogoCambioUbicacion(Frame propietario, Ubicacion ubicacionActual) {
        super(propietario, "Cambiar ubicacion", true);

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(new JLabel("Ubicacion actual: " + ubicacionActual));
        panel.add(new JLabel("Nueva ubicacion:"));
        panel.add(comboUbicacion);

        cargarUbicaciones();

        JButton botonConfirmar = new JButton("Confirmar");
        JButton botonCancelar = new JButton("Cancelar");
        botonConfirmar.addActionListener(e -> {
            confirmado = true;
            setVisible(false);
        });
        botonCancelar.addActionListener(e -> {
            confirmado = false;
            setVisible(false);
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonConfirmar);
        panelBotones.add(botonCancelar);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(propietario);
    }

    private void cargarUbicaciones() {
        try {
            List<Ubicacion> ubicaciones = new UbicacionDAO().listarTodas();
            for (Ubicacion u : ubicaciones) comboUbicacion.addItem(u);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar las ubicaciones: " + e.getMessage());
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Ubicacion obtenerUbicacionSeleccionada() {
        return (Ubicacion) comboUbicacion.getSelectedItem();
    }
}
