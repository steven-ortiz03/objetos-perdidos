package objetosperdidos.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import objetosperdidos.controller.ObjetoController;
import objetosperdidos.model.FiltroObjeto;
import objetosperdidos.model.ObjetoPerdido;
import objetosperdidos.util.Tema;

/**
 * Pantalla del administrador: tabla con todos los objetos y botones para crear, editar,
 * eliminar y registrar entregas (RF-08 a RF-12). Se muestra dentro de la ventana principal.
 *
 * SE EDITA CON EL DISEÑADOR DE NETBEANS: abre el archivo y pulsa "Design". No modifiques la
 * sección "Generated Code"; la lógica va fuera de ella.
 */
public class PnlGestionObjetos extends javax.swing.JPanel implements Recargable {

    private final ObjetoController objetoController = new ObjetoController();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Categoría", "Lugar encontrado", "Fecha", "Resguardo", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false; // la tabla es solo de consulta
        }
    };

    // Lista en el mismo orden de las filas, para saber qué objeto se seleccionó
    private List<ObjetoPerdido> objetos = new ArrayList<>();

    public PnlGestionObjetos() {
        initComponents();
        for (JButton boton : new JButton[]{btnNuevo, btnEditar, btnEntregar, btnEliminar, btnBuscar}) {
            Tema.estilizarBoton(boton);
        }

        tblObjetos.setModel(modelo);
        Tema.estilizarTabla(tblObjetos);
        tblObjetos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblObjetos.getColumnModel().getColumn(0).setMaxWidth(60);
        tblObjetos.getColumnModel().getColumn(4).setMinWidth(105);
        tblObjetos.getColumnModel().getColumn(4).setMaxWidth(120);
        tblObjetos.getColumnModel().getColumn(6).setMinWidth(100);
        tblObjetos.getColumnModel().getColumn(6).setMaxWidth(120);
        scrollTabla.setBorder(BorderFactory.createLineBorder(Tema.BORDE));

        // Doble clic en una fila: abre el detalle
        tblObjetos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && getSeleccionado(false) != null) {
                    Ventanas.abrirSobre(PnlGestionObjetos.this, new FrmDetalleObjeto(getSeleccionado(false)), null);
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

    /** Devuelve el objeto de la fila seleccionada, o null si no hay ninguna. */
    private ObjetoPerdido getSeleccionado(boolean avisar) {
        int fila = tblObjetos.getSelectedRow();
        if (fila < 0) {
            if (avisar) {
                Mensajes.advertencia(this, "Primero seleccione un objeto de la tabla.");
            }
            return null;
        }
        return objetos.get(fila);
    }

    /**
     * Abre la ventana para crear (objeto = null) o editar. Al cerrarla se recarga la tabla:
     * así, si se canceló una edición, la tabla vuelve a mostrar los datos guardados.
     */
    private void abrirFormulario(ObjetoPerdido objeto) {
        Ventanas.abrirSobre(this, new FrmObjeto(objeto), this::recargar);
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
        Ventanas.abrirSobre(this, new FrmEntrega(objeto), this::recargar);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEntregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        scrollTabla = new javax.swing.JScrollPane();
        tblObjetos = new javax.swing.JTable();
        lblAyuda = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 247, 250));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(30, 58, 95));
        lblTitulo.setText("Gestión de objetos");

        btnNuevo.setBackground(new java.awt.Color(20, 184, 166));
        btnNuevo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setText("+ Nuevo objeto");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEditar.setBackground(new java.awt.Color(255, 255, 255));
        btnEditar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(31, 41, 55));
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEntregar.setBackground(new java.awt.Color(30, 58, 95));
        btnEntregar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEntregar.setForeground(new java.awt.Color(255, 255, 255));
        btnEntregar.setText("Registrar entrega");
        btnEntregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntregarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(220, 38, 38));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        lblBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblBuscar.setForeground(new java.awt.Color(31, 41, 55));
        lblBuscar.setText("Buscar:");

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(31, 41, 55));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        scrollTabla.setViewportView(tblObjetos);

        lblAyuda.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblAyuda.setForeground(new java.awt.Color(107, 114, 128));
        lblAyuda.setText("Seleccione una fila y use los botones. Doble clic para ver el detalle.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnEntregar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 20, Short.MAX_VALUE)
                            .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
                        .addComponent(lblAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(14, 14, 14)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEntregar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12, 12, 12)
                    .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                    .addGap(10, 10, 10)
                    .addComponent(lblAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        abrirFormulario(null);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEntregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntregarActionPerformed
        entregar();
    }//GEN-LAST:event_btnEntregarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        recargar();
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        recargar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEntregar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JLabel lblAyuda;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tblObjetos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
