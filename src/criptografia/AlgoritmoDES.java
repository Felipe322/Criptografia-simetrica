package criptografia;


import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author Felipe
 */
public class AlgoritmoDES {
    
    // Se define el algoritmo a utilizar.
    protected static final String KEY_ALGORITHM = "DES";
    
    // Se define el algorimo utilizando un padding.
    protected static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding ";

    /**
     * Método que se encarga de convertir bytes
     * a una Key del algorimo DES.
     * @param llave bytes de entrada.
     * @return llaveGenerada es la llave generada.
     */
    private static Key convertirALlave(byte[] llave) throws Exception {
        SecretKey llaveGenerada = new SecretKeySpec(llave, KEY_ALGORITHM);
        return llaveGenerada;
    }

    /**
     * Método que se encarga de encriptar la información.
     * @param datos son los datos a cifrar.
     * @param llave es la llave del cifrado.
     * @return byte[] regresa los datos encriptados.
     * @throws java.lang.Exception
     */
    protected static byte[] cifrar(byte[] datos, byte[] llave) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Key llaveGenerada = convertirALlave(llave);
        Cipher cifrado = Cipher.getInstance(CIPHER_ALGORITHM);
        cifrado.init(Cipher.ENCRYPT_MODE, llaveGenerada);
        return cifrado.doFinal(datos);
    }

    /**
     * Método que se encarga de decifrar la información.
     * @param datos son los datos a decifrar.
     * @param llave es la llave del decifrado.
     * @return byte[] regresa los datos decifrados.
     * @throws java.lang.Exception
     */
    protected static byte[] decifrar(byte[] datos, byte[] llave) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Key llaveGenerada = convertirALlave(llave);
        Cipher cifrado = Cipher.getInstance(CIPHER_ALGORITHM);
        cifrado.init(Cipher.DECRYPT_MODE, llaveGenerada,cifrado.getParameters());
        return cifrado.doFinal(datos);
    }        
}

