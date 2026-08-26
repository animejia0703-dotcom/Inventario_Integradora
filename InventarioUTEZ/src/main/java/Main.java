import gui.VentanaLogin;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Todo lo que sea interfaz grafica en Swing se ejecuta en su propio hilo.
        SwingUtilities.invokeLater(() -> {
            VentanaLogin login = new VentanaLogin();
            login.setVisible(true);
        });
    }
}
