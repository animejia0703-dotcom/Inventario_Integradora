package modelo;

import java.time.LocalDateTime;

public class HistorialUbicacion {
    private int idHistorial;
    private int idEquipo;
    private String nombreEquipo;
    private Ubicacion ubicacion;
    private LocalDateTime fechaCambio;

    public HistorialUbicacion() {
    }

    public HistorialUbicacion(int idHistorial, int idEquipo, Ubicacion ubicacion, LocalDateTime fechaCambio) {
        this.idHistorial = idHistorial;
        this.idEquipo = idEquipo;
        this.ubicacion = ubicacion;
        this.fechaCambio = fechaCambio;
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

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}
