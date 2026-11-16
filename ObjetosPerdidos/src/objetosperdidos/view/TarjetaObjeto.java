package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.ImagenUtil;
import objetosperdidos.util.Tema;

/**
 * Tarjeta del catálogo: muestra la foto, el nombre, el estado y el lugar
 * de un objeto (RF-14, RF-19). Se crea una por cada objeto encontrado.
 */
public class TarjetaObjeto extends JPanel {

    private final ObjetoPerdido objeto;

    public TarjetaObjeto(ObjetoPerdido objeto) {
        this.objeto = objeto;
        setLayout(new BorderLayout(0, 8));
        setBackground(Tema.BLANCO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE),
                BorderFactory.createEmptyBorder(10, 10, 12, 10)));
        setPreferredSize(new Dimension(220, 285));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Clic para ver el detalle");

        // Fotografía en miniatura
        JLabel lblFoto = new JLabel(ImagenUtil.cargar(objeto.getRutaFoto(), 198, 150));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(198, 150));
        add(lblFoto, BorderLayout.NORTH);

        // Textos
        JPanel textos = new JPanel(new GridLayout(4, 1, 0, 2));
        textos.setOpaque(false);

        JLabel lblNombre = new JLabel(objeto.getNombre());
        lblNombre.setFont(Tema.FUENTE_NEGRITA);
        lblNombre.setForeground(Tema.TEXTO);

        JLabel lblEstado = new JLabel("● " + objeto.getEstado().getEtiqueta());
        lblEstado.setFont(Tema.FUENTE_NEGRITA.deriveFont(12f));
        lblEstado.setForeground(objeto.estaDisponible() ? Tema.VERDE : Tema.GRIS);

        JLabel lblLugar = new JLabel("Encontrado en: " + objeto.getLugarEncontrado().getNombre());
        lblLugar.setFont(Tema.FUENTE_PEQUENA);
        lblLugar.setForeground(Tema.GRIS);

        JLabel lblFecha = new JLabel("Fecha: " + objeto.getFechaEncontrado().format(Tema.FECHA));
        lblFecha.setFont(Tema.FUENTE_PEQUENA);
        lblFecha.setForeground(Tema.GRIS);

        textos.add(lblNombre);
        textos.add(lblEstado);
        textos.add(lblLugar);
        textos.add(lblFecha);
        add(textos, BorderLayout.CENTER);
    }

    public ObjetoPerdido getObjeto() {
        return objeto;
    }
}
