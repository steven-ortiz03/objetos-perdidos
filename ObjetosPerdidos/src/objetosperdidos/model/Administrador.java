package objetosperdidos.model;

/** Funcionario institucional: gestiona los objetos y registra las entregas. */
public class Administrador extends Usuario {

    public Administrador() {
    }

    public Administrador(int id, String nombre, String correo) {
        super(id, nombre, correo);
    }

    @Override
    public Rol getRol() {
        return Rol.ADMINISTRADOR;
    }

    @Override
    public boolean puedeGestionarObjetos() {
        return true;
    }

    @Override
    public String getNombreRol() {
        return "Administrador";
    }
}
