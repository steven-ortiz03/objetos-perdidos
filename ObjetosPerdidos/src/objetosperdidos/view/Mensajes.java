package objetosperdidos.view;

import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import objetosperdidos.util.ValidacionException;

/** Ventanas de mensaje reutilizables, para que todas se vean y se redacten igual. */
public class Mensajes {

    public static void informacion(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void advertencia(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    /** Pregunta Sí / No. Devuelve true si el usuario eligió Sí. */
    public static boolean confirmar(Component padre, String mensaje) {
        Object[] opciones = {"Sí", "No"};
        int respuesta = JOptionPane.showOptionDialog(padre, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[1]);
        return respuesta == 0;
    }

    /**
     * Muestra el mensaje adecuado según el tipo de error:
     * - ValidacionException: algo que el usuario puede corregir.
     * - SQLException: problema con la base de datos (RNF-08).
     * - Cualquier otro: error inesperado.
     */
    public static void error(Component padre, Exception error) {
        if (error instanceof ValidacionException) {
            advertencia(padre, error.getMessage());
        } else if (error instanceof SQLException) {
            JOptionPane.showMessageDialog(padre,
                    "No fue posible acceder a la base de datos local (data/objetos_perdidos.db).\n"
                    + "Verifique que la carpeta \"data\" tenga permisos de escritura, que el archivo no\n"
                    + "esté abierto en otro programa y que la librería sqlite-jdbc esté en Libraries.\n\n"
                    + "Detalle técnico: " + error.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(padre,
                    "Ocurrió un error inesperado: " + error.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
