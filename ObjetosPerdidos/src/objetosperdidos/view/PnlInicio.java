package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import objetosperdidos.controller.ObjetoController;
import objetosperdidos.model.EstadoObjeto;
import objetosperdidos.model.Usuario;
import objetosperdidos.util.Sesion;
import objetosperdidos.util.Tema;

/** Panel de bienvenida con un resumen de los objetos registrados. */
public class PnlInicio extends JPanel implements Recargable {

    private final ObjetoController objetoController = new ObjetoController();

    private final JLabel lblTotal = new JLabel("-");
    private final JLabel lblDisponibles = new JLabel("-");
    private final JLabel lblEntregados = new JLabel("-");

    public PnlInicio() {
        Usuario usuario = Sesion.getUsuario();
        setLayout(new BorderLayout(0, 20));
        setBackground(Tema.FONDO);
        setBorder(BorderFactory.createEmptyBorder(28, 30, 28, 30));

        // Encabezado
        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezado.setOpaque(false);
        encabezado.add(Tema.titulo("Bienvenido(a), " + usuario.getNombre()));
        String ayuda = usuario.puedeGestionarObjetos()
                ? "Desde el menú puede registrar objetos encontrados, entregarlos y consultar el historial."
                : "Abra el Catálogo para buscar su objeto y ver en qué dependencia puede reclamarlo.";
        JLabel lblAyuda = new JLabel(ayuda);
        lblAyuda.setForeground(Tema.GRIS);
        encabezado.add(lblAyuda);
        add(encabezado, BorderLayout.NORTH);

        // Tarjetas con los contadores
        JPanel contadores = new JPanel(new GridLayout(1, 3, 20, 0));
        contadores.setOpaque(false);
        contadores.add(crearContador("Objetos registrados", lblTotal, Tema.AZUL));
        contadores.add(crearContador("Disponibles para reclamar", lblDisponibles, Tema.VERDE));
        contadores.add(crearContador("Entregados a su dueño", lblEntregados, Tema.GRIS));

        // Se ubican arriba para que no se estiren a todo el alto de la ventana
        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(contadores, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearContador(String titulo, JLabel lblNumero, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 6));
        tarjeta.setBackground(Tema.BLANCO);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, color),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Tema.GRIS);
        lblNumero.setFont(Tema.FUENTE_NUMERO);
        lblNumero.setForeground(color);
        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblNumero, BorderLayout.CENTER);
        return tarjeta;
    }

    @Override
    public void recargar() {
        try {
            int disponibles = objetoController.contar(EstadoObjeto.DISPONIBLE);
            int entregados = objetoController.contar(EstadoObjeto.ENTREGADO);
            lblTotal.setText(String.valueOf(disponibles + entregados));
            lblDisponibles.setText(String.valueOf(disponibles));
            lblEntregados.setText(String.valueOf(entregados));
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }
}
