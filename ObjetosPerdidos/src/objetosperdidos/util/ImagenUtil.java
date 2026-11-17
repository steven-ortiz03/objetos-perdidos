package objetosperdidos.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Manejo de las fotografías de los objetos (RF-06, RNF-12).
 * Las fotos se copian a la carpeta "imagenes" del proyecto con un nombre único
 * y en la base de datos solo se guarda la ruta relativa.
 */
public class ImagenUtil {

    public static final String CARPETA = "imagenes";
    private static final long TAMANO_MAXIMO = 5L * 1024 * 1024; // 5 MB

    /** Revisa que el archivo sea una imagen JPG o PNG válida de máximo 5 MB. */
    public static void validar(File archivo) throws ValidacionException {
        String nombre = archivo.getName().toLowerCase();
        if (!(nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png"))) {
            throw new ValidacionException("La fotografía debe ser un archivo JPG o PNG.");
        }
        if (archivo.length() > TAMANO_MAXIMO) {
            throw new ValidacionException("La fotografía supera el tamaño máximo de 5 MB.");
        }
        try {
            if (ImageIO.read(archivo) == null) {
                throw new ValidacionException("El archivo seleccionado no es una imagen válida.");
            }
        } catch (IOException e) {
            throw new ValidacionException("No se pudo leer la imagen seleccionada.");
        }
    }

    /**
     * Copia la imagen a la carpeta "imagenes" con un nombre único (UUID)
     * y devuelve la ruta relativa que se guarda en la base de datos.
     */
    public static String guardar(File archivo) throws IOException {
        String nombre = archivo.getName();
        String extension = nombre.substring(nombre.lastIndexOf('.')).toLowerCase();
        String nuevoNombre = UUID.randomUUID() + extension;

        Path carpeta = Paths.get(CARPETA);
        Files.createDirectories(carpeta);
        Files.copy(archivo.toPath(), carpeta.resolve(nuevoNombre), StandardCopyOption.REPLACE_EXISTING);
        return CARPETA + "/" + nuevoNombre;
    }

    /** Borra el archivo de una fotografía. Si no existe, no hace nada. */
    public static void eliminar(String ruta) {
        if (ruta == null || ruta.isEmpty()) {
            return;
        }
        try {
            Files.deleteIfExists(Paths.get(ruta));
        } catch (IOException e) {
            System.err.println("No se pudo borrar la imagen: " + ruta);
        }
    }

    /**
     * Carga una fotografía escalada para que quepa en el tamaño indicado
     * sin deformarse. Si no hay foto, devuelve una imagen gris "Sin fotografía".
     */
    public static ImageIcon cargar(String ruta, int ancho, int alto) {
        BufferedImage original = null;
        if (ruta != null && !ruta.isEmpty()) {
            try {
                File archivo = new File(ruta);
                if (archivo.exists()) {
                    original = ImageIO.read(archivo);
                }
            } catch (IOException e) {
                original = null;
            }
        }
        if (original == null) {
            return new ImageIcon(crearImagenVacia(ancho, alto));
        }
        // Se calcula la escala para conservar la proporción de la foto
        double escala = Math.min((double) ancho / original.getWidth(),
                (double) alto / original.getHeight());
        int nuevoAncho = Math.max(1, (int) (original.getWidth() * escala));
        int nuevoAlto = Math.max(1, (int) (original.getHeight() * escala));
        Image escalada = original.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }

    /** Dibuja un rectángulo gris con el texto "Sin fotografía". */
    private static BufferedImage crearImagenVacia(int ancho, int alto) {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(0xE5E7EB));
        g.fillRect(0, 0, ancho, alto);
        g.setColor(new Color(0x6B7280));
        g.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String texto = "Sin fotografía";
        FontMetrics medidas = g.getFontMetrics();
        g.drawString(texto, (ancho - medidas.stringWidth(texto)) / 2, alto / 2);
        g.dispose();
        return imagen;
    }
}
