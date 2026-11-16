package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import objetosperdidos.controller.ObjetoController;
import objetosperdidos.model.FiltroObjeto;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.Tema;

/**
 * Panel del administrador: tabla con todos los objetos y botones para
 * crear, editar, eliminar y registrar entregas (RF-08 a RF-12).
 */
public class PnlGestionObjetos extends JPanel implements Recargable {

    private final ObjetoController objetoController = new ObjetoController();

    private final JTextField txtBuscar = new JTextField(18);
    private final DefaultTableModel modelo;
    private final JTable tabla;

    // Lista en el mismo orden de las filas, para saber qué objeto se seleccionó
    private List<ObjetoPerdido> objetos = new ArrayList<>();

    public PnlGestionObjetos() {
        setLayout(new BorderLayout(0, 12));
        setBackground(Tema.FONDO);
        setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        // ----- Encabezado y botones -----
        JButton btnNuevo = Tema.botonAcento("+ Nuevo objeto");
        JButton btnEditar = Tema.botonSecundario("Editar");
        JButton btnEntregar = Tema.botonPrimario("Registrar entrega");
        JButton btnEliminar = Tema.botonPeligro("Eliminar");
        JButton btnBuscar = Tema.botonSecundario("Buscar");
        txtBuscar.setPreferredSize(new Dimension(180, 34));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(btnNuevo);
        acciones.add(btnEditar);
        acciones.add(btnEntregar);
        acciones.add(btnEliminar);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        busqueda.setOpaque(false);
        busqueda.add(new JLabel("Buscar:"));
        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);
        barra.add(acciones, BorderLayout.WEST);
        barra.add(busqueda, BorderLayout.EAST);

        JPanel norte = new JPanel(new BorderLayout(0, 12));
        norte.setOpaque(false);
        norte.add(Tema.titulo("Gestión de objetos"), BorderLayout.NORTH);
        norte.add(barra, BorderLayout.SOUTH);
        add(norte, BorderLayout.NORTH);

        // ----- Tabla -----
        String[] columnas = {"ID", "Nombre", "Categoría", "Lugar encontrado", "Fecha", "Resguardo", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false; // la tabla es solo de consulta
            }
        };
        tabla = new JTable(modelo);
        Tema.estilizarTabla(tabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(60);
        tabla.getColumnModel().getColumn(4).setMinWidth(105);
        tabla.getColumnModel().getColumn(4).setMaxWidth(120);
        tabla.getColumnModel().getColumn(6).setMinWidth(100);
        tabla.getColumnModel().getColumn(6).setMaxWidth(120);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        add(scroll, BorderLayout.CENTER);

        JLabel lblAyuda = new JLabel("Seleccione una fila y use los botones. Doble clic para ver el detalle.");
        lblAyuda.setFont(Tema.FUENTE_PEQUENA);
        lblAyuda.setForeground(Tema.GRIS);
        add(lblAyuda, BorderLayout.SOUTH);

        // ----- Acciones -----
        btnNuevo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editar());
        btnEntregar.addActionListener(e -> entregar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> recargar());
        txtBuscar.addActionListener(e -> recargar());
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && getSeleccionado(false) != null) {
                    new DlgDetalleObjeto(getVentana(), getSeleccionado(false)).setVisible(true);
                }
            }
        });
    }

    /** Vuelve a consultar los objetos y llena la tabla. */
    @Override
    public void recargar() {
        try {
            FiltroObjeto filtro = new FiltroObjeto();
            filtro.setTexto(txtBuscar.getText());
            objetos = objetoController.buscar(filtro);

            modelo.setRowCount(0); // borra las filas anteriores
            for (ObjetoPerdido objeto : objetos) {
                modelo.addRow(new Object[]{
                    objeto.getId(),
                    objeto.getNombre(),
                    objeto.getCategoria().getNombre(),
                    objeto.getLugarEncontrado().getNombre(),
                    objeto.getFechaEncontrado().format(Tema.FECHA),
                    objeto.getLugarResguardo().getNombre(),
                    objeto.getEstado().getEtiqueta()
                });
            }
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }

    private Window getVentana() {
        return SwingUtilities.getWindowAncestor(this);
    }

    /** Devuelve el objeto de la fila seleccionada, o null si no hay ninguna. */
    private ObjetoPerdido getSeleccionado(boolean avisar) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            if (avisar) {
                Mensajes.advertencia(this, "Primero seleccione un objeto de la tabla.");
            }
            return null;
        }
        return objetos.get(fila);
    }

    /** Abre el formulario para crear (objeto = null) o editar. */
    private void abrirFormulario(ObjetoPerdido objeto) {
        DlgObjeto dialogo = new DlgObjeto(getVentana(), objeto);
        dialogo.setVisible(true); // el código se detiene aquí hasta que se cierre el diálogo
        // Se recarga siempre: si se canceló una edición, la tabla vuelve a los datos guardados
        recargar();
    }

    private void editar() {
        ObjetoPerdido objeto = getSeleccionado(true);
        if (objeto != null) {
            abrirFormulario(objeto);
        }
    }

    private void entregar() {
        ObjetoPerdido objeto = getSeleccionado(true);
        if (objeto == null) {
            return;
        }
        if (!objeto.estaDisponible()) {
            Mensajes.advertencia(this, "Este objeto ya fue entregado.");
            return;
        }
        DlgEntrega dialogo = new DlgEntrega(getVentana(), objeto);
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) {
            recargar();
        }
    }

    private void eliminar() {
        ObjetoPerdido objeto = getSeleccionado(true);
        if (objeto == null) {
            return;
        }
        try {
            // El controlador vuelve a verificar esta regla; aquí se avisa antes de preguntar
            if (!objeto.estaDisponible()) {
                Mensajes.advertencia(this,
                        "Este objeto ya fue entregado y debe conservarse en el historial. No se puede eliminar.");
                return;
            }
            if (Mensajes.confirmar(this, "¿Eliminar el objeto \"" + objeto.getNombre()
                    + "\"?\nEsta acción no se puede deshacer.")) {
                objetoController.eliminar(objeto);
                Mensajes.informacion(this, "Objeto eliminado correctamente.");
                recargar();
            }
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }
}
