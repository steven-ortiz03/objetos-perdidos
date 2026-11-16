package objetosperdidos.view;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * Campo para elegir una fecha con formato día/mes/año.
 * Swing no trae un calendario, por eso se adapta un JSpinner y se
 * agregan métodos para trabajar con LocalDate.
 */
public class SelectorFecha extends JSpinner {

    public SelectorFecha() {
        super(new SpinnerDateModel());
        setEditor(new JSpinner.DateEditor(this, "dd/MM/yyyy"));
    }

    public LocalDate getFecha() {
        try {
            commitEdit(); // toma en cuenta lo que el usuario escribió a mano
        } catch (ParseException e) {
            // Si el texto no es una fecha válida se conserva el último valor correcto
        }
        Date fecha = (Date) getValue();
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setFecha(LocalDate fecha) {
        setValue(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
}
