package objetosperdidos.util;

/**
 * Excepción propia para errores que el usuario puede corregir:
 * campos vacíos, datos inválidos o acciones no permitidas.
 * Su mensaje se muestra tal cual en pantalla.
 */
public class ValidacionException extends Exception {

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
