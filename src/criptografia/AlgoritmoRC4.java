package criptografia;

/**
 * @author Felipe
 */
public class AlgoritmoRC4 {

    // Se define el arreglo.
    private Integer[] arreglo = new Integer[256];
    
    /**
     * Método que se encarga de cifrar la información.
     * @param texto son los datos de entrada.
     * @param llave es la llave que se utiliza para cifrar.
     * @return textoCifrado es el texto cifrado.
    */
    protected String cifrar(String texto, String llave) {
        Character[] claveGenerada = new Character[texto.length()];
        StringBuffer textoCifrado = new StringBuffer();

        prga(arreglo, claveGenerada, texto, llave);
        for (int pos = 0; pos < texto.length(); ++pos) {
            textoCifrado.append((char) (texto.charAt(pos) ^ claveGenerada[pos]));
        }
        return textoCifrado.toString();
    }

    /**
     * Método que se encarga de llenar el arreglo con números.
     * @param arreglo es el arreglo a llenar.
    */
    private void asignarValoresArreglo(Integer[] arreglo) {
        for (int pos = 0; pos < arreglo.length; pos++) {
            arreglo[pos] = pos;
        }
    }

    /**
     * Método que se encarga de realizar el ksa.
     * @param arreglo es el arreglo a manejar.
     * @param llave es la llave que se utiliza para cifrar.
    */
    private void ksa(Integer[] arreglo, String llave) {
        int j = 0;
        for (int pos = 0; pos < arreglo.length; ++pos) {
            j = (j + arreglo[pos] + llave.charAt(pos % llave.length())) % 256;
            intercambio(arreglo, pos, j);
        }
    }

    /**
     * Método que se encarga de realizar el prga.
     * @param arreglo es el arreglo a manejar.
     * @param claveGenerada es la clave generada.
     * @param texto es el texto a tratar.
     * @param llave es la llave que se utiliza para cifrar.
    */
    private void prga(Integer[] arreglo, Character[] claveGenerada, String texto, String llave) {
        asignarValoresArreglo(arreglo);
        ksa(arreglo, llave);
        int i = 0;
        int j = 0;
        int pos = 0;
        while (pos < texto.length()) {
            i = (i + 1) % 256;
            j = (j + arreglo[i]) % 256;
            intercambio(arreglo, i, j);
            claveGenerada[pos] = (char) (arreglo[(arreglo[i] + arreglo[j]) % 256]).intValue();
            pos++;
        }
    }

    /**
     * Método que se encarga de realizar el intercambio.
     * @param arreglo es el arreglo a manejar.
     * @param i es la posición a cambiar.
     * @param j es la posición a cambiar.
    */
    private void intercambio(Integer[] arreglo, int i, int j) {
        Integer aux = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = aux;
    }
}
