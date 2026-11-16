package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import objetosperdidos.controller.AuthController;
import objetosperdidos.util.Tema;

/** Formulario para que un estudiante cree su cuenta (CU-02). */
public class DlgRegistroEstudiante extends JDialog {

    private final AuthController authController = new AuthController();

    private final JTextField txtNombre = new JTextField(24);
    private final JTextField txtCodigo = new JTextField(24);
    private final JTextField txtCorreo = new JTextField(24);
    private final JPasswordField txtPassword = new JPasswordField(24);
    private final JPasswordField txtConfirmacion = new JPasswordField(24);

    public DlgRegistroEstudiante(Frame padre) {
        super(padre, "Registro de estudiante", true); // true = ventana modal
        construirInterfaz();
        pack();
        setLocationRelativeTo(padre);
        setResizable(false);
    }

    private void construirInterfaz() {
        JPanel contenido = new JPanel(new BorderLayout(0, 15));
        contenido.setBackground(Tema.BLANCO);
        contenido.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        contenido.add(Tema.titulo("Crear cuenta de estudiante"), BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        agregarFila(formulario, 0, "Nombre completo *", txtNombre);
        agregarFila(formulario, 1, "Código institucional *", txtCodigo);
        agregarFila(formulario, 2, "Correo *", txtCorreo);
        agregarFila(formulario, 3, "Contraseña * (mínimo 6)", txtPassword);
        agregarFila(formulario, 4, "Confirmar contraseña *", txtConfirmacion);
        contenido.add(formulario, BorderLayout.CENTER);

        JButton btnCancelar = Tema.botonSecundario("Cancelar");
        JButton btnGuardar = Tema.botonPrimario("Crear cuenta");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        contenido.add(botones, BorderLayout.SOUTH);

        setContentPane(contenido);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
        getRootPane().setDefaultButton(btnGuardar);
    }

    /** Agrega una fila "etiqueta + campo" al formulario. */
    private void agregarFila(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila;
        c.insets = new Insets(6, 0, 6, 12);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.insets = new Insets(6, 0, 6, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 6;
        panel.add(campo, c);
    }

    private void guardar() {
        try {
            authController.registrarEstudiante(
                    txtNombre.getText(),
                    txtCodigo.getText(),
                    txtCorreo.getText(),
                    new String(txtPassword.getPassword()),
                    new String(txtConfirmacion.getPassword()));
            Mensajes.informacion(this, "Cuenta creada correctamente. Ya puede iniciar sesión.");
            dispose();
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }
}
