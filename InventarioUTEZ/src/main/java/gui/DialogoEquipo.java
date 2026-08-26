package gui;

import dao.EstadoFisicoDAO;
import dao.TipoEquipoDAO;
import dao.UbicacionDAO;
import modelo.Equipo;
import modelo.EstadoFisico;
import modelo.TipoEquipo;
import modelo.Ubicacion;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

// Formulario para registrar un equipo nuevo o editar uno existente,
// segun si equipoAEditar viene null o no.
public class DialogoEquipo extends JDialog {

    private final JTextField campoNombre = new JTextField(20);
    private final JComboBox<TipoEquipo> comboTipo = new JComboBox<>();
    private final JTextField campoMarca = new JTextField(20);
    private final JTextField campoModelo = new JTextField(20);
    private final JTextField campoNumeroSerie = new JTextField(20);
    private final JComboBox<EstadoFisico> comboEstado = new JComboBox<>();
    private final JTextField campoNotas = new JTextField(20);
    private final JComboBox<Ubicacion> comboUbicacion = new JComboBox<>();
    private final JTextField campoResponsable = new JTextField(20);

    private boolean guardado = false;
    private final Equipo equipoAEditar;

    public DialogoEquipo(Frame propietario, Equipo equipoAEditar) {
        super(propietario, equipoAEditar == null ? "Registrar equipo" : "Editar equipo", true);
        this.equipoAEditar = equipoAEditar;

        construirFormulario();
        cargarCatalogos();

        if (equipoAEditar != null) {
            precargarDatos(equipoAEditar);
        }

        pack();
        setLocationRelativeTo(propietario);
    }

    private void construirFormulario() {
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(campoNombre);

        panelFormulario.add(new JLabel("Tipo de equipo:"));
        panelFormulario.add(comboTipo);

        panelFormulario.add(new JLabel("Marca:"));
        panelFormulario.add(campoMarca);

        panelFormulario.add(new JLabel("Modelo:"));
        panelFormulario.add(campoModelo);

        panelFormulario.add(new JLabel("Numero de serie:"));
        panelFormulario.add(campoNumeroSerie);

        panelFormulario.add(new JLabel("Estado fisico:"));
        panelFormulario.add(comboEstado);

        panelFormulario.add(new JLabel("Notas:"));
        panelFormulario.add(campoNotas);

        panelFormulario.add(new JLabel("Ubicacion:"));
        panelFormulario.add(comboUbicacion);
        // La ubicacion se cambia aparte, con el boton "Cambiar ubicacion".
        if (equipoAEditar != null) {
            comboUbicacion.setEnabled(false);
        }

        panelFormulario.add(new JLabel("Responsable:"));
        panelFormulario.add(campoResponsable);

        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");

        botonGuardar.addActionListener(e -> {
            if (validarCampos()) {
                guardado = true;
                setVisible(false);
            }
        });
        botonCancelar.addActionListener(e -> {
            guardado = false;
            setVisible(false);
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);

        setLayout(new BorderLayout());
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCatalogos() {
        try {
            List<TipoEquipo> tipos = new TipoEquipoDAO().listarTodos();
            for (TipoEquipo t : tipos) comboTipo.addItem(t);

            List<EstadoFisico> estados = new EstadoFisicoDAO().listarTodos();
            for (EstadoFisico e : estados) comboEstado.addItem(e);

            List<Ubicacion> ubicaciones = new UbicacionDAO().listarTodas();
            for (Ubicacion u : ubicaciones) comboUbicacion.addItem(u);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los catalogos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void precargarDatos(Equipo equipo) {
        campoNombre.setText(equipo.getNombre());
        comboTipo.setSelectedItem(equipo.getTipo());
        campoMarca.setText(equipo.getMarca());
        campoModelo.setText(equipo.getModelo());
        campoNumeroSerie.setText(equipo.getNumeroSerie());
        comboEstado.setSelectedItem(equipo.getEstado());
        campoNotas.setText(equipo.getNotas());
        comboUbicacion.setSelectedItem(equipo.getUbicacion());
        campoResponsable.setText(equipo.getResponsable());
    }

    private boolean validarCampos() {
        if (campoNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del equipo es obligatorio.");
            return false;
        }
        if (comboTipo.getSelectedItem() == null || comboEstado.getSelectedItem() == null
                || comboUbicacion.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona tipo, estado y ubicacion.");
            return false;
        }
        return true;
    }

    public boolean isGuardado() {
        return guardado;
    }

    // Si estamos editando, conserva el id y la ubicacion originales.
    public Equipo obtenerEquipoCapturado() {
        Equipo equipo = new Equipo();
        if (equipoAEditar != null) {
            equipo.setIdEquipo(equipoAEditar.getIdEquipo());
            equipo.setUbicacion(equipoAEditar.getUbicacion());
        } else {
            equipo.setUbicacion((Ubicacion) comboUbicacion.getSelectedItem());
        }
        equipo.setNombre(campoNombre.getText().trim());
        equipo.setTipo((TipoEquipo) comboTipo.getSelectedItem());
        equipo.setMarca(campoMarca.getText().trim());
        equipo.setModelo(campoModelo.getText().trim());
        equipo.setNumeroSerie(campoNumeroSerie.getText().trim());
        equipo.setEstado((EstadoFisico) comboEstado.getSelectedItem());
        equipo.setNotas(campoNotas.getText().trim());
        equipo.setResponsable(campoResponsable.getText().trim());
        return equipo;
    }
}
