package modelo;

public class Ubicacion {
    private int idUbicacion;
    private String edificio;
    private String aula;

    public Ubicacion() {
    }

    public Ubicacion(int idUbicacion, String edificio, String aula) {
        this.idUbicacion = idUbicacion;
        this.edificio = edificio;
        this.aula = aula;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    @Override
    public String toString() {
        return edificio + " - " + aula;
    }

    @Override
    public boolean equals(Object otro) {
        // Sin esto, el combo no reconoce dos objetos con el mismo id como iguales.
        if (this == otro) return true;
        if (!(otro instanceof Ubicacion)) return false;
        return idUbicacion == ((Ubicacion) otro).idUbicacion;
    }

    @Override
    public int hashCode() {
        return idUbicacion;
    }
}
