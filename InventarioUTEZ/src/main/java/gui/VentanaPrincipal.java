package gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Inventarios de Equipos de Computo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        PanelEquipos panelEquipos = new PanelEquipos(this);
        PanelHistorial panelHistorial = new PanelHistorial();
        pestanas.addTab("Equipos", panelEquipos);
        pestanas.addTab("Ubicaciones", new PanelUbicaciones());
        pestanas.addTab("Historial", panelHistorial);

        // Refresca la pestana activa por si hubo cambios en otra.
        pestanas.addChangeListener(e -> {
            Component seleccionada = pestanas.getSelectedComponent();
            if (seleccionada == panelEquipos) {
                panelEquipos.refrescarAlEntrar();
            } else if (seleccionada == panelHistorial) {
                panelHistorial.refrescarAlEntrar();
            }
        });

        setLayout(new BorderLayout());
        add(construirBarraSuperior(), BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
    }

    private JPanel construirBarraSuperior() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonCerrarSesion = new JButton("Cerrar sesion");
        botonCerrarSesion.addActionListener(e -> cerrarSesion());
        barra.add(botonCerrarSesion);
        return barra;
    }

    private void cerrarSesion() {
        dispose();
        VentanaLogin login = new VentanaLogin();
        login.setVisible(true);
    }
}
