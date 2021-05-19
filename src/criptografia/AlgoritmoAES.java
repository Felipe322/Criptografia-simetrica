package criptografia;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Felipe
 */
public class AlgoritmoAES {

    /**
     * Crea la clave de encriptacion usada internamente.
     * @param clave Clave que se usara para cifrar.
     * @return Clave de encriptación.
     */
    private SecretKeySpec crearClave(String clave) throws Exception {
        byte[] claveEncriptacion = clave.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        claveEncriptacion = sha.digest(claveEncriptacion);
        claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);
        SecretKeySpec llaveGenerada = new SecretKeySpec(claveEncriptacion, "AES");

        return llaveGenerada;
    }

    /**
     * Aplica la encriptacion AES a la cadena de texto usando la clave indicada.
     * @param datos Cadena a cifrar.
     * @param clave Clave para cifrar.
     * @return Información encriptada.
     * @throws java.lang.Exception
     */
    protected String cifrar(String datos, String clave) throws Exception {
        SecretKeySpec secretKey = crearClave(clave);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptados = datos.getBytes("UTF-8");
        byte[] bytesEncriptados = cipher.doFinal(datosEncriptados);
        String encriptado = Base64.getEncoder().encodeToString(bytesEncriptados);

        return encriptado;
    }

    /**
     * Desencripta la cadena de texto indicada usando la clave de encriptacion.
     * @param datosEncriptados Datos encriptados.
     * @param claveSecreta Clave de encriptacion.
     * @return Informacion desencriptada.
     * @throws java.lang.Exception
     */
    protected String decifrar(String datosEncriptados, String claveSecreta) throws Exception {
        SecretKeySpec secretKey = crearClave(claveSecreta);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] bytesEncriptados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
        String datos = new String(datosDesencriptados);

        return datos;
    }
}
