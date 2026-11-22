package objetosperdidos.util;

/** Validaciones de texto reutilizables en los controladores. */
public class Validador {

    public static boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /** Formato básico de correo: algo@algo.algo */
    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

    /** Documento de identidad: entre 5 y 20 letras, números o guiones. */
    public static boolean esDocumentoValido(String documento) {
        return documento != null && documento.matches("^[0-9A-Za-z-]{5,20}$");
    }

    /** Cédula de ciudadanía: solo números, entre 6 y 10 dígitos. */
    public static boolean esCedulaValida(String cedula) {
        return cedula != null && cedula.matches("^[0-9]{6,10}$");
    }

    /** Lanza un error si el campo obligatorio está vacío. */
    public static void exigir(String valor, String nombreCampo) throws ValidacionException {
        if (estaVacio(valor)) {
            throw new ValidacionException("El campo \"" + nombreCampo + "\" es obligatorio.");
        }
    }

    /** Lanza un error si el texto supera la longitud permitida por la base de datos. */
    public static void exigirMaximo(String valor, int maximo, String nombreCampo) throws ValidacionException {
        if (valor != null && valor.length() > maximo) {
            throw new ValidacionException("El campo \"" + nombreCampo + "\" admite máximo "
                    + maximo + " caracteres.");
        }
    }
}
