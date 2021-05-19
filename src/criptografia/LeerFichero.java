package criptografia;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;

/**
 * @author Felipe
 */
public class LeerFichero {

    /**
     * Método que se encarga de obtener una imagen.
     * @param path es la dirección donde está el archivo.
     * @return BufferedImage regresa la imágen.
     * @throws java.io.IOException
     */
    protected static BufferedImage obtenerImagen(String path) throws IOException {
        File archivo = new File(path);
        BufferedImage imagen = ImageIO.read(archivo);
        return imagen;
    }

    /**
     * Método que se encarga de procesar una imagen y se
     * determina si se cifra o de decifra.
     * @param archivo es el archivo a tratar.
     * @param esCifrado determina si se decifra o se cifra.
     * @return regresa los datos procesados.
     * @throws java.io.IOException
     */
    protected static String procesarImagen(String archivo, boolean esCifrado) throws Exception {
        File archivoNuevo = new File(archivo);
        String datos = "";
        if (esCifrado) {
            datos = cifrarImagen(archivoNuevo);
        } else {
            datos = decifrarImagen(archivoNuevo);        
        }
        return datos;
    }

    /**
     * Método que se encarga de cifrar una imagen.
     * @param archivo es el archivo a tratar.
     * @return regresa los datos procesados.
     * @throws java.io.IOException
     */
    protected static String cifrarImagen(File archivo) throws Exception {
        FileInputStream fileInputStreamReader = new FileInputStream(archivo);
        byte[] bytes = new byte[(int) archivo.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.encodeBase64(bytes), "UTF-8");
    }

    /**
     * Método que se encarga de decifrar una imagen.
     * @param archivo es el archivo a tratar.
     * @return regresa los datos procesados.
     * @throws java.io.IOException
     */
    protected static String decifrarImagen(File archivo) throws Exception {
        FileInputStream fileInputStreamReader = new FileInputStream(archivo);
        byte[] bytes = new byte[(int) archivo.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.decodeBase64(bytes), "UTF-8");
    }

    /**
     * Método que se encarga de leer y regresar datos de un archivo.
     * @param archivo es el archivo a tratar.
     * @param esCifrado determina si es cifrado o no.
     * @return regresa los datos leidos.
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected static String muestraContenido(String archivo, boolean esCifrado) 
            throws FileNotFoundException, IOException {
        String cadena;
        String contenido = "";
        String contenidoDos = "";
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while ((cadena = b.readLine()) != null) {
            if (esCifrado) {
                contenido += cadena + "\r\n";
            } else {
                contenido += cadena;
            }
        }
        b.close();
        if (esCifrado) {
            contenidoDos = contenido.substring(0, contenido.length() - 1);
            return contenidoDos;
        } else {
            return contenido;
        }
    }
}
