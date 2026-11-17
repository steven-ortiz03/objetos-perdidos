package objetosperdidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import objetosperdidos.database.ConexionBD;
import objetosperdidos.model.Entrega;

/** Acceso a la tabla "entregas". */
public class EntregaDAO {

    /**
     * Registra la entrega y cambia el estado del objeto a ENTREGADO (RF-11, RF-12).
     * Las dos operaciones van en una transacción: se hacen las dos o ninguna.
     * Devuelve false si el objeto ya no estaba disponible.
     */
    public boolean registrar(Entrega entrega) throws SQLException {
        String sqlEstado = "UPDATE objetos SET estado = 'ENTREGADO' "
                + "WHERE id_objeto = ? AND estado = 'DISPONIBLE'";
        String sqlEntrega = "INSERT INTO entregas (id_objeto, nombre_receptor, documento_receptor, "
                + "fecha_entrega, observaciones, id_funcionario) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getConexion()) {
            conexion.setAutoCommit(false); // inicia la transacción
            try (PreparedStatement cambiarEstado = conexion.prepareStatement(sqlEstado);
                 PreparedStatement insertar = conexion.prepareStatement(sqlEntrega)) {

                cambiarEstado.setInt(1, entrega.getIdObjeto());
                if (cambiarEstado.executeUpdate() == 0) {
                    conexion.rollback(); // el objeto ya fue entregado o no existe
                    return false;
                }

                insertar.setInt(1, entrega.getIdObjeto());
                insertar.setString(2, entrega.getNombreReceptor());
                insertar.setString(3, entrega.getDocumentoReceptor());
                insertar.setString(4, LocalDate.now().toString()); // la fecha de entrega es la de hoy
                insertar.setString(5, entrega.getObservaciones());
                insertar.setInt(6, entrega.getIdFuncionario());
                insertar.executeUpdate();

                conexion.commit(); // confirma los dos cambios
                return true;
            } catch (SQLException e) {
                conexion.rollback(); // deshace todo si algo falló
                throw e;
            }
        }
    }

    /** Historial de entregas, de la más reciente a la más antigua (RF-13). */
    public List<Entrega> listar() throws SQLException {
        String sql = "SELECT e.id_entrega, e.id_objeto, o.nombre AS objeto, e.nombre_receptor, "
                + "e.documento_receptor, e.fecha_entrega, e.observaciones, e.id_funcionario, "
                + "u.nombre AS funcionario "
                + "FROM entregas e "
                + "JOIN objetos o ON o.id_objeto = e.id_objeto "
                + "JOIN usuarios u ON u.id_usuario = e.id_funcionario "
                + "ORDER BY e.fecha_entrega DESC, e.id_entrega DESC";
        List<Entrega> entregas = new ArrayList<>();
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                Entrega entrega = new Entrega();
                entrega.setId(resultado.getInt("id_entrega"));
                entrega.setIdObjeto(resultado.getInt("id_objeto"));
                entrega.setNombreObjeto(resultado.getString("objeto"));
                entrega.setNombreReceptor(resultado.getString("nombre_receptor"));
                entrega.setDocumentoReceptor(resultado.getString("documento_receptor"));
                entrega.setFechaEntrega(LocalDate.parse(resultado.getString("fecha_entrega")));
                entrega.setObservaciones(resultado.getString("observaciones"));
                entrega.setIdFuncionario(resultado.getInt("id_funcionario"));
                entrega.setNombreFuncionario(resultado.getString("funcionario"));
                entregas.add(entrega);
            }
        }
        return entregas;
    }
}
