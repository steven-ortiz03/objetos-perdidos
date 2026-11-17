package objetosperdidos.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utilidad para proteger contraseñas (RNF-02).
 * Nunca se guarda la contraseña: se guarda un hash PBKDF2WithHmacSHA256
 * (65536 iteraciones, clave de 256 bits) calculado con una sal aleatoria
 * diferente para cada usuario. Hash y sal se guardan en Base64, que es el
 * formato de las cuentas de data/datos_iniciales.sql.
 */
public class PasswordUtil {

    private static final int ITERACIONES = 65536;
    private static final int BITS_CLAVE = 256;

    /** Genera una sal aleatoria de 16 bytes en Base64. */
    public static String generarSalt() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /** Calcula el hash PBKDF2 de la contraseña con la sal indicada (ambos en Base64). */
    public static String hash(String password, String salt) {
        try {
            PBEKeySpec datos = new PBEKeySpec(password.toCharArray(),
                    Base64.getDecoder().decode(salt), ITERACIONES, BITS_CLAVE);
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return Base64.getEncoder().encodeToString(fabrica.generateSecret(datos).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Ambos existen en cualquier JDK moderno; este error no debería ocurrir
            throw new IllegalStateException("No se pudo calcular el hash de la contraseña", e);
        }
    }

    /** Compara la contraseña escrita con el hash guardado. */
    public static boolean verificar(String password, String salt, String hashGuardado) {
        byte[] calculado = hash(password, salt).getBytes(StandardCharsets.UTF_8);
        byte[] guardado = hashGuardado.getBytes(StandardCharsets.UTF_8);
        // Comparación de tiempo constante: no revela cuántos caracteres coincidieron
        return MessageDigest.isEqual(calculado, guardado);
    }
}
