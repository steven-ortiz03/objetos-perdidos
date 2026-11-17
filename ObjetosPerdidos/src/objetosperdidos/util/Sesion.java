package objetosperdidos.util;

import objetosperdidos.model.Usuario;

/**
 * Guarda el usuario que inició sesión mientras la aplicación está abierta.
 * Los controladores la consultan para verificar permisos (RNF-03).
 */
public class Sesion {

    private static Usuario usuarioActual;

    public static void iniciar(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static void cerrar() {
        usuarioActual = null;
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }

    /** Verifica que exista una sesión activa. */
    public static void exigirSesion() throws ValidacionException {
        if (usuarioActual == null) {
            throw new ValidacionException("Debe iniciar sesión para realizar esta acción.");
        }
    }

    /**
     * Verifica que el usuario actual pueda gestionar objetos.
     * Aquí se usa el polimorfismo: cada tipo de usuario responde distinto
     * a puedeGestionarObjetos().
     */
    public static void exigirAdministrador() throws ValidacionException {
        exigirSesion();
        if (!usuarioActual.puedeGestionarObjetos()) {
            throw new ValidacionException("Su rol no tiene permiso para realizar esta acción.");
        }
    }
}
