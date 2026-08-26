package dao;

import modelo.HistorialUbicacion;
import modelo.Ubicacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialUbicacionDAO {

    public void registrarMovimiento(int idEquipo, int idUbicacion, Connection con) throws SQLException {
        // La conexion viene de afuera para que EquipoDAO haga el UPDATE y este
        // INSERT en una sola transaccion.
        String sql = "INSERT INTO historial_ubicaciones (id_equipo, id_ubicacion) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setInt(2, idUbicacion);
            ps.executeUpdate();
        }
    }

    public List<HistorialUbicacion> listarTodo() throws SQLException {
        List<HistorialUbicacion> lista = new ArrayList<>();
        String sql = "SELECT h.id_historial, h.id_equipo, h.fecha_cambio, "
                   + "       u.id_ubicacion, u.edificio, u.aula, "
                   + "       eq.nombre AS nombre_equipo "
                   + "FROM historial_ubicaciones h "
                   + "JOIN ubicaciones u ON h.id_ubicacion = u.id_ubicacion "
                   + "JOIN equipos eq    ON h.id_equipo = eq.id_equipo "
                   + "ORDER BY h.fecha_cambio DESC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        }
        return lista;
    }

    public List<HistorialUbicacion> obtenerHistorialPorEquipo(int idEquipo) throws SQLException {
        List<HistorialUbicacion> lista = new ArrayList<>();
        String sql = "SELECT h.id_historial, h.id_equipo, h.fecha_cambio, "
                   + "       u.id_ubicacion, u.edificio, u.aula "
                   + "FROM historial_ubicaciones h "
                   + "JOIN ubicaciones u ON h.id_ubicacion = u.id_ubicacion "
                   + "WHERE h.id_equipo = ? "
                   + "ORDER BY h.fecha_cambio DESC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearFila(rs));
                }
            }
        }
        return lista;
    }

    private HistorialUbicacion mapearFila(ResultSet rs) throws SQLException {
        Ubicacion ubicacion = new Ubicacion(
                rs.getInt("id_ubicacion"), rs.getString("edificio"), rs.getString("aula"));

        HistorialUbicacion historial = new HistorialUbicacion();
        historial.setIdHistorial(rs.getInt("id_historial"));
        historial.setIdEquipo(rs.getInt("id_equipo"));
        historial.setUbicacion(ubicacion);
        historial.setFechaCambio(rs.getTimestamp("fecha_cambio").toLocalDateTime());

        // Solo viene en el historial completo (listarTodo), no en el de un solo equipo.
        if (hayColumna(rs, "nombre_equipo")) {
            historial.setNombreEquipo(rs.getString("nombre_equipo"));
        }
        return historial;
    }

    private boolean hayColumna(ResultSet rs, String nombreColumna) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            if (meta.getColumnLabel(i).equalsIgnoreCase(nombreColumna)) {
                return true;
            }
        }
        return false;
    }
}
