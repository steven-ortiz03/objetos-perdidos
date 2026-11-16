package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import objetosperdidos.controller.AuthController;
import objetosperdidos.model.Usuario;
import objetosperdidos.util.Sesion;
import objetosperdidos.util.Tema;

/**
 * Ventana principal: menú lateral a la izquierda y, en el centro,
 * un panel que cambia según la opción elegida (CardLayout).
 * Las opciones del menú dependen del rol del usuario (RF-04).
 */
public class FrmPrincipal extends JFrame {

    private final AuthController authController = new AuthController();

    private final CardLayout tarjetas = new CardLayout();
    private final JPanel pnlCentro = new JPanel(tarjetas);
    private final JPanel pnlMenu = new JPanel();

    // Relaciona el nombre de cada opción con su panel y con su botón del menú
    private final Map<String, Recargable> paneles = new HashMap<>();
    private final List<JButton> botonesMenu = new ArrayList<>();

    public FrmPrincipal() {
        setTitle("Sistema de Objetos Perdidos y Encontrados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setMinimumSize(new Dimension(980, 600));
        setLocationRelativeTo(null);
        construirInterfaz();
        mostrar("Inicio");
    }

    private void construirInterfaz() {
        Usuario usuario = Sesion.getUsuario();

        // ----- Menú lateral -----
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
        pnlMenu.setBackground(Tema.AZUL);
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(22, 14, 18, 14));
        pnlMenu.setPreferredSize(new Dimension(230, 0));

        JLabel lblApp = new JLabel("Objetos Perdidos");
        lblApp.setFont(Tema.FUENTE_TITULO.deriveFont(19f));
        lblApp.setForeground(Tema.BLANCO);
        lblApp.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlMenu.add(lblApp);

        JLabel lblUsuario = new JLabel(usuario.getNombre());
        lblUsuario.setForeground(Tema.BLANCO);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblRol = new JLabel(usuario.getNombreRol());
        lblRol.setFont(Tema.FUENTE_PEQUENA);
        lblRol.setForeground(Tema.TURQUESA);
        lblRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlMenu.add(Box.createVerticalStrut(14));
        pnlMenu.add(lblUsuario);
        pnlMenu.add(lblRol);
        pnlMenu.add(Box.createVerticalStrut(24));

        // ----- Paneles disponibles para todos -----
        agregarOpcion("Inicio", new PnlInicio());
        agregarOpcion("Catálogo", new PnlCatalogo());

        // ----- Paneles solo para quien puede gestionar objetos -----
        if (usuario.puedeGestionarObjetos()) {
            agregarOpcion("Gestión de objetos", new PnlGestionObjetos());
            agregarOpcion("Historial de entregas", new PnlHistorial());
        }

        pnlMenu.add(Box.createVerticalGlue()); // empuja "Cerrar sesión" hacia abajo
        JButton btnSalir = crearBotonMenu("Cerrar sesión");
        btnSalir.addActionListener(e -> cerrarSesion());
        pnlMenu.add(btnSalir);

        pnlCentro.setBackground(Tema.FONDO);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pnlMenu, BorderLayout.WEST);
        getContentPane().add(pnlCentro, BorderLayout.CENTER);
    }

    /** Agrega un panel al centro y su botón correspondiente al menú. */
    private void agregarOpcion(String nombre, JPanel panel) {
        paneles.put(nombre, (Recargable) panel);
        pnlCentro.add(panel, nombre);

        JButton boton = crearBotonMenu(nombre);
        boton.addActionListener(e -> mostrar(nombre));
        botonesMenu.add(boton);
        pnlMenu.add(boton);
        pnlMenu.add(Box.createVerticalStrut(6));
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = Tema.botonMenu(texto);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        return boton;
    }

    /** Muestra el panel elegido, actualiza sus datos y resalta su botón. */
    private void mostrar(String nombre) {
        tarjetas.show(pnlCentro, nombre);
        paneles.get(nombre).recargar();
        for (JButton boton : botonesMenu) {
            boolean seleccionado = boton.getText().equals(nombre);
            boton.setBackground(seleccionado ? Tema.AZUL_CLARO : Tema.AZUL);
            boton.setFont(seleccionado ? Tema.FUENTE_NEGRITA : Tema.FUENTE);
        }
    }

    private void cerrarSesion() {
        if (Mensajes.confirmar(this, "¿Desea cerrar la sesión?")) {
            authController.cerrarSesion();
            new FrmLogin().setVisible(true);
            dispose();
        }
    }
}
