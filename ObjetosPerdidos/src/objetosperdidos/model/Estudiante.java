package objetosperdidos.model;

/** Estudiante: solo consulta el catálogo. No puede modificar información. */
public class Estudiante extends Usuario {

    public Estudiante() {
    }

    public Estudiante(int id, String nombre, String correo) {
        super(id, nombre, correo);
    }

    @Override
    public Rol getRol() {
        return Rol.ESTUDIANTE;
    }

    @Override
    public boolean puedeGestionarObjetos() {
        return false;
    }

    @Override
    public String getNombreRol() {
        return "Estudiante";
    }
}
