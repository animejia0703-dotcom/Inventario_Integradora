package modelo;

public class TipoEquipo {
    private int idTipo;
    private String nombre;

    public TipoEquipo() {
    }

    public TipoEquipo(int idTipo, String nombre) {
        this.idTipo = idTipo;
        this.nombre = nombre;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        // Para que el JComboBox muestre el nombre y no la referencia del objeto.
        return nombre;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof TipoEquipo)) return false;
        return idTipo == ((TipoEquipo) otro).idTipo;
    }

    @Override
    public int hashCode() {
        return idTipo;
    }
}
