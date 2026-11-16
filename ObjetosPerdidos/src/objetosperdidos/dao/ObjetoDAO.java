package objetosperdidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import objetosperdidos.database.ConexionBD;
import objetosperdidos.model.Categoria;
import objetosperdidos.model.EstadoObjeto;
import objetosperdidos.model.FiltroObjeto;
import objetosperdidos.model.Lugar;
import objetosperdidos.model.ObjetoPerdido;

/** Acceso a la tabla "objetos". Todo el SQL usa parámetros (RNF-04). */
public class ObjetoDAO {

    /**
     * Consulta base: trae el objeto junto con los nombres de su categoría,
     * sus dos lugares y la fecha de entrega (si ya fue entregado).
     */
    private static final String CONSULTA_BASE =
            "SELECT o.id_objeto, o.nombre, o.descripcion, o.ruta_foto, o.fecha_encontrado, o.estado, "
            + "o.id_registrado_por, c.id_categoria, c.nombre AS categoria, "
            + "le.id_lugar AS id_lugar_encontrado, le.nombre AS lugar_encontrado, "
            + "lr.id_lugar AS id_lugar_resguardo, lr.nombre AS lugar_resguardo, e.fecha_entrega "
            + "FROM objetos o "
            + "JOIN categorias c ON c.id_categoria = o.id_categoria "
            + "JOIN lugares le ON le.id_lugar = o.id_lugar_encontrado "
            + "JOIN lugares lr ON lr.id_lugar = o.id_lugar_resguardo "
            + "LEFT JOIN entregas e ON e.id_objeto = o.id_objeto ";

