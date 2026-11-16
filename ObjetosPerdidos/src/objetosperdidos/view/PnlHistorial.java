package objetosperdidos.view;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import objetosperdidos.controller.EntregaController;
import objetosperdidos.model.Entrega;
import objetosperdidos.util.Tema;

/** Historial de objetos entregados, solo para administradores (CU-10). */
public class PnlHistorial extends JPanel implements Recargable {

    private final EntregaController entregaController = new EntregaController();
    private final DefaultTableModel modelo;
    private final JLabel lblTotal = new JLabel(" ");

    public PnlHistorial() {
        setLayout(new BorderLayout(0, 12));
        setBackground(Tema.FONDO);
        setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(Tema.titulo("Historial de entregas"), BorderLayout.WEST);
        lblTotal.setForeground(Tema.GRIS);
        norte.add(lblTotal, BorderLayout.EAST);
        add(norte, BorderLayout.NORTH);

        String[] columnas = {"Objeto", "Recibió", "Documento", "Fecha de entrega", "Entregó", "Observaciones"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        JTable tabla = new JTable(modelo);
        Tema.estilizarTabla(tabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        add(scroll, BorderLayout.CENTER);
    }

    @Override
    public void recargar() {
        try {
            List<Entrega> entregas = entregaController.listarHistorial();
            modelo.setRowCount(0);
            for (Entrega entrega : entregas) {
                modelo.addRow(new Object[]{
                    entrega.getNombreObjeto(),
                    entrega.getNombreReceptor(),
                    entrega.getDocumentoReceptor(),
                    entrega.getFechaEntrega().format(Tema.FECHA),
                    entrega.getNombreFuncionario(),
                    entrega.getObservaciones()
                });
            }
            lblTotal.setText(entregas.size() + " entrega(s) registrada(s)");
        } catch (Exception error) {
            Mensajes.error(this, error);
        }
    }
}
