package dao;

import modelo.EstadoFisico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoFisicoDAO {

    public List<EstadoFisico> listarTodos() throws SQLException {
        List<EstadoFisico> lista = new ArrayList<>();
        String sql = "SELECT id_estado, nombre FROM estados_fisicos ORDER BY id_estado";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new EstadoFisico(rs.getInt("id_estado"), rs.getString("nombre")));
            }
        }
        return lista;
    }
}
