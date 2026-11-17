package objetosperdidos.model;

/** Estados posibles de un objeto. Solo se puede pasar de DISPONIBLE a ENTREGADO. */
public enum EstadoObjeto {
    DISPONIBLE("Disponible"),
    ENTREGADO("Entregado");

    private final String etiqueta;

    EstadoObjeto(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /** Texto que se muestra al usuario. */
    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
