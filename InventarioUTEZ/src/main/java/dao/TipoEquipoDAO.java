package dao;

import modelo.TipoEquipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoEquipoDAO {

    public List<TipoEquipo> listarTodos() throws SQLException {
        List<TipoEquipo> lista = new ArrayList<>();
        String sql = "SELECT id_tipo, nombre FROM tipos_equipo ORDER BY nombre";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new TipoEquipo(rs.getInt("id_tipo"), rs.getString("nombre")));
            }
        }
        return lista;
    }
}
