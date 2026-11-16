package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.ImagenUtil;
import objetosperdidos.util.Tema;

/** Ficha completa de un objeto: foto ampliada, descripción y dónde reclamarlo (CU-08). */
public class DlgDetalleObjeto extends JDialog {

    public DlgDetalleObjeto(Window padre, ObjetoPerdido objeto) {
        super(padre, "Detalle del objeto", ModalityType.APPLICATION_MODAL);

        JPanel contenido = new JPanel(new BorderLayout(22, 14));
        contenido.setBackground(Tema.BLANCO);
        contenido.setBorder(BorderFactory.createEmptyBorder(22, 24, 18, 24));

        // ----- Fotografía ampliada -----
        JLabel lblFoto = new JLabel(ImagenUtil.cargar(objeto.getRutaFoto(), 380, 320));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(380, 320));
        lblFoto.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        contenido.add(lblFoto, BorderLayout.WEST);

        // ----- Información -----
        JPanel info = new JPanel(new BorderLayout(0, 10));
        info.setOpaque(false);
        info.setPreferredSize(new Dimension(360, 320));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezado.setOpaque(false);
        encabezado.add(Tema.titulo(objeto.getNombre()));
        JLabel lblEstado = new JLabel("● " + objeto.getEstado().getEtiqueta()
                + "   ·   " + objeto.getCategoria().getNombre());
        lblEstado.setFont(Tema.FUENTE_NEGRITA);
        lblEstado.setForeground(objeto.estaDisponible() ? Tema.VERDE : Tema.GRIS);
        encabezado.add(lblEstado);
        info.add(encabezado, BorderLayout.NORTH);

        JTextArea txtDescripcion = new JTextArea(objeto.getDescripcion());
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(Tema.FONDO);
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        info.add(scrollDescripcion, BorderLayout.CENTER);

        JPanel datos = new JPanel(new GridLayout(0, 1, 0, 5));
        datos.setOpaque(false);
        datos.add(new JLabel("Encontrado en: " + objeto.getLugarEncontrado().getNombre()));
        datos.add(new JLabel("Fecha encontrado: " + objeto.getFechaEncontrado().format(Tema.FECHA)));

        // Mensaje destacado: dónde reclamarlo o cuándo se entregó
        JLabel lblReclamo = new JLabel();
        lblReclamo.setOpaque(true);
        lblReclamo.setFont(Tema.FUENTE_NEGRITA);
        lblReclamo.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        if (objeto.estaDisponible()) {
            lblReclamo.setText("Reclámalo en: " + objeto.getLugarResguardo().getNombre());
            lblReclamo.setBackground(new Color(0xDCFCE7));
            lblReclamo.setForeground(new Color(0x166534));
        } else {
            String fecha = objeto.getFechaEntrega() == null ? "" : " el " + objeto.getFechaEntrega().format(Tema.FECHA);
            lblReclamo.setText("Este objeto ya fue entregado" + fecha);
            lblReclamo.setBackground(Tema.BORDE);
            lblReclamo.setForeground(Tema.TEXTO);
        }
        datos.add(lblReclamo);
        info.add(datos, BorderLayout.SOUTH);
        contenido.add(info, BorderLayout.CENTER);

        // ----- Botón cerrar -----
        JButton btnCerrar = Tema.botonPrimario("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        botones.setOpaque(false);
        botones.add(btnCerrar);
        contenido.add(botones, BorderLayout.SOUTH);

        setContentPane(contenido);
        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }
}
