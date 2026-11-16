package objetosperdidos.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Entrega conexiones a la base de datos SQLite (un solo archivo local).
 *
 * La base de datos es el archivo data/objetos_perdidos.db. Si no existe (primera
 * ejecución), se crea sola ejecutando los scripts de src/db: schema.sql (tablas),
 * datos_iniciales.sql (categorías, lugares y cuentas) y datos_prueba.sql (ejemplos).
 * Para volver a empezar de cero basta con borrar la carpeta data.
 */
public class ConexionBD {

    private static final String CARPETA = "data";
    private static final String ARCHIVO = CARPETA + "/objetos_perdidos.db";
    private static final String[] SCRIPTS = {"/db/schema.sql", "/db/datos_iniciales.sql", "/db/datos_prueba.sql"};

    private static boolean preparada = false;

    /**
     * Abre una conexión nueva. Quien la pide debe cerrarla;
     * por eso los DAO la usan dentro de un try-with-resources.
     */
    public static Connection getConexion() throws SQLException {
        prepararBaseDeDatos();
        return abrir();
    }

    /** Abre la conexión y activa las claves foráneas (SQLite las trae apagadas por defecto). */
    private static Connection abrir() throws SQLException {
        Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + ARCHIVO);
        try (Statement sentencia = conexion.createStatement()) {
            sentencia.execute("PRAGMA foreign_keys = ON");
        }
        return conexion;
    }

    /** Crea la carpeta y las tablas la primera vez que se usa la aplicación. */
    private static synchronized void prepararBaseDeDatos() throws SQLException {
        if (preparada) {
            return;
        }
        try {
            Files.createDirectories(Paths.get(CARPETA));
        } catch (IOException e) {
            throw new SQLException("No se pudo crear la carpeta \"" + CARPETA + "\": " + e.getMessage());
        }
        try (Connection conexion = abrir()) {
            if (!existeTablaUsuarios(conexion)) {
                ejecutarScripts(conexion);
            }
        }
        preparada = true;
    }

    private static boolean existeTablaUsuarios(Connection conexion) throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'usuarios'";
        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            return resultado.next();
        }
    }

    /** Ejecuta los tres scripts dentro de una transacción: o se crea todo o nada. */
    private static void ejecutarScripts(Connection conexion) throws SQLException {
        conexion.setAutoCommit(false);
        try (Statement sentencia = conexion.createStatement()) {
            for (String script : SCRIPTS) {
                for (String instruccion : dividir(leer(script))) {
                    sentencia.execute(instruccion);
                }
            }
            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    /** Lee un script SQL que está empaquetado dentro del proyecto (carpeta src/db). */
    private static String leer(String recurso) throws SQLException {
        try (InputStream entrada = ConexionBD.class.getResourceAsStream(recurso)) {
            if (entrada == null) {
                throw new SQLException("No se encontró el script " + recurso
                        + ". Haga Clean and Build para que se copie a la aplicación.");
            }
            return new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer el script " + recurso + ": " + e.getMessage());
        }
    }

    /**
     * Separa un script en instrucciones sueltas (el controlador ejecuta una por vez).
     * Ignora las líneas de comentario (--) y no corta los ; que estén dentro de un texto entre comillas.
     */
    private static java.util.List<String> dividir(String script) {
        StringBuilder limpio = new StringBuilder();
        for (String linea : script.split("\\R")) {
            if (!linea.trim().startsWith("--")) {
                limpio.append(linea).append('\n');
            }
        }
        java.util.List<String> instrucciones = new java.util.ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean dentroDeTexto = false;
        for (char c : limpio.toString().toCharArray()) {
            if (c == '\'') {
                dentroDeTexto = !dentroDeTexto;
            }
            if (c == ';' && !dentroDeTexto) {
                if (!actual.toString().trim().isEmpty()) {
                    instrucciones.add(actual.toString().trim());
                }
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        if (!actual.toString().trim().isEmpty()) {
            instrucciones.add(actual.toString().trim());
        }
        return instrucciones;
    }
}
