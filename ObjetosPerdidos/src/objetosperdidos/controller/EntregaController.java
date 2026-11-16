package objetosperdidos.controller;

import java.sql.SQLException;
import java.util.List;
import objetosperdidos.dao.EntregaDAO;
import objetosperdidos.model.Entrega;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.Sesion;
import objetosperdidos.util.ValidacionException;
import objetosperdidos.util.Validador;

/** Lógica de las entregas de objetos y del historial. Solo para administradores. */
public class EntregaController {

    private final EntregaDAO entregaDAO = new EntregaDAO();

    /** CU-09: registra la entrega de un objeto disponible a su propietario. */
    public void registrarEntrega(ObjetoPerdido objeto, String nombreReceptor,
                                 String documentoReceptor, String observaciones)
            throws ValidacionException, SQLException {
        Sesion.exigirAdministrador();
        if (!objeto.estaDisponible()) {
            throw new ValidacionException("Este objeto ya fue entregado.");
        }
        Validador.exigir(nombreReceptor, "Nombre de quien recibe");
        Validador.exigir(documentoReceptor, "Documento de quien recibe");
        Validador.exigirMaximo(nombreReceptor, 100, "Nombre de quien recibe");
        Validador.exigirMaximo(observaciones, 300, "Observaciones");
        if (!Validador.esDocumentoValido(documentoReceptor.trim())) {
            throw new ValidacionException(
                    "El documento debe tener entre 5 y 20 caracteres (números, letras o guiones).");
        }

        Entrega entrega = new Entrega();
        entrega.setIdObjeto(objeto.getId());
        entrega.setNombreReceptor(nombreReceptor.trim());
        entrega.setDocumentoReceptor(documentoReceptor.trim());
        entrega.setObservaciones(observaciones == null ? "" : observaciones.trim());
        entrega.setIdFuncionario(Sesion.getUsuario().getId());

        boolean registrada = entregaDAO.registrar(entrega);
        if (!registrada) {
            throw new ValidacionException("El objeto ya no está disponible. Actualice la lista.");
        }
    }

    /** CU-10: consulta el historial de entregas. */
    public List<Entrega> listarHistorial() throws ValidacionException, SQLException {
        Sesion.exigirAdministrador();
        return entregaDAO.listar();
    }
}
