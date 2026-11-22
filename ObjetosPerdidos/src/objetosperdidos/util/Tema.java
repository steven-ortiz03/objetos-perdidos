package objetosperdidos.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Tema visual de toda la aplicación (RNF-11).
 * Los colores, fuentes y estilos de botones se definen una sola vez aquí
 * para que todas las ventanas se vean iguales.
 */
public class Tema {

    // Colores
    public static final Color AZUL = new Color(0x1E3A5F);
    public static final Color AZUL_CLARO = new Color(0x2C5282);
    public static final Color TURQUESA = new Color(0x14B8A6);
    public static final Color FONDO = new Color(0xF5F7FA);
    public static final Color BLANCO = Color.WHITE;
    public static final Color TEXTO = new Color(0x1F2937);
    public static final Color VERDE = new Color(0x16A34A);
    public static final Color GRIS = new Color(0x6B7280);
    public static final Color ROJO = new Color(0xDC2626);
    public static final Color BORDE = new Color(0xE5E7EB);

    // Fuentes
    public static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_NEGRITA = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_PEQUENA = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_NUMERO = new Font("Segoe UI", Font.BOLD, 36);

    // Formatos de fecha para mostrar en pantalla
    public static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Activa la apariencia moderna FlatLaf (archivo .jar opcional en la carpeta lib).
     * Se carga por su nombre para que el proyecto compile aunque falte. Si no está disponible,
     * se usa la apariencia del sistema operativo y la aplicación sigue funcionando.
     */
    public static void aplicarLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorado) {
                // Se mantiene la apariencia por defecto de Java
            }
        }
        UIManager.put("Label.font", FUENTE);
        UIManager.put("Button.font", FUENTE);
        UIManager.put("TextField.font", FUENTE);
        UIManager.put("PasswordField.font", FUENTE);
        UIManager.put("TextArea.font", FUENTE);
        UIManager.put("ComboBox.font", FUENTE);
        UIManager.put("CheckBox.font", FUENTE);
        UIManager.put("Table.font", FUENTE);
        UIManager.put("TableHeader.font", FUENTE_NEGRITA);
        UIManager.put("Spinner.font", FUENTE);
        UIManager.put("OptionPane.messageFont", FUENTE);
        UIManager.put("OptionPane.buttonFont", FUENTE);
    }

    /**
     * Da a un JButton creado en el diseñador de NetBeans el mismo aspecto redondeado de los
     * demás botones. Respeta el color de fondo y de texto que se configuren en el diseñador.
     */
    public static void estilizarBoton(JButton boton) {
        estilizarBoton(boton, true);
    }

    /** Botón de menú lateral: fondo plano, sin contorno y con el texto separado del borde. */
    public static void estilizarBotonMenu(JButton boton) {
        estilizarBoton(boton, false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
    }

    private static void estilizarBoton(JButton boton, boolean conContorno) {
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, javax.swing.JComponent componente) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                JButton b = (JButton) componente;
                Color fondo = b.getBackground();
                if (!b.isEnabled()) {
                    fondo = BORDE;
                } else if (b.getModel().isPressed()) {
                    fondo = fondo.darker();
                }
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 8, 8);
                if (conContorno) {
                    g2.setColor(BORDE);
                    g2.drawRoundRect(0, 0, b.getWidth() - 1, b.getHeight() - 1, 8, 8);
                }
                g2.dispose();
                super.paint(g, componente); // dibuja el texto
            }
        });
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Botón de la acción principal (azul). */
    public static JButton botonPrimario(String texto) {
        return new Boton(texto, AZUL, BLANCO, false);
    }

    /** Botón de acción secundaria (blanco con borde). */
    public static JButton botonSecundario(String texto) {
        return new Boton(texto, BLANCO, TEXTO, true);
    }

    /** Botón de acción positiva (turquesa). */
    public static JButton botonAcento(String texto) {
        return new Boton(texto, TURQUESA, BLANCO, false);
    }

    /** Botón de acción delicada, como eliminar (rojo). */
    public static JButton botonPeligro(String texto) {
        return new Boton(texto, ROJO, BLANCO, false);
    }

    /** Botón del menú lateral: su color de fondo lo cambia la ventana principal. */
    public static JButton botonMenu(String texto) {
        Boton boton = new Boton(texto, AZUL, BLANCO, false);
        boton.setFont(FUENTE);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        return boton;
    }

    /**
     * Botón que dibuja su propio fondo redondeado. Así los colores se ven igual con
     * cualquier apariencia de Swing (la de Windows, por ejemplo, ignora setBackground).
     */
    private static class Boton extends JButton {

        private final boolean conBorde;

        Boton(String texto, Color fondo, Color colorTexto, boolean conBorde) {
            super(texto);
            this.conBorde = conBorde;
            setFont(FUENTE_NEGRITA);
            setBackground(fondo);
            setForeground(colorTexto);
            setContentAreaFilled(false); // el fondo lo dibujamos nosotros
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fondo = getBackground();
            if (!isEnabled()) {
                fondo = BORDE;
            } else if (getModel().isPressed()) {
                fondo = fondo.darker();
            } else if (getModel().isRollover()) {
                fondo = conBorde ? FONDO : fondo.brighter();
            }
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            if (conBorde) {
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
            g2.dispose();
            super.paintComponent(g); // dibuja el texto
        }
    }

    public static JLabel titulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_TITULO);
        etiqueta.setForeground(AZUL);
        return etiqueta;
    }

    /** Estilo común para todas las tablas. */
    public static void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(30);
        tabla.setFont(FUENTE);
        tabla.setGridColor(BORDE);
        tabla.setSelectionBackground(new Color(0xCCFBF1));
        tabla.setSelectionForeground(TEXTO);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setFont(FUENTE_NEGRITA);
        tabla.getTableHeader().setReorderingAllowed(false);
    }
}
