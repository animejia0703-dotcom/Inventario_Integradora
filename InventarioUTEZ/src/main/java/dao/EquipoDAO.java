package dao;

import modelo.Equipo;
import modelo.EstadoFisico;
import modelo.TipoEquipo;
import modelo.Ubicacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    private final HistorialUbicacionDAO historialUbicacionDAO = new HistorialUbicacionDAO();
    private final HistorialEstadoDAO historialEstadoDAO = new HistorialEstadoDAO();

    public void registrar(Equipo equipo) throws SQLException {
        String sql = "INSERT INTO equipos (nombre, id_tipo, marca, modelo, numero_serie, "
                   + "id_estado, notas, id_ubicacion, responsable) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, equipo.getNombre());
            ps.setInt(2, equipo.getTipo().getIdTipo());
            ps.setString(3, equipo.getMarca());
            ps.setString(4, equipo.getModelo());
            ps.setString(5, equipo.getNumeroSerie());
            ps.setInt(6, equipo.getEstado().getIdEstado());
            ps.setString(7, equipo.getNotas());
            ps.setInt(8, equipo.getUbicacion().getIdUbicacion());
            ps.setString(9, equipo.getResponsable());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int idEquipo = keys.getInt(1);
                    historialUbicacionDAO.registrarMovimiento(idEquipo, equipo.getUbicacion().getIdUbicacion(), con);
                    historialEstadoDAO.registrarCambio(idEquipo, equipo.getEstado().getIdEstado(), con);
                }
            }
        }
    }

    public void actualizarDatos(Equipo equipo) throws SQLException {
        // No toca la ubicacion; para eso esta cambiarUbicacion().
        String selectEstado = "SELECT id_estado FROM equipos WHERE id_equipo = ?";
        String update = "UPDATE equipos SET nombre = ?, id_tipo = ?, marca = ?, modelo = ?, "
                       + "numero_serie = ?, id_estado = ?, notas = ?, responsable = ? "
                       + "WHERE id_equipo = ?";

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            int estadoAnterior;
            try (PreparedStatement ps = con.prepareStatement(selectEstado)) {
                ps.setInt(1, equipo.getIdEquipo());
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    estadoAnterior = rs.getInt("id_estado");
                }
            }

            try (PreparedStatement ps = con.prepareStatement(update)) {
                ps.setString(1, equipo.getNombre());
                ps.setInt(2, equipo.getTipo().getIdTipo());
                ps.setString(3, equipo.getMarca());
                ps.setString(4, equipo.getModelo());
                ps.setString(5, equipo.getNumeroSerie());
                ps.setInt(6, equipo.getEstado().getIdEstado());
                ps.setString(7, equipo.getNotas());
                ps.setString(8, equipo.getResponsable());
                ps.setInt(9, equipo.getIdEquipo());
                ps.executeUpdate();
            }

            int estadoNuevo = equipo.getEstado().getIdEstado();
            if (estadoNuevo != estadoAnterior) {
                historialEstadoDAO.registrarCambio(equipo.getIdEquipo(), estadoNuevo, con);
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) con.setAutoCommit(true);
        }
    }

    public void cambiarUbicacion(int idEquipo, int idNuevaUbicacion) throws SQLException {
        String sql = "UPDATE equipos SET id_ubicacion = ? WHERE id_equipo = ?";

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false); // Las dos operaciones deben ocurrir juntas o ninguna.

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idNuevaUbicacion);
                ps.setInt(2, idEquipo);
                ps.executeUpdate();
            }

            historialUbicacionDAO.registrarMovimiento(idEquipo, idNuevaUbicacion, con);

            con.commit();
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) con.setAutoCommit(true);
        }
    }

    public void eliminar(int idEquipo) throws SQLException {
        String sql = "UPDATE equipos SET eliminado = 1, fecha_baja = NOW() WHERE id_equipo = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEquipo);
            ps.executeUpdate();
        }
    }

    private static final String SELECT_EQUIPOS =
              "SELECT e.id_equipo, e.nombre, e.marca, e.modelo, e.numero_serie, "
            + "       e.notas, e.responsable, e.fecha_registro, e.fecha_baja, "
            + "       t.id_tipo, t.nombre AS nombre_tipo, "
            + "       est.id_estado, est.nombre AS nombre_estado, "
            + "       u.id_ubicacion, u.edificio, u.aula "
            + "FROM equipos e "
            + "JOIN tipos_equipo t   ON e.id_tipo = t.id_tipo "
            + "JOIN estados_fisicos est ON e.id_estado = est.id_estado "
            + "JOIN ubicaciones u    ON e.id_ubicacion = u.id_ubicacion ";

    public List<Equipo> listarTodos() throws SQLException {
        return listar(SELECT_EQUIPOS + "WHERE e.eliminado = 0 ORDER BY e.nombre", null);
    }

    public List<Equipo> listarPorUbicacion(int idUbicacion) throws SQLException {
        return listar(SELECT_EQUIPOS + "WHERE e.eliminado = 0 AND u.id_ubicacion = ? ORDER BY e.nombre", idUbicacion);
    }

    public List<Equipo> listarEliminados() throws SQLException {
        return listar(SELECT_EQUIPOS + "WHERE e.eliminado = 1 ORDER BY e.fecha_baja DESC", null);
    }

    private List<Equipo> listar(String sql, Integer idUbicacion) throws SQLException {
        List<Equipo> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (idUbicacion != null) {
                ps.setInt(1, idUbicacion);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearFila(rs));
                }
            }
        }
        return lista;
    }

    private Equipo mapearFila(ResultSet rs) throws SQLException {
        TipoEquipo tipo = new TipoEquipo(rs.getInt("id_tipo"), rs.getString("nombre_tipo"));
        EstadoFisico estado = new EstadoFisico(rs.getInt("id_estado"), rs.getString("nombre_estado"));
        Ubicacion ubicacion = new Ubicacion(rs.getInt("id_ubicacion"), rs.getString("edificio"), rs.getString("aula"));

        Equipo equipo = new Equipo();
        equipo.setIdEquipo(rs.getInt("id_equipo"));
        equipo.setNombre(rs.getString("nombre"));
        equipo.setTipo(tipo);
        equipo.setMarca(rs.getString("marca"));
        equipo.setModelo(rs.getString("modelo"));
        equipo.setNumeroSerie(rs.getString("numero_serie"));
        equipo.setEstado(estado);
        equipo.setNotas(rs.getString("notas"));
        equipo.setUbicacion(ubicacion);
        equipo.setResponsable(rs.getString("responsable"));

        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) {
            equipo.setFechaRegistro(ts.toLocalDateTime());
        }
        Timestamp tsBaja = rs.getTimestamp("fecha_baja");
        if (tsBaja != null) {
            equipo.setFechaBaja(tsBaja.toLocalDateTime());
        }
        return equipo;
    }
}
