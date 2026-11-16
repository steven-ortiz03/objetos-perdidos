package objetosperdidos.view;

/**
 * Interfaz que implementan los paneles de la ventana principal.
 * Cada vez que el usuario abre un panel desde el menú, la ventana principal
 * llama a recargar() para que el panel muestre la información más reciente.
 */
public interface Recargable {

    void recargar();
}
