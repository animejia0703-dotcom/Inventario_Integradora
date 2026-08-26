package dao;

import modelo.Ubicacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UbicacionDAO {

    public List<Ubicacion> listarTodas() throws SQLException {
        List<Ubicacion> lista = new ArrayList<>();
        String sql = "SELECT id_ubicacion, edificio, aula FROM ubicaciones WHERE activa = 1 ORDER BY edificio, aula";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Ubicacion(rs.getInt("id_ubicacion"), rs.getString("edificio"), rs.getString("aula")));
            }
        }
        return lista;
    }

    public void agregar(Ubicacion ubicacion) throws SQLException {
        String sql = "INSERT INTO ubicaciones (edificio, aula) VALUES (?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ubicacion.getEdificio());
            ps.setString(2, ubicacion.getAula());
            ps.executeUpdate();
        }
    }

    public void actualizar(Ubicacion ubicacion) throws SQLException {
        String sql = "UPDATE ubicaciones SET edificio = ?, aula = ? WHERE id_ubicacion = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ubicacion.getEdificio());
            ps.setString(2, ubicacion.getAula());
            ps.setInt(3, ubicacion.getIdUbicacion());
            ps.executeUpdate();
        }
    }

    public void eliminar(int idUbicacion) throws SQLException {
        // Baja logica: el historial guarda ubicaciones pasadas para siempre.
        String sql = "UPDATE ubicaciones SET activa = 0 WHERE id_ubicacion = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUbicacion);
            ps.executeUpdate();
        }
    }
}
