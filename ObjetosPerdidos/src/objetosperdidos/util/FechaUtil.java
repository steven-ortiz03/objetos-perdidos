package objetosperdidos.util;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * Convierte un JSpinner del diseñador en un campo de fecha con formato día/mes/año.
 * Swing no trae un calendario, por eso se adapta un JSpinner y se trabaja con LocalDate.
 */
public class FechaUtil {

    /** Prepara el JSpinner para mostrar y editar fechas. Se llama una vez, después de initComponents(). */
    public static void configurar(JSpinner campo) {
        campo.setModel(new SpinnerDateModel());
        campo.setEditor(new JSpinner.DateEditor(campo, "dd/MM/yyyy"));
    }

    public static LocalDate leer(JSpinner campo) {
        try {
            campo.commitEdit(); // toma en cuenta lo que el usuario escribió a mano
        } catch (ParseException e) {
            // Si el texto no es una fecha válida se conserva el último valor correcto
        }
        Date fecha = (Date) campo.getValue();
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static void poner(JSpinner campo, LocalDate fecha) {
        campo.setValue(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
}
