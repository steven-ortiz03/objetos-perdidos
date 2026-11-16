package objetosperdidos.view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import objetosperdidos.controller.AuthController;
import objetosperdidos.util.Tema;

/** Ventana de inicio de sesión (CU-01). */
public class FrmLogin extends JFrame {

    private final AuthController authController = new AuthController();

    private final JTextField txtCorreo = new JTextField(22);
    private final JPasswordField txtPassword = new JPasswordField(22);

    public FrmLogin() {
        setTitle("Objetos Perdidos y Encontrados - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 560);
        setLocationRelativeTo(null); // centra la ventana
        setResizable(false);
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(Tema.AZUL);

        // Tarjeta blanca con el formulario
        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Tema.BLANCO);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 0, 4, 0);

        JLabel lblTitulo = Tema.titulo("Objetos Perdidos");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 0;
        tarjeta.add(lblTitulo, c);

        JLabel lblSubtitulo = new JLabel("Catálogo institucional de objetos encontrados", SwingConstants.CENTER);
        lblSubtitulo.setFont(Tema.FUENTE_PEQUENA);
        lblSubtitulo.setForeground(Tema.GRIS);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 22, 0);
        tarjeta.add(lblSubtitulo, c);

        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = 2;
        tarjeta.add(new JLabel("Correo"), c);
        c.gridy = 3;
        txtCorreo.setPreferredSize(new Dimension(280, 36));
        tarjeta.add(txtCorreo, c);

        c.gridy = 4;
        tarjeta.add(new JLabel("Contraseña"), c);
        c.gridy = 5;
        txtPassword.setPreferredSize(new Dimension(280, 36));
        tarjeta.add(txtPassword, c);

        JButton btnIngresar = Tema.botonPrimario("Ingresar");
        c.gridy = 6;
        c.insets = new Insets(18, 0, 6, 0);
        tarjeta.add(btnIngresar, c);

        // Botón con aspecto de enlace para el registro de estudiantes
        JButton btnRegistrarme = new JButton("¿Eres estudiante y no tienes cuenta? Regístrate");
        btnRegistrarme.setFont(Tema.FUENTE_PEQUENA);
        btnRegistrarme.setForeground(Tema.TURQUESA.darker());
        btnRegistrarme.setBorderPainted(false);
        btnRegistrarme.setContentAreaFilled(false);
        btnRegistrarme.setFocusPainted(false);
        btnRegistrarme.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.gridy = 7;
        c.insets = new Insets(4, 0, 0, 0);
        tarjeta.add(btnRegistrarme, c);

        fondo.add(tarjeta);
        setContentPane(fondo);

        // Acciones de los botones
        btnIngresar.addActionListener(e -> ingresar());
        btnRegistrarme.addActionListener(e -> new DlgRegistroEstudiante(this).setVisible(true));
        getRootPane().setDefaultButton(btnIngresar); // la tecla Enter también ingresa
    }

    private void ingresar() {
        try {
            String password = new String(txtPassword.getPassword());
            authController.iniciarSesion(txtCorreo.getText(), password);
            new FrmPrincipal().setVisible(true);
            dispose(); // cierra la ventana de inicio de sesión
        } catch (Exception error) {
            txtPassword.setText("");
            Mensajes.error(this, error);
        }
    }
}
