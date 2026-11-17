package objetosperdidos.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import objetosperdidos.dao.CategoriaDAO;
import objetosperdidos.dao.LugarDAO;
import objetosperdidos.dao.ObjetoDAO;
import objetosperdidos.model.Categoria;
import objetosperdidos.model.EstadoObjeto;
import objetosperdidos.model.FiltroObjeto;
import objetosperdidos.model.Lugar;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.ImagenUtil;
import objetosperdidos.util.Sesion;
import objetosperdidos.util.ValidacionException;
import objetosperdidos.util.Validador;

/**
 * Lógica de los objetos: consulta del catálogo, registro, edición y eliminación.
 * Antes de modificar datos siempre verifica el rol del usuario (RNF-03).
 */
public class ObjetoController {

    private final ObjetoDAO objetoDAO = new ObjetoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final LugarDAO lugarDAO = new LugarDAO();

    // ---------- Consultas (administrador y estudiante) ----------

    /** CU-06 y CU-07: consulta el catálogo con los filtros indicados. */
    public List<ObjetoPerdido> buscar(FiltroObjeto filtro) throws ValidacionException, SQLException {
        Sesion.exigirSesion();
        if (filtro.getDesde() != null && filtro.getHasta() != null
                && filtro.getDesde().isAfter(filtro.getHasta())) {
            throw new ValidacionException("La fecha inicial no puede ser posterior a la fecha final.");
        }
        return objetoDAO.buscar(filtro);
    }

    public List<Categoria> listarCategorias() throws SQLException {
        return categoriaDAO.listar();
    }

    public List<Lugar> listarLugares() throws SQLException {
        return lugarDAO.listar();
    }

    public int contar(EstadoObjeto estado) throws SQLException {
        return objetoDAO.contarPorEstado(estado);
    }

    // ---------- Operaciones exclusivas del administrador ----------

    /** Revisa los campos obligatorios de un objeto (RF-20). */
    public void validar(ObjetoPerdido objeto) throws ValidacionException {
        Validador.exigir(objeto.getNombre(), "Nombre");
        Validador.exigir(objeto.getDescripcion(), "Descripción");
        Validador.exigirMaximo(objeto.getNombre(), 100, "Nombre");
        Validador.exigirMaximo(objeto.getDescripcion(), 500, "Descripción");
        if (objeto.getCategoria() == null) {
            throw new ValidacionException("Seleccione una categoría.");
        }
        if (objeto.getLugarEncontrado() == null) {
            throw new ValidacionException("Seleccione el lugar donde se encontró.");
        }
        if (objeto.getLugarResguardo() == null) {
            throw new ValidacionException("Seleccione el lugar de resguardo.");
        }
        if (objeto.getFechaEncontrado() == null) {
            throw new ValidacionException("Indique la fecha en que se encontró.");
        }
        if (objeto.getFechaEncontrado().isAfter(LocalDate.now())) {
            throw new ValidacionException("La fecha encontrada no puede ser una fecha futura.");
        }
    }

    /** Indica si ya hay otro objeto con el mismo nombre, lugar y fecha. */
    public boolean existePosibleDuplicado(ObjetoPerdido objeto) throws SQLException {
        return objetoDAO.existeDuplicado(objeto, objeto.getId());
    }

    /** CU-03: registra un objeto nuevo. La fotografía es obligatoria (la base de datos la exige). */
    public void registrar(ObjetoPerdido objeto, File foto) throws ValidacionException, SQLException {
        Sesion.exigirAdministrador();
        validar(objeto);
        if (foto == null) {
            throw new ValidacionException("Seleccione la fotografía del objeto.");
        }

        String rutaFoto = copiarFoto(foto);
        objeto.setRutaFoto(rutaFoto);
        objeto.setIdRegistradoPor(Sesion.getUsuario().getId());
        try {
            objetoDAO.insertar(objeto);
        } catch (SQLException e) {
            // Si no se pudo guardar, se borra la foto copiada para no dejar archivos sueltos
            ImagenUtil.eliminar(rutaFoto);
            throw e;
        }
    }

    /** CU-04: actualiza un objeto. Si nuevaFoto es null se conserva la foto anterior. */
    public void actualizar(ObjetoPerdido objeto, File nuevaFoto) throws ValidacionException, SQLException {
        Sesion.exigirAdministrador();
        validar(objeto);

        String rutaAnterior = objeto.getRutaFoto();
        String rutaNueva = copiarFoto(nuevaFoto);
        if (rutaNueva != null) {
            objeto.setRutaFoto(rutaNueva);
        }
        try {
            objetoDAO.actualizar(objeto);
        } catch (SQLException e) {
            ImagenUtil.eliminar(rutaNueva);
            objeto.setRutaFoto(rutaAnterior);
            throw e;
        }
        // Si se cambió la foto, la anterior ya no se necesita
        if (rutaNueva != null) {
            ImagenUtil.eliminar(rutaAnterior);
        }
    }

    /** CU-05: elimina un objeto disponible y su fotografía (RF-10). */
    public void eliminar(ObjetoPerdido objeto) throws ValidacionException, SQLException {
        Sesion.exigirAdministrador();
        if (!objeto.estaDisponible()) {
            throw new ValidacionException(
                    "Este objeto ya fue entregado y debe conservarse en el historial. No se puede eliminar.");
        }
        boolean eliminado = objetoDAO.eliminar(objeto.getId());
        if (!eliminado) {
            throw new ValidacionException("El objeto no se pudo eliminar porque ya no está disponible.");
        }
        ImagenUtil.eliminar(objeto.getRutaFoto());
    }

    /** Valida y copia la foto a la carpeta de imágenes. Devuelve null si no hay foto. */
    private String copiarFoto(File foto) throws ValidacionException {
        if (foto == null) {
            return null;
        }
        ImagenUtil.validar(foto);
        try {
            return ImagenUtil.guardar(foto);
        } catch (IOException e) {
            throw new ValidacionException("No se pudo guardar la fotografía en la carpeta de imágenes.");
        }
    }
}
