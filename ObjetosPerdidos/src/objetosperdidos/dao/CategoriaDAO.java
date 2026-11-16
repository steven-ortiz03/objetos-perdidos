package objetosperdidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import objetosperdidos.database.ConexionBD;
import objetosperdidos.model.Categoria;

/** Acceso a la tabla "categorias". */
public class CategoriaDAO {

    public List<Categoria> listar() throws SQLException {
        String sql = "SELECT id_categoria, nombre FROM categorias ORDER BY nombre";
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                categorias.add(new Categoria(resultado.getInt("id_categoria"), resultado.getString("nombre")));
            }
        }
        return categorias;
    }
}
