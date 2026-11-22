package objetosperdidos.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import objetosperdidos.controller.ObjetoController;
import objetosperdidos.model.Categoria;
import objetosperdidos.model.EstadoObjeto;
import objetosperdidos.model.FiltroObjeto;
import objetosperdidos.model.Lugar;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.FechaUtil;
import objetosperdidos.util.Tema;

/**
 * Catálogo de objetos encontrados con buscador y filtros (CU-06, CU-07).
 * Es un panel que se muestra dentro de la ventana principal; lo usan estudiantes y administradores.
 *
 * SE EDITA CON EL DISEÑADOR DE NETBEANS: abre el archivo y pulsa "Design". No modifiques la
 * sección "Generated Code"; la lógica va fuera de ella.
 * Las tarjetas de los objetos se crean por código dentro de pnlLista (ver TarjetaObjeto).
 */
public class PnlCatalogo extends javax.swing.JPanel implements Recargable {

    private static final String TODAS = "Todas las categorías";
    private static final String TODOS_LUGARES = "Todos los lugares";
    private static final String TODOS_ESTADOS = "Todos los estados";
    private static final int ANCHO_TARJETA = 235;

    private final ObjetoController objetoController = new ObjetoController();

    // Rejilla donde se acomodan las tarjetas; va dentro de pnlLista
    private final GridLayout rejilla = new GridLayout(0, 4, 15, 15);
    private final JPanel pnlTarjetas = new JPanel(rejilla);

    public PnlCatalogo() {
        initComponents();
        Tema.estilizarBoton(btnBuscar);
        Tema.estilizarBoton(btnLimpiar);

        cmbEstado.addItem(TODOS_ESTADOS);
        cmbEstado.addItem(EstadoObjeto.DISPONIBLE);
        cmbEstado.addItem(EstadoObjeto.ENTREGADO);

        FechaUtil.configurar(spnDesde);
        FechaUtil.configurar(spnHasta);
        FechaUtil.poner(spnDesde, LocalDate.now().minusMonths(1));
        activarFechas();

        // Las tarjetas van en la parte de arriba de pnlLista para que no se estiren hacia abajo
        pnlTarjetas.setOpaque(false);
        pnlLista.setLayout(new BorderLayout());
        pnlLista.add(pnlTarjetas, BorderLayout.NORTH);
        scrollTarjetas.setBorder(null);
        scrollTarjetas.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTarjetas.getVerticalScrollBar().setUnitIncrement(18);

        // Al cambiar el tamaño de la ventana se ajusta el número de columnas
        scrollTarjetas.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarColumnas();
            }
        });
    }

    /** Las fechas solo se pueden cambiar si la casilla está marcada. */
    private void activarFechas() {
        spnDesde.setEnabled(chkFechas.isSelected());
        spnHasta.setEnabled(chkFechas.isSelected());
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
            filtro.setDesde(FechaUtil.leer(spnDesde));
            filtro.setHasta(FechaUtil.leer(spnHasta));
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
                    Ventanas.abrirSobre(PnlCatalogo.this, new FrmDetalleObjeto(objeto), null);
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
        int ancho = scrollTarjetas.getViewport().getWidth();
        int columnas = Math.max(1, ancho / ANCHO_TARJETA);
        if (rejilla.getColumns() != columnas) {
            rejilla.setColumns(columnas);
            pnlTarjetas.revalidate();
        }
    }

    private void limpiarFiltros() {
        txtBuscar.setText("");
        cmbCategoria.setSelectedIndex(0);
        cmbLugar.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        chkFechas.setSelected(false);
        activarFechas();
        buscar();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblResultado = new javax.swing.JLabel();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        cmbCategoria = new javax.swing.JComboBox<>();
        cmbLugar = new javax.swing.JComboBox<>();
        cmbEstado = new javax.swing.JComboBox<>();
        chkFechas = new javax.swing.JCheckBox();
        lblDesde = new javax.swing.JLabel();
        spnDesde = new javax.swing.JSpinner();
        lblHasta = new javax.swing.JLabel();
        spnHasta = new javax.swing.JSpinner();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        scrollTarjetas = new javax.swing.JScrollPane();
        pnlLista = new javax.swing.JPanel();

        setBackground(new java.awt.Color(245, 247, 250));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(30, 58, 95));
        lblTitulo.setText("Catálogo de objetos encontrados");

        lblResultado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblResultado.setForeground(new java.awt.Color(107, 114, 128));
        lblResultado.setText("0 objeto(s) encontrado(s)");

        lblBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblBuscar.setForeground(new java.awt.Color(31, 41, 55));
        lblBuscar.setText("Buscar:");

        txtBuscar.setToolTipText("Palabra clave en el nombre o la descripción");
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        chkFechas.setBackground(new java.awt.Color(245, 247, 250));
        chkFechas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkFechas.setForeground(new java.awt.Color(31, 41, 55));
        chkFechas.setText("Filtrar por fecha encontrada:");
        chkFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFechasActionPerformed(evt);
            }
        });

        lblDesde.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDesde.setForeground(new java.awt.Color(31, 41, 55));
        lblDesde.setText("desde");

        lblHasta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblHasta.setForeground(new java.awt.Color(31, 41, 55));
        lblHasta.setText("hasta");

        btnBuscar.setBackground(new java.awt.Color(30, 58, 95));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(31, 41, 55));
        btnLimpiar.setText("Limpiar filtros");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        pnlLista.setBackground(new java.awt.Color(245, 247, 250));

        javax.swing.GroupLayout pnlListaLayout = new javax.swing.GroupLayout(pnlLista);
        pnlLista.setLayout(pnlListaLayout);
        pnlListaLayout.setHorizontalGroup(
            pnlListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 840, Short.MAX_VALUE)
        );
        pnlListaLayout.setVerticalGroup(
            pnlListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE)
        );

        scrollTarjetas.setViewportView(pnlLista);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 20, Short.MAX_VALUE)
                            .addComponent(lblResultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbLugar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(chkFechas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(spnDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(spnHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(14, 14, 14)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(scrollTarjetas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblResultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12, 12, 12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbLugar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10, 10, 10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkFechas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spnDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spnHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(14, 14, 14)
                    .addComponent(scrollTarjetas, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void chkFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFechasActionPerformed
        activarFechas();
    }//GEN-LAST:event_chkFechasActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFiltros();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JCheckBox chkFechas;
    private javax.swing.JComboBox<Object> cmbCategoria;
    private javax.swing.JComboBox<Object> cmbEstado;
    private javax.swing.JComboBox<Object> cmbLugar;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblDesde;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JLabel lblResultado;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlLista;
    private javax.swing.JScrollPane scrollTarjetas;
    private javax.swing.JSpinner spnDesde;
    private javax.swing.JSpinner spnHasta;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
