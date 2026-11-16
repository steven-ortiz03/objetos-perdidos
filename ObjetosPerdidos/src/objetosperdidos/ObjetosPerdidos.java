package objetosperdidos;

import javax.swing.SwingUtilities;
import objetosperdidos.util.Tema;
import objetosperdidos.view.FrmLogin;

/**
 * Punto de entrada del Sistema de Objetos Perdidos y Encontrados.
 * La base de datos SQLite se crea sola la primera vez (ver ConexionBD).
 */
public class ObjetosPerdidos {

    public static void main(String[] args) {
        Tema.aplicarLookAndFeel();
        // Las ventanas de Swing deben crearse en el hilo de la interfaz gráfica
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}
