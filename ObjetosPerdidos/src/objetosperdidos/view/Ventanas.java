package objetosperdidos.view;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Ayuda para abrir una ventana (JFrame) encima de otra. */
public class Ventanas {

    /**
     * Abre "hija" centrada sobre la ventana donde está "origen" y bloquea la de atrás
     * hasta que la nueva se cierre. Al cerrarse ejecuta "alCerrar" (puede ser null),
     * por ejemplo para recargar una tabla.
     */
    public static void abrirSobre(Component origen, JFrame hija, Runnable alCerrar) {
        Window padre = (origen instanceof Window) ? (Window) origen : SwingUtilities.getWindowAncestor(origen);
        if (padre != null) {
            padre.setEnabled(false);
        }
        hija.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (padre != null) {
                    padre.setEnabled(true);
                    padre.toFront();
                }
                if (alCerrar != null) {
                    alCerrar.run();
                }
            }
        });
        hija.setLocationRelativeTo(padre);
        hija.setVisible(true);
    }
}
