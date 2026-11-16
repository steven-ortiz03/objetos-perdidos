package objetosperdidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import objetosperdidos.database.ConexionBD;
import objetosperdidos.model.Administrador;
import objetosperdidos.model.Estudiante;
import objetosperdidos.model.Rol;
import objetosperdidos.model.Usuario;

/** Acceso a la tabla "usuarios". */
public class UsuarioDAO {

    /** Busca un usuario por su correo. Devuelve null si no existe. */
    public Usuario buscarPorCorreo(String correo) throws SQLException {
        String sql = "SELECT id_usuario, nombre, correo, codigo_institucional, password_hash, salt, rol, activo "
                + "FROM usuarios WHERE correo = ?";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, correo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertir(resultado);
                }
                return null;
            }
        }
    }

    /** Indica si ya existe una cuenta con ese código institucional (es único). */
    public boolean existeCodigo(String codigo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE LOWER(codigo_institucional) = LOWER(?)";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, codigo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                resultado.next();
                return resultado.getInt(1) > 0;
            }
        }
    }

    /** Guarda un usuario nuevo. El rol sale del tipo de objeto recibido. */
    public void insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, codigo_institucional, password_hash, salt, rol) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getCorreo());
            sentencia.setString(3, usuario.getCodigoInstitucional());
            sentencia.setString(4, usuario.getPasswordHash());
            sentencia.setString(5, usuario.getSalt());
            sentencia.setString(6, usuario.getRol().name());
            sentencia.executeUpdate();
        }
    }

    /** Crea un Administrador o un Estudiante según el rol guardado en la fila. */
    private Usuario convertir(ResultSet fila) throws SQLException {
        Rol rol = Rol.valueOf(fila.getString("rol"));
        Usuario usuario;
        if (rol == Rol.ADMINISTRADOR) {
            usuario = new Administrador();
        } else {
            usuario = new Estudiante();
        }
        usuario.setId(fila.getInt("id_usuario"));
        usuario.setNombre(fila.getString("nombre"));
        usuario.setCorreo(fila.getString("correo"));
        usuario.setCodigoInstitucional(fila.getString("codigo_institucional"));
        usuario.setPasswordHash(fila.getString("password_hash"));
        usuario.setSalt(fila.getString("salt"));
        usuario.setActivo(fila.getInt("activo") == 1);
        return usuario;
    }
}
