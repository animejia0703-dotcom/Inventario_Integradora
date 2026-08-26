package modelo;

public class EstadoFisico {
    private int idEstado;
    private String nombre;

    public EstadoFisico() {
    }

    public EstadoFisico(int idEstado, String nombre) {
        this.idEstado = idEstado;
        this.nombre = nombre;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof EstadoFisico)) return false;
        return idEstado == ((EstadoFisico) otro).idEstado;
    }

    @Override
    public int hashCode() {
        return idEstado;
    }
}
