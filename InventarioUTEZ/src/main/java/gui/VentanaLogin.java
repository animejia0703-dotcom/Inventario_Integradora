package gui;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private static final String USUARIO_VALIDO = "root";
    private static final String CONTRASENA_VALIDA = "1234";

    private final JTextField campoUsuario = new JTextField(15);
    private final JPasswordField campoContrasena = new JPasswordField(15);

    public VentanaLogin() {
        setTitle("Inventarios de Equipos de Computo - Iniciar sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(construirPanel());
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel construirPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(campoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contrasena:"), gbc);
        gbc.gridx = 1;
        panel.add(campoContrasena, gbc);

        JButton botonIngresar = new JButton("Ingresar");
        botonIngresar.addActionListener(e -> intentarIngresar());
        campoContrasena.addActionListener(e -> intentarIngresar());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(botonIngresar, gbc);

        return panel;
    }

    private void intentarIngresar() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword());

        if (usuario.equals(USUARIO_VALIDO) && contrasena.equals(CONTRASENA_VALIDA)) {
            dispose();
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contrasena incorrectos.",
                    "Error de acceso", JOptionPane.ERROR_MESSAGE);
            campoContrasena.setText("");
        }
    }
}
