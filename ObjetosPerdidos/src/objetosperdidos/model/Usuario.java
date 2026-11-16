package objetosperdidos.model;

/**
 * Clase abstracta que reúne lo que tienen en común todos los usuarios.
 * No se pueden crear objetos "Usuario" directamente: siempre será un
 * Administrador o un Estudiante (herencia).
 * Cada subclase define sus propios permisos (polimorfismo).
 */
public abstract class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String codigoInstitucional;
    private String passwordHash;
    private String salt;
    private boolean activo = true;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    // ----- Métodos abstractos: cada tipo de usuario responde a su manera -----

    /** Rol del usuario. */
    public abstract Rol getRol();

    /** Indica si puede registrar, editar, eliminar y entregar objetos. */
    public abstract boolean puedeGestionarObjetos();

    /** Nombre del rol para mostrar en pantalla. */
    public abstract String getNombreRol();

    // ----- Getters y setters (encapsulamiento) -----

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCodigoInstitucional() {
        return codigoInstitucional;
    }

    public void setCodigoInstitucional(String codigoInstitucional) {
        this.codigoInstitucional = codigoInstitucional;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
