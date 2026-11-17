package objetosperdidos.controller;

import java.sql.SQLException;
import objetosperdidos.dao.UsuarioDAO;
import objetosperdidos.model.Estudiante;
import objetosperdidos.model.Usuario;
import objetosperdidos.util.PasswordUtil;
import objetosperdidos.util.Sesion;
import objetosperdidos.util.ValidacionException;
import objetosperdidos.util.Validador;

/** Lógica de inicio de sesión, registro de estudiantes y cierre de sesión. */
public class AuthController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** CU-01: valida las credenciales y abre la sesión (RF-01). */
    public Usuario iniciarSesion(String correo, String password) throws ValidacionException, SQLException {
        if (Validador.estaVacio(correo) || Validador.estaVacio(password)) {
            throw new ValidacionException("Ingrese su correo y su contraseña.");
        }
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo.trim().toLowerCase());

        // Mensaje genérico: no se indica si falló el correo o la contraseña
        if (usuario == null
                || !PasswordUtil.verificar(password, usuario.getSalt(), usuario.getPasswordHash())) {
            throw new ValidacionException("Correo o contraseña incorrectos.");
        }
        if (!usuario.isActivo()) {
            throw new ValidacionException("La cuenta está inactiva y no puede ingresar.");
        }
        Sesion.iniciar(usuario);
        return usuario;
    }

    /** CU-02: registra una cuenta nueva, siempre con rol estudiante (RF-02). */
    public void registrarEstudiante(String nombre, String cedula, String correo,
                                    String password, String confirmacion)
            throws ValidacionException, SQLException {
        Validador.exigir(nombre, "Nombre completo");
        Validador.exigir(cedula, "Cédula");
        Validador.exigir(correo, "Correo");
        Validador.exigir(password, "Contraseña");
        Validador.exigirMaximo(nombre, 100, "Nombre completo");
                Validador.exigirMaximo(correo, 120, "Correo");

        if (!Validador.esCedulaValida(cedula.trim())) {
            throw new ValidacionException("La cédula debe tener solo números (entre 6 y 10 dígitos).");
        }

        String correoLimpio = correo.trim().toLowerCase();
        if (!Validador.esCorreoValido(correoLimpio)) {
            throw new ValidacionException("El correo no tiene un formato válido.");
        }
        if (password.length() < 6) {
            throw new ValidacionException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (!password.equals(confirmacion)) {
            throw new ValidacionException("Las contraseñas no coinciden.");
        }
        if (usuarioDAO.buscarPorCorreo(correoLimpio) != null) {
            throw new ValidacionException("Ya existe una cuenta registrada con ese correo.");
        }
        if (usuarioDAO.existeCedula(cedula.trim())) {
            throw new ValidacionException("Ya existe una cuenta registrada con esa cédula.");
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(nombre.trim());
        estudiante.setCedula(cedula.trim());
        estudiante.setCorreo(correoLimpio);
        String salt = PasswordUtil.generarSalt();
        estudiante.setSalt(salt);
        estudiante.setPasswordHash(PasswordUtil.hash(password, salt));
        usuarioDAO.insertar(estudiante);
    }

    /** CU-11: cierra la sesión actual (RF-03). */
    public void cerrarSesion() {
        Sesion.cerrar();
    }
}
