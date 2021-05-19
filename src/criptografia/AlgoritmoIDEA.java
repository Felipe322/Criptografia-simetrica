package criptografia;

import java.security.Key;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author Felipe
 */
public class AlgoritmoIDEA {

    // Se define el algoritmo a utilizar.
    protected static final String KEY_ALGORITHM = "IDEA";

    // Se define el algorimo utilizando un padding.
    protected static final String CIPHER_ALGORITHM = "IDEA/ECB/ISO10126Padding";

    /**
     * Método que se encarga de convertir bytes
     * a una Key del algorimo IDEA.
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
        cifrado.init(Cipher.DECRYPT_MODE, llaveGenerada);
        return cifrado.doFinal(datos);
    }
}
