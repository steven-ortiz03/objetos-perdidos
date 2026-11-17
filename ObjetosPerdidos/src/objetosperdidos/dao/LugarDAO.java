package objetosperdidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import objetosperdidos.database.ConexionBD;
import objetosperdidos.model.Lugar;

/** Acceso a la tabla "lugares". */
public class LugarDAO {

    public List<Lugar> listar() throws SQLException {
        String sql = "SELECT id_lugar, nombre FROM lugares ORDER BY nombre";
        List<Lugar> lugares = new ArrayList<>();
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                lugares.add(new Lugar(resultado.getInt("id_lugar"), resultado.getString("nombre")));
            }
        }
        return lugares;
    }
}
