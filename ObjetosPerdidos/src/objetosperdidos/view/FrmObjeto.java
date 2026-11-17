package objetosperdidos.view;

import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import objetosperdidos.controller.ObjetoController;
import objetosperdidos.model.Categoria;
import objetosperdidos.model.Lugar;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.FechaUtil;
import objetosperdidos.util.ImagenUtil;
import objetosperdidos.util.Tema;

/**
 * Formulario para registrar un objeto nuevo o editar uno existente (CU-03, CU-04).
 * Si recibe un objeto, trabaja en modo edición; si recibe null, en modo registro.
 *
 * SE EDITA CON EL DISEÑADOR DE NETBEANS: abre el archivo y pulsa "Design". No modifiques la
 * sección "Generated Code"; la lógica va fuera de ella.
 */
public class FrmObjeto extends javax.swing.JFrame {

    private static final int ANCHO_FOTO = 240;
    private static final int ALTO_FOTO = 190;

    private final ObjetoController objetoController = new ObjetoController();

    private final ObjetoPerdido objetoEditado; // null cuando se registra uno nuevo
    private File fotoSeleccionada;             // null si no se eligió una foto nueva

    /** Modo registro. */
    public FrmObjeto() {
        this(null);
    }

    public FrmObjeto(ObjetoPerdido objeto) {
        this.objetoEditado = objeto;
        initComponents();
        aplicarEstilo();
        cargarListas();
        if (objeto != null) {
            setTitle("Editar objeto");
            lblTitulo.setText("Editar objeto");
            mostrarDatos(objeto);
        }
        setLocationRelativeTo(null);
    }

    /** Detalles de apariencia que no se configuran desde el diseñador. */
    private void aplicarEstilo() {
        Tema.estilizarBoton(btnFoto);
        Tema.estilizarBoton(btnCancelar);
        Tema.estilizarBoton(btnGuardar);
        FechaUtil.configurar(spnFecha);
        lblFoto.setText("");
        lblFoto.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        lblFoto.setIcon(ImagenUtil.cargar(null, ANCHO_FOTO, ALTO_FOTO));
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
        FechaUtil.poner(spnFecha, objeto.getFechaEncontrado());
        lblFoto.setIcon(ImagenUtil.cargar(objeto.getRutaFoto(), ANCHO_FOTO, ALTO_FOTO));
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
                lblFoto.setIcon(ImagenUtil.cargar(archivo.getPath(), ANCHO_FOTO, ALTO_FOTO));
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
        objeto.setFechaEncontrado(FechaUtil.leer(spnFecha));

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
            dispose();
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFondo = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblCategoria = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        lblDescripcion = new javax.swing.JLabel();
        scrollDescripcion = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        lblLugarEncontrado = new javax.swing.JLabel();
        cmbLugarEncontrado = new javax.swing.JComboBox<>();
        lblFecha = new javax.swing.JLabel();
        spnFecha = new javax.swing.JSpinner();
        lblLugarResguardo = new javax.swing.JLabel();
        cmbLugarResguardo = new javax.swing.JComboBox<>();
        lblFoto = new javax.swing.JLabel();
        btnFoto = new javax.swing.JButton();
        lblAyudaFoto = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nuevo objeto encontrado");
        setResizable(false);

        pnlFondo.setBackground(new java.awt.Color(255, 255, 255));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(30, 58, 95));
        lblTitulo.setText("Registrar objeto encontrado");

        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(31, 41, 55));
        lblNombre.setText("Nombre *");

        lblCategoria.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblCategoria.setForeground(new java.awt.Color(31, 41, 55));
        lblCategoria.setText("Categoría *");

        lblDescripcion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(31, 41, 55));
        lblDescripcion.setText("Descripción *");

        txtDescripcion.setColumns(20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setRows(4);
        txtDescripcion.setWrapStyleWord(true);

        scrollDescripcion.setViewportView(txtDescripcion);

        lblLugarEncontrado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLugarEncontrado.setForeground(new java.awt.Color(31, 41, 55));
        lblLugarEncontrado.setText("Lugar encontrado *");

        lblFecha.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(31, 41, 55));
        lblFecha.setText("Fecha encontrado *");

        lblLugarResguardo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLugarResguardo.setForeground(new java.awt.Color(31, 41, 55));
        lblLugarResguardo.setText("Lugar de resguardo *");

        lblFoto.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblFoto.setForeground(new java.awt.Color(107, 114, 128));
        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setText("Vista previa de la fotografía");

        btnFoto.setBackground(new java.awt.Color(255, 255, 255));
        btnFoto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnFoto.setForeground(new java.awt.Color(31, 41, 55));
        btnFoto.setText("Seleccionar fotografía *");
        btnFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoActionPerformed(evt);
            }
        });

        lblAyudaFoto.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblAyudaFoto.setForeground(new java.awt.Color(107, 114, 128));
        lblAyudaFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAyudaFoto.setText("JPG o PNG, máximo 5 MB");

        btnCancelar.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(31, 41, 55));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(30, 58, 95));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFondoLayout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnlFondoLayout.createSequentialGroup()
                            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblLugarEncontrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblLugarResguardo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                .addComponent(cmbCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                .addComponent(scrollDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                .addComponent(cmbLugarEncontrado, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                .addComponent(spnFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                .addComponent(cmbLugarResguardo, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                            .addGap(24, 24, 24)
                            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblAyudaFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(26, 26, 26))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFondoLayout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16)
                    .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFondoLayout.createSequentialGroup()
                            .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(lblCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(lblDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(lblLugarEncontrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbLugarEncontrado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(spnFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(lblLugarResguardo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbLugarResguardo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlFondoLayout.createSequentialGroup()
                            .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addComponent(btnFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(lblAyudaFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(22, 22, 22)
                    .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoActionPerformed
        seleccionarFoto();
    }//GEN-LAST:event_btnFotoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    /** Permite ejecutar solo esta ventana para ver su diseño: clic derecho > Run File (Shift+F6). */
    public static void main(String[] args) {
        objetosperdidos.util.Tema.aplicarLookAndFeel();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmObjeto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFoto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<Object> cmbCategoria;
    private javax.swing.JComboBox<Object> cmbLugarEncontrado;
    private javax.swing.JComboBox<Object> cmbLugarResguardo;
    private javax.swing.JLabel lblAyudaFoto;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblLugarEncontrado;
    private javax.swing.JLabel lblLugarResguardo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JScrollPane scrollDescripcion;
    private javax.swing.JSpinner spnFecha;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
