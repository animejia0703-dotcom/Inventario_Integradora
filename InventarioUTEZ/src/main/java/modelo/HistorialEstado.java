package modelo;

import java.time.LocalDateTime;

public class HistorialEstado {
    private int idHistorial;
    private int idEquipo;
    private String nombreEquipo;
    private EstadoFisico estado;
    private LocalDateTime fechaCambio;

    public HistorialEstado() {
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public EstadoFisico getEstado() {
        return estado;
    }

    public void setEstado(EstadoFisico estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}