    /**
     * Busca objetos aplicando solo los filtros que tengan valor.
     * La consulta se arma agregando condiciones y sus parámetros en el mismo orden.
     */
    public List<ObjetoPerdido> buscar(FiltroObjeto filtro) throws SQLException {
        StringBuilder sql = new StringBuilder(CONSULTA_BASE).append("WHERE 1 = 1 ");
        List<Object> parametros = new ArrayList<>();

        if (filtro.getTexto() != null && !filtro.getTexto().trim().isEmpty()) {
            sql.append("AND (o.nombre LIKE ? OR o.descripcion LIKE ?) ");
            String patron = "%" + filtro.getTexto().trim() + "%";
            parametros.add(patron);
            parametros.add(patron);
        }
        if (filtro.getCategoria() != null) {
            sql.append("AND o.id_categoria = ? ");
            parametros.add(filtro.getCategoria().getId());
        }
        if (filtro.getLugarEncontrado() != null) {
            sql.append("AND o.id_lugar_encontrado = ? ");
            parametros.add(filtro.getLugarEncontrado().getId());
        }
        if (filtro.getEstado() != null) {
            sql.append("AND o.estado = ? ");
            parametros.add(filtro.getEstado().name());
        }
        if (filtro.getDesde() != null) {
            sql.append("AND o.fecha_encontrado >= ? ");
            parametros.add(filtro.getDesde().toString());
        }
        if (filtro.getHasta() != null) {
            sql.append("AND o.fecha_encontrado <= ? ");
            parametros.add(filtro.getHasta().toString());
        }
        // Los más recientes primero
        sql.append("ORDER BY o.fecha_encontrado DESC, o.id_objeto DESC");

        List<ObjetoPerdido> objetos = new ArrayList<>();
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                sentencia.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    objetos.add(convertir(resultado));
                }
            }
        }
        return objetos;
    }

    /** Busca un objeto por su id. Devuelve null si no existe. */
    public ObjetoPerdido buscarPorId(int id) throws SQLException {
        String sql = CONSULTA_BASE + "WHERE o.id_objeto = ?";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertir(resultado);
                }
                return null;
            }
        }
    }

    /** Guarda un objeto nuevo y le asigna el id que generó la base de datos (RF-07). */
    public void insertar(ObjetoPerdido objeto) throws SQLException {
        String sql = "INSERT INTO objetos (nombre, descripcion, ruta_foto, fecha_encontrado, estado, "
                + "id_categoria, id_lugar_encontrado, id_lugar_resguardo, id_registrado_por) "
                + "VALUES (?, ?, ?, ?, 'DISPONIBLE', ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, objeto.getNombre());
            sentencia.setString(2, objeto.getDescripcion());
            sentencia.setString(3, objeto.getRutaFoto());
            sentencia.setString(4, objeto.getFechaEncontrado().toString());
            sentencia.setInt(5, objeto.getCategoria().getId());
            sentencia.setInt(6, objeto.getLugarEncontrado().getId());
            sentencia.setInt(7, objeto.getLugarResguardo().getId());
            sentencia.setInt(8, objeto.getIdRegistradoPor());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    objeto.setId(claves.getInt(1));
                }
            }
        }
    }

    /** Actualiza los datos de un objeto. El estado no se cambia aquí, solo al entregar. */
    public void actualizar(ObjetoPerdido objeto) throws SQLException {
        String sql = "UPDATE objetos SET nombre = ?, descripcion = ?, ruta_foto = ?, fecha_encontrado = ?, "
                + "id_categoria = ?, id_lugar_encontrado = ?, id_lugar_resguardo = ? WHERE id_objeto = ?";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, objeto.getNombre());
            sentencia.setString(2, objeto.getDescripcion());
            sentencia.setString(3, objeto.getRutaFoto());
            sentencia.setString(4, objeto.getFechaEncontrado().toString());
            sentencia.setInt(5, objeto.getCategoria().getId());
            sentencia.setInt(6, objeto.getLugarEncontrado().getId());
            sentencia.setInt(7, objeto.getLugarResguardo().getId());
            sentencia.setInt(8, objeto.getId());
            sentencia.executeUpdate();
        }
    }

    /**
     * Elimina un objeto solo si sigue disponible (RF-10).
     * Devuelve true si se eliminó la fila.
     */
    public boolean eliminar(int idObjeto) throws SQLException {
        String sql = "DELETE FROM objetos WHERE id_objeto = ? AND estado = 'DISPONIBLE'";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idObjeto);
            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Indica si ya existe otro objeto con el mismo nombre, lugar y fecha (RF-20).
     * idExcluir es el id del objeto que se está editando (0 si es nuevo).
     */
    public boolean existeDuplicado(ObjetoPerdido objeto, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM objetos WHERE LOWER(nombre) = LOWER(?) "
                + "AND id_lugar_encontrado = ? AND fecha_encontrado = ? AND id_objeto <> ?";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, objeto.getNombre());
            sentencia.setInt(2, objeto.getLugarEncontrado().getId());
            sentencia.setString(3, objeto.getFechaEncontrado().toString());
            sentencia.setInt(4, idExcluir);
            try (ResultSet resultado = sentencia.executeQuery()) {
                resultado.next();
                return resultado.getInt(1) > 0;
            }
        }
    }

    /** Cuenta cuántos objetos hay en un estado. */
    public int contarPorEstado(EstadoObjeto estado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM objetos WHERE estado = ?";
        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, estado.name());
            try (ResultSet resultado = sentencia.executeQuery()) {
                resultado.next();
                return resultado.getInt(1);
            }
        }
    }

    /** Convierte una fila del resultado en un ObjetoPerdido. */
    private ObjetoPerdido convertir(ResultSet fila) throws SQLException {
        ObjetoPerdido objeto = new ObjetoPerdido();
        objeto.setId(fila.getInt("id_objeto"));
        objeto.setNombre(fila.getString("nombre"));
        objeto.setDescripcion(fila.getString("descripcion"));
        objeto.setRutaFoto(fila.getString("ruta_foto"));
        // SQLite guarda las fechas como texto yyyy-MM-dd
        objeto.setFechaEncontrado(LocalDate.parse(fila.getString("fecha_encontrado")));
        objeto.setEstado(EstadoObjeto.valueOf(fila.getString("estado")));
        objeto.setIdRegistradoPor(fila.getInt("id_registrado_por"));
        objeto.setCategoria(new Categoria(fila.getInt("id_categoria"), fila.getString("categoria")));
        objeto.setLugarEncontrado(new Lugar(fila.getInt("id_lugar_encontrado"), fila.getString("lugar_encontrado")));
        objeto.setLugarResguardo(new Lugar(fila.getInt("id_lugar_resguardo"), fila.getString("lugar_resguardo")));
        String entrega = fila.getString("fecha_entrega");
        if (entrega != null) {
            objeto.setFechaEntrega(LocalDate.parse(entrega));
        }
        return objeto;
    }
}
