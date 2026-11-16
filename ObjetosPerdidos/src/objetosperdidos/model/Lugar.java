package objetosperdidos.model;

/**
 * Sitio de la institución. Se usa tanto para el lugar donde se encontró
 * el objeto como para la dependencia donde queda guardado.
 */
public class Lugar {

    private int id;
    private String nombre;

    public Lugar(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Lugar && ((Lugar) otro).id == this.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
