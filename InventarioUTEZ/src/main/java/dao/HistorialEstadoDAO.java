package dao;

import modelo.EstadoFisico;
import modelo.HistorialEstado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialEstadoDAO {

    public void registrarCambio(int idEquipo, int idEstado, Connection con) throws SQLException {
        // La conexion viene de afuera para que EquipoDAO haga el UPDATE y este
        // INSERT en una sola transaccion.
        String sql = "INSERT INTO historial_estados (id_equipo, id_estado) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setInt(2, idEstado);
            ps.executeUpdate();
        }
    }

    public List<HistorialEstado> listarTodo() throws SQLException {
        List<HistorialEstado> lista = new ArrayList<>();
        String sql = "SELECT h.id_historial, h.id_equipo, h.fecha_cambio, "
                   + "       est.id_estado, est.nombre AS nombre_estado, "
                   + "       eq.nombre AS nombre_equipo "
                   + "FROM historial_estados h "
                   + "JOIN estados_fisicos est ON h.id_estado = est.id_estado "
                   + "JOIN equipos eq          ON h.id_equipo = eq.id_equipo "
                   + "ORDER BY h.fecha_cambio DESC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoFisico estado = new EstadoFisico(rs.getInt("id_estado"), rs.getString("nombre_estado"));

                HistorialEstado historial = new HistorialEstado();
                historial.setIdHistorial(rs.getInt("id_historial"));
                historial.setIdEquipo(rs.getInt("id_equipo"));
                historial.setNombreEquipo(rs.getString("nombre_equipo"));
                historial.setEstado(estado);
                historial.setFechaCambio(rs.getTimestamp("fecha_cambio").toLocalDateTime());

                lista.add(historial);
            }
        }
        return lista;
    }
}
