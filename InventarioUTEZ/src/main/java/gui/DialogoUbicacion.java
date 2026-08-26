package gui;

import modelo.Ubicacion;

import javax.swing.*;
import java.awt.*;

public class DialogoUbicacion extends JDialog {

    private static final String[] EDIFICIOS = {
            "Docencia 1", "Docencia 2", "Docencia 3", "Docencia 4", "Docencia 5",
            "Cecadec", "Ceviset", "Taller Pesado 1", "Taller Pesado 2", "Rectoria", "Cedim"
    };

    private final JComboBox<String> comboEdificio = new JComboBox<>(EDIFICIOS);
    private final JTextField campoAula = new JTextField(12);
    private final Ubicacion ubicacionOriginal;
    private boolean guardado = false;

    public DialogoUbicacion(Frame propietario, Ubicacion ubicacion) {
        super(propietario, "Editar ubicacion", true);
        this.ubicacionOriginal = ubicacion;

        comboEdificio.setSelectedItem(ubicacion.getEdificio());
        campoAula.setText(ubicacion.getAula());

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(new JLabel("Edificio:"));
        panel.add(comboEdificio);
        panel.add(new JLabel("Aula:"));
        panel.add(campoAula);

        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        botonGuardar.addActionListener(e -> {
            if (campoAula.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escribe el aula.");
                return;
            }
            guardado = true;
            setVisible(false);
        });
        botonCancelar.addActionListener(e -> {
            guardado = false;
            setVisible(false);
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(propietario);
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Ubicacion obtenerUbicacionCapturada() {
        return new Ubicacion(
                ubicacionOriginal.getIdUbicacion(),
                (String) comboEdificio.getSelectedItem(),
                campoAula.getText().trim());
    }
}
