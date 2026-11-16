package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import objetosperdidos.controller.ObjetoController;
import objetosperdidos.model.Categoria;
import objetosperdidos.model.Lugar;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.ImagenUtil;
import objetosperdidos.util.Tema;

/**
 * Formulario para registrar un objeto nuevo o editar uno existente (CU-03, CU-04).
 * Si recibe un objeto, trabaja en modo edición; si recibe null, en modo registro.
 */
public class DlgObjeto extends JDialog {

    private final ObjetoController objetoController = new ObjetoController();

    private final ObjetoPerdido objetoEditado; // null cuando se registra uno nuevo
    private File fotoSeleccionada;             // null si no se eligió una foto nueva
    private boolean guardado = false;

    private final JTextField txtNombre = new JTextField(24);
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();
    private final JTextArea txtDescripcion = new JTextArea(4, 24);
    private final JComboBox<Lugar> cmbLugarEncontrado = new JComboBox<>();
    private final SelectorFecha fechaEncontrado = new SelectorFecha();
    private final JComboBox<Lugar> cmbLugarResguardo = new JComboBox<>();
    private final JLabel lblFoto = new JLabel();

    public DlgObjeto(Window padre, ObjetoPerdido objeto) {
        super(padre, objeto == null ? "Nuevo objeto encontrado" : "Editar objeto",
                ModalityType.APPLICATION_MODAL);
        this.objetoEditado = objeto;
        construirInterfaz();
        cargarListas();
        if (objeto != null) {
            mostrarDatos(objeto);
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    private void construirInterfaz() {
        JPanel contenido = new JPanel(new BorderLayout(22, 14));
        contenido.setBackground(Tema.BLANCO);
        contenido.setBorder(BorderFactory.createEmptyBorder(22, 26, 20, 26));
        contenido.add(Tema.titulo(objetoEditado == null ? "Registrar objeto encontrado" : "Editar objeto"),
                BorderLayout.NORTH);

        // ----- Formulario -----
        txtNombre.setPreferredSize(new Dimension(290, 34));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        agregarFila(formulario, 0, "Nombre *", txtNombre);
        agregarFila(formulario, 1, "Categoría *", cmbCategoria);
        agregarFila(formulario, 2, "Descripción *", scrollDescripcion);
        agregarFila(formulario, 3, "Lugar encontrado *", cmbLugarEncontrado);
        agregarFila(formulario, 4, "Fecha encontrado *", fechaEncontrado);
        agregarFila(formulario, 5, "Lugar de resguardo *", cmbLugarResguardo);
        contenido.add(formulario, BorderLayout.CENTER);

        // ----- Fotografía con vista previa -----
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(240, 190));
        lblFoto.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        lblFoto.setIcon(ImagenUtil.cargar(null, 240, 190));
        JButton btnFoto = Tema.botonSecundario("Seleccionar fotografía *");
        JLabel lblAyudaFoto = new JLabel("JPG o PNG, máximo 5 MB", SwingConstants.CENTER);
        lblAyudaFoto.setFont(Tema.FUENTE_PEQUENA);
        lblAyudaFoto.setForeground(Tema.GRIS);

        JPanel pnlBotonFoto = new JPanel(new BorderLayout(0, 4));
        pnlBotonFoto.setOpaque(false);
        pnlBotonFoto.add(btnFoto, BorderLayout.NORTH);
        pnlBotonFoto.add(lblAyudaFoto, BorderLayout.SOUTH);

        JPanel pnlFoto = new JPanel(new BorderLayout(0, 8));
        pnlFoto.setOpaque(false);
        pnlFoto.add(lblFoto, BorderLayout.NORTH);
        pnlFoto.add(pnlBotonFoto, BorderLayout.CENTER);
        JPanel envolturaFoto = new JPanel(new BorderLayout());
        envolturaFoto.setOpaque(false);
        envolturaFoto.add(pnlFoto, BorderLayout.NORTH);
        contenido.add(envolturaFoto, BorderLayout.EAST);

        // ----- Botones -----
        JButton btnCancelar = Tema.botonSecundario("Cancelar");
        JButton btnGuardar = Tema.botonPrimario("Guardar");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        contenido.add(botones, BorderLayout.SOUTH);

        setContentPane(contenido);

        btnFoto.addActionListener(e -> seleccionarFoto());
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
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

    private void cargarListas() {
        try {
            for (Categoria categoria : objetoController.listarCategorias()) {
                cmbCategoria.addItem(categoria);
            }
            for (Lugar lugar : objetoController.listarLugares()) {
                cmbLugarEncontrado.addItem(lugar);
                cmbLugarResguardo.addItem(lugar);
            }
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }

    /** En modo edición, llena el formulario con los datos actuales del objeto. */
    private void mostrarDatos(ObjetoPerdido objeto) {
        txtNombre.setText(objeto.getNombre());
        txtDescripcion.setText(objeto.getDescripcion());
        cmbCategoria.setSelectedItem(objeto.getCategoria());
        cmbLugarEncontrado.setSelectedItem(objeto.getLugarEncontrado());
        cmbLugarResguardo.setSelectedItem(objeto.getLugarResguardo());
        fechaEncontrado.setFecha(objeto.getFechaEncontrado());
        lblFoto.setIcon(ImagenUtil.cargar(objeto.getRutaFoto(), 240, 190));
    }

    /** Abre el selector de archivos y muestra la vista previa (RF-06). */
    private void seleccionarFoto() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccionar fotografía del objeto");
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes JPG o PNG", "jpg", "jpeg", "png"));
        selector.setAcceptAllFileFilterUsed(false);

        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();
            try {
                ImagenUtil.validar(archivo);
                fotoSeleccionada = archivo;
                lblFoto.setIcon(ImagenUtil.cargar(archivo.getPath(), 240, 190));
            } catch (Exception error) {
                Mensajes.error(this, error);
            }
        }
    }

    private void guardar() {
        // En edición se modifica el mismo objeto; en registro se crea uno nuevo
        ObjetoPerdido objeto = (objetoEditado != null) ? objetoEditado : new ObjetoPerdido();
        objeto.setNombre(txtNombre.getText().trim());
        objeto.setDescripcion(txtDescripcion.getText().trim());
        objeto.setCategoria((Categoria) cmbCategoria.getSelectedItem());
        objeto.setLugarEncontrado((Lugar) cmbLugarEncontrado.getSelectedItem());
        objeto.setLugarResguardo((Lugar) cmbLugarResguardo.getSelectedItem());
        objeto.setFechaEncontrado(fechaEncontrado.getFecha());

        try {
            objetoController.validar(objeto);

            // Advertencia de posible duplicado: el usuario decide si continúa
            if (objetoController.existePosibleDuplicado(objeto)
                    && !Mensajes.confirmar(this, "Ya existe un objeto con el mismo nombre, lugar y fecha.\n"
                            + "¿Desea guardarlo de todas formas?")) {
                return;
            }

            if (objetoEditado == null) {
                objetoController.registrar(objeto, fotoSeleccionada);
                Mensajes.informacion(this, "Objeto registrado correctamente. Ya aparece en el catálogo.");
            } else {
                objetoController.actualizar(objeto, fotoSeleccionada);
                Mensajes.informacion(this, "Objeto actualizado correctamente.");
            }
            guardado = true;
            dispose();
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }

    /** Indica a quien abrió el diálogo si se guardaron cambios. */
    public boolean isGuardado() {
        return guardado;
    }
}
