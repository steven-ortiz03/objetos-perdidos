package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import objetosperdidos.controller.ObjetoController;
import objetosperdidos.model.Categoria;
import objetosperdidos.model.EstadoObjeto;
import objetosperdidos.model.FiltroObjeto;
import objetosperdidos.model.Lugar;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.Tema;

/**
 * Catálogo de objetos encontrados con buscador y filtros (CU-06, CU-07).
 * Lo usan tanto los estudiantes como los administradores.
 */
public class PnlCatalogo extends JPanel implements Recargable {

    private static final String TODAS = "Todas las categorías";
    private static final String TODOS_LUGARES = "Todos los lugares";
    private static final String TODOS_ESTADOS = "Todos los estados";
    private static final int ANCHO_TARJETA = 235;

    private final ObjetoController objetoController = new ObjetoController();

    private final JTextField txtBuscar = new JTextField(20);
    private final JComboBox<Object> cmbCategoria = new JComboBox<>();
    private final JComboBox<Object> cmbLugar = new JComboBox<>();
    private final JComboBox<Object> cmbEstado = new JComboBox<>();
    private final JCheckBox chkFechas = new JCheckBox("Filtrar por fecha encontrada:");
    private final SelectorFecha fechaDesde = new SelectorFecha();
    private final SelectorFecha fechaHasta = new SelectorFecha();
    private final JLabel lblResultado = new JLabel(" ");

    private final GridLayout rejilla = new GridLayout(0, 4, 15, 15);
    private final JPanel pnlTarjetas = new JPanel(rejilla);
    private final JScrollPane scroll;

    public PnlCatalogo() {
        setLayout(new BorderLayout(0, 12));
        setBackground(Tema.FONDO);
        setBorder(BorderFactory.createEmptyBorder(22, 26, 18, 26));

        add(construirFiltros(), BorderLayout.NORTH);

        // Las tarjetas van arriba de un panel contenedor para que no se estiren hacia abajo
        pnlTarjetas.setOpaque(false);
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Tema.FONDO);
        contenedor.add(pnlTarjetas, BorderLayout.NORTH);

        scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll, BorderLayout.CENTER);

        // Al cambiar el tamaño de la ventana se ajusta el número de columnas
        scroll.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarColumnas();
            }
        });
    }

    private JPanel construirFiltros() {
        JPanel filtros = new JPanel();
        filtros.setLayout(new BoxLayout(filtros, BoxLayout.Y_AXIS));
        filtros.setOpaque(false);

        JPanel filaTitulo = new JPanel(new BorderLayout());
        filaTitulo.setOpaque(false);
        filaTitulo.add(Tema.titulo("Catálogo de objetos encontrados"), BorderLayout.WEST);
        lblResultado.setForeground(Tema.GRIS);
        filaTitulo.add(lblResultado, BorderLayout.EAST);

        // Fila 1: palabra clave y listas
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        fila1.setOpaque(false);
        txtBuscar.setPreferredSize(new Dimension(220, 34));
        txtBuscar.setToolTipText("Palabra clave en el nombre o la descripción");
        cmbEstado.addItem(TODOS_ESTADOS);
        cmbEstado.addItem(EstadoObjeto.DISPONIBLE);
        cmbEstado.addItem(EstadoObjeto.ENTREGADO);
        fila1.add(new JLabel("Buscar:"));
        fila1.add(txtBuscar);
        fila1.add(cmbCategoria);
        fila1.add(cmbLugar);
        fila1.add(cmbEstado);

        // Fila 2: rango de fechas y botones
        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        fila2.setOpaque(false);
        chkFechas.setOpaque(false);
        fechaDesde.setFecha(LocalDate.now().minusMonths(1));
        fechaDesde.setEnabled(false);
        fechaHasta.setEnabled(false);
        JButton btnBuscar = Tema.botonPrimario("Buscar");
        JButton btnLimpiar = Tema.botonSecundario("Limpiar filtros");
        fila2.add(chkFechas);
        fila2.add(new JLabel("desde"));
        fila2.add(fechaDesde);
        fila2.add(new JLabel("hasta"));
        fila2.add(fechaHasta);
        fila2.add(btnBuscar);
        fila2.add(btnLimpiar);

        filtros.add(filaTitulo);
        filtros.add(fila1);
        filtros.add(fila2);

        // Acciones
        chkFechas.addActionListener(e -> {
            fechaDesde.setEnabled(chkFechas.isSelected());
            fechaHasta.setEnabled(chkFechas.isSelected());
        });
        btnBuscar.addActionListener(e -> buscar());
        txtBuscar.addActionListener(e -> buscar()); // Enter en el campo de texto
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        return filtros;
    }

    /** Se ejecuta cada vez que se abre el catálogo desde el menú. */
    @Override
    public void recargar() {
        cargarListas();
        buscar();
    }

    /** Llena las listas de categorías y lugares conservando lo que estaba seleccionado. */
    private void cargarListas() {
        try {
            Object categoriaActual = cmbCategoria.getSelectedItem();
            Object lugarActual = cmbLugar.getSelectedItem();

            cmbCategoria.removeAllItems();
            cmbCategoria.addItem(TODAS);
            for (Categoria categoria : objetoController.listarCategorias()) {
                cmbCategoria.addItem(categoria);
            }
            cmbLugar.removeAllItems();
            cmbLugar.addItem(TODOS_LUGARES);
            for (Lugar lugar : objetoController.listarLugares()) {
                cmbLugar.addItem(lugar);
            }
            if (categoriaActual != null) {
                cmbCategoria.setSelectedItem(categoriaActual);
            }
            if (lugarActual != null) {
                cmbLugar.setSelectedItem(lugarActual);
            }
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }

    /** Arma el filtro con lo que eligió el usuario y muestra las tarjetas. */
    private void buscar() {
        FiltroObjeto filtro = new FiltroObjeto();
        filtro.setTexto(txtBuscar.getText());
        // Si en la lista está elegido "Todas...", el elemento es un texto y no se filtra
        if (cmbCategoria.getSelectedItem() instanceof Categoria) {
            filtro.setCategoria((Categoria) cmbCategoria.getSelectedItem());
        }
        if (cmbLugar.getSelectedItem() instanceof Lugar) {
            filtro.setLugarEncontrado((Lugar) cmbLugar.getSelectedItem());
        }
        if (cmbEstado.getSelectedItem() instanceof EstadoObjeto) {
            filtro.setEstado((EstadoObjeto) cmbEstado.getSelectedItem());
        }
        if (chkFechas.isSelected()) {
            filtro.setDesde(fechaDesde.getFecha());
            filtro.setHasta(fechaHasta.getFecha());
        }

        try {
            List<ObjetoPerdido> objetos = objetoController.buscar(filtro);
            mostrarTarjetas(objetos);
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }

    private void mostrarTarjetas(List<ObjetoPerdido> objetos) {
        pnlTarjetas.removeAll();
        for (ObjetoPerdido objeto : objetos) {
            TarjetaObjeto tarjeta = new TarjetaObjeto(objeto);
            tarjeta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    abrirDetalle(objeto);
                }
            });
            pnlTarjetas.add(tarjeta);
        }
        if (objetos.isEmpty()) {
            lblResultado.setText("No se encontraron objetos con esos criterios");
        } else {
            lblResultado.setText(objetos.size() + " objeto(s) encontrado(s)");
        }
        ajustarColumnas();
        pnlTarjetas.revalidate();
        pnlTarjetas.repaint();
    }

    /** Calcula cuántas tarjetas caben por fila según el ancho disponible. */
    private void ajustarColumnas() {
        int ancho = scroll.getViewport().getWidth();
        int columnas = Math.max(1, ancho / ANCHO_TARJETA);
        if (rejilla.getColumns() != columnas) {
            rejilla.setColumns(columnas);
            pnlTarjetas.revalidate();
        }
    }

    private void abrirDetalle(ObjetoPerdido objeto) {
        new DlgDetalleObjeto(SwingUtilities.getWindowAncestor(this), objeto).setVisible(true);
    }

    private void limpiarFiltros() {
        txtBuscar.setText("");
        cmbCategoria.setSelectedIndex(0);
        cmbLugar.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        chkFechas.setSelected(false);
        fechaDesde.setEnabled(false);
        fechaHasta.setEnabled(false);
        buscar();
    }
}
