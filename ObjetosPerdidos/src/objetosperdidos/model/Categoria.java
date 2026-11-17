package objetosperdidos.model;

/** Clasificación de un objeto: tecnología, documentos, ropa, etc. */
public class Categoria {

    private int id;
    private String nombre;

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /** Las listas desplegables (JComboBox) muestran este texto. */
    @Override
    public String toString() {
        return nombre;
    }

    /** Dos categorías son iguales si tienen el mismo id. */
    @Override
    public boolean equals(Object otro) {
        return otro instanceof Categoria && ((Categoria) otro).id == this.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
