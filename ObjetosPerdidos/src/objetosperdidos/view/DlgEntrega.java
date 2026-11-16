package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import objetosperdidos.controller.EntregaController;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.Sesion;
import objetosperdidos.util.Tema;

/** Formulario para registrar la entrega de un objeto a su propietario (CU-09). */
public class DlgEntrega extends JDialog {

    private final EntregaController entregaController = new EntregaController();
    private final ObjetoPerdido objeto;
    private boolean guardado = false;

    private final JTextField txtNombre = new JTextField(24);
    private final JTextField txtDocumento = new JTextField(24);
    private final JTextArea txtObservaciones = new JTextArea(3, 24);

    public DlgEntrega(Window padre, ObjetoPerdido objeto) {
        super(padre, "Registrar entrega", ModalityType.APPLICATION_MODAL);
        this.objeto = objeto;
        construirInterfaz();
        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    private void construirInterfaz() {
        JPanel contenido = new JPanel(new BorderLayout(0, 14));
        contenido.setBackground(Tema.BLANCO);
        contenido.setBorder(BorderFactory.createEmptyBorder(22, 26, 20, 26));

        // ----- Resumen del objeto que se entrega -----
        JPanel resumen = new JPanel(new GridLayout(0, 1, 0, 3));
        resumen.setBackground(Tema.FONDO);
        resumen.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        JLabel lblObjeto = new JLabel("Objeto: " + objeto.getNombre() + "  (ID " + objeto.getId() + ")");
        lblObjeto.setFont(Tema.FUENTE_NEGRITA);
        resumen.add(lblObjeto);
        resumen.add(new JLabel("Resguardado en: " + objeto.getLugarResguardo().getNombre()));
        resumen.add(new JLabel("Entrega: " + Sesion.getUsuario().getNombre() + " · fecha y hora actuales"));

        JPanel norte = new JPanel(new BorderLayout(0, 12));
        norte.setOpaque(false);
        norte.add(Tema.titulo("Registrar entrega"), BorderLayout.NORTH);
        norte.add(resumen, BorderLayout.SOUTH);
        contenido.add(norte, BorderLayout.NORTH);

        // ----- Datos de quien recibe -----
        txtNombre.setPreferredSize(new java.awt.Dimension(290, 34));
        txtDocumento.setPreferredSize(new java.awt.Dimension(290, 34));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        agregarFila(formulario, 0, "Nombre de quien recibe *", txtNombre);
        agregarFila(formulario, 1, "Documento de identidad *", txtDocumento);
        agregarFila(formulario, 2, "Observaciones", new JScrollPane(txtObservaciones));
        contenido.add(formulario, BorderLayout.CENTER);

        JButton btnCancelar = Tema.botonSecundario("Cancelar");
        JButton btnConfirmar = Tema.botonPrimario("Confirmar entrega");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.add(btnCancelar);
        botones.add(btnConfirmar);
        contenido.add(botones, BorderLayout.SOUTH);

        setContentPane(contenido);

        btnCancelar.addActionListener(e -> dispose());
        btnConfirmar.addActionListener(e -> confirmar());
    }

    private void agregarFila(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila;
        c.insets = new Insets(6, 0, 6, 12);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.insets = new Insets(6, 0, 6, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        panel.add(campo, c);
    }

    private void confirmar() {
        try {
            entregaController.registrarEntrega(objeto, txtNombre.getText(),
                    txtDocumento.getText(), txtObservaciones.getText());
            Mensajes.informacion(this, "Entrega registrada. El objeto quedó como Entregado.");
            guardado = true;
            dispose();
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }

    public boolean isGuardado() {
        return guardado;
    }
}
