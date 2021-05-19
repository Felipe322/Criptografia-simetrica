package criptografia;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Felipe
 */
public class GUICriptografiaSimetrica extends javax.swing.JFrame {

    /**
     * Se crea la GUI.
     */
    public GUICriptografiaSimetrica() {
        initComponents();
        this.setResizable(false);
        this.setTitle("Criptografía simétrica");
    }

     /**
     * Muestra un mensaje en la interfaz.
     */
    private void archivoGenerado() {
        JOptionPane.showMessageDialog(null, "Archivo generado correctamente.");
    }

    private boolean validarArchivo() {
        if (!jTFNombreArchivo.getText().isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Elija un archivo");
            return false;
        }
    }

    private boolean validarRadioButon() {
        boolean valido = true;
        if (jRBCifrar.isSelected() == false && jRBDecifrar.isSelected() == false) {
            JOptionPane.showMessageDialog(null, "Elija una opción Cifrar/Decifrar");
            valido = false;
        }

        return valido;
    }

    private boolean validarTamanoLlave() {
        String opcion = jCBAlgoritmos.getSelectedItem().toString();
        boolean valido = true;
        if (opcion.equals("RC4")) {
            if (jTFLlave.getText().length() < 5 || jTFLlave.getText().length() > 32) {
                JOptionPane.showMessageDialog(null,
                        "El algoritmo RC4 utiliza una llave entre 40 y 256 bits.\n"
                        + "Un total de 5-32 caracteres.", "Tamaño de llave erróneo", 1);
                valido = false;
            }
        } else if (opcion.equals("DES")) {
            if (jTFLlave.getText().length() != 8) {
                JOptionPane.showMessageDialog(null,
                        "El algoritmo DES utiliza una llave de 64 bits.\n"
                        + "Un total de 8 caracteres.", "Tamaño de llave erróneo", 1);
                valido = false;
            }
        } else if (opcion.equals("IDEA")) {
            if (jTFLlave.getText().length() != 16) {
                JOptionPane.showMessageDialog(null,
                        "El algoritmo IDEA utiliza una llave de 128 bits.\n"
                        + "Un total de 16 caracteres.", "Tamaño de llave erróneo", 1);
                valido = false;
            }
        } else if (opcion.equals("AES-128")) {
            if (jTFLlave.getText().length() != 16) {
                JOptionPane.showMessageDialog(null,
                        "El algoritmo AES-128 utiliza una llave de 128 bits.\n"
                        + "Un total de 16 caracteres.", "Tamaño de llave erróneo", 1);
                valido = false;
            }
        }

        return valido;
    }
    
    private boolean esImagen(String tipoArchivo) {
        if (tipoArchivo.contains(".jpg")
                || tipoArchivo.contains(".jpeg")
                || tipoArchivo.contains(".png")) {
            return true;
        }
        return false;
    }
    
    private void generarArchivoCifrado() {
        try {
            String ruta = archivoUtilizado.getAbsolutePath() + "-cif";
            String contenidoArchivo = LeerFichero.muestraContenido(archivoUtilizado.getAbsolutePath(), true);
            String contenido = algoritmoCifrar(jCBAlgoritmos.getSelectedItem().toString(), contenidoArchivo);//Llamada al método
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    private void generarArchivoDecifrado() {
        try {
            String ruta = archivoUtilizado.getAbsolutePath();
            String contenidoArchivo = LeerFichero.muestraContenido(archivoUtilizado.getAbsolutePath(), false);
            String contenido = algoritmoDecifrar(jCBAlgoritmos.getSelectedItem().toString(), contenidoArchivo);
            File fileNuevo = new File(ruta.replaceAll("-cif", "-desc"));

            // Si el archivo no existe es creado
            if (!fileNuevo.exists()) {
                fileNuevo.createNewFile();
            }

            FileWriter fw = new FileWriter(fileNuevo);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarArchivo() {
        JFileChooser selectorArchivos = new JFileChooser();
        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // indica cual fue la accion de usuario sobre el jfilechooser
        selectorArchivos.showOpenDialog(this);
        archivoUtilizado = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado

        jTFNombreArchivo.setText(archivoUtilizado.getName());
    }

    private void generarImagenCifrada() throws IOException, Exception {

        File file = new File(archivoUtilizado.getAbsolutePath() + "-cif");
        imagenSeleccionada = LeerFichero.obtenerImagen(archivoUtilizado.getAbsolutePath());
        String imagenContenido = LeerFichero.procesarImagen(archivoUtilizado.getAbsolutePath(), true);
        String imagenEncriptada = algoritmoCifrar(jCBAlgoritmos.getSelectedItem().toString(), imagenContenido);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(imagenEncriptada);
        bw.close();

    }

    private int radioButonSeleccionado() {
        if (jRBCifrar.isSelected()) {
            return 0;
        } else {
            return 1;
        }
    }

    private void generarImagenDecifrada() throws IOException, Exception {
        
        File file = new File(archivoUtilizado.getAbsolutePath());
        BufferedImage obtenerImagen = LeerFichero.obtenerImagen(archivoUtilizado.getAbsolutePath());
        String imagenContenido = LeerFichero.procesarImagen(archivoUtilizado.getAbsolutePath(), false);
        LeerFichero.cifrarImagen(file);
        ImageIO.write(imagenSeleccionada, "png", new File(archivoUtilizado.getAbsolutePath().replaceAll("-cif", "-desc")));
    }

    private String algoritmoCifrar(String algoritmo, String contenidoArchivo) throws IOException, Exception {
        String contenido = "";
        String llave = jTFLlave.getText().toString();

        if (algoritmo.equals("RC4")) {
            contenido = algoritmoRC4.cifrar(contenidoArchivo, llave);
        } else if (algoritmo.equals("DES")) {
            contenido = Base64.encodeBase64String(AlgoritmoDES.cifrar(contenidoArchivo.getBytes("UTF-8"), llave.getBytes("UTF-8")));
        } else if (algoritmo.equals("IDEA")) {
            contenido = Base64.encodeBase64String(AlgoritmoIDEA.cifrar(contenidoArchivo.getBytes("UTF-8"), llave.getBytes("UTF-8")));
        } else if (algoritmo.equals("AES-128")) {
            contenido = algoritmoAES.cifrar(contenidoArchivo, llave);
        }

        return contenido;

    }

    private String algoritmoDecifrar(String algoritmo, String contenidoArchivo) throws IOException, Exception {
        String contenido = "";
        String llave = jTFLlave.getText().toString();

        if (algoritmo.equals("RC4")) {
            contenido = algoritmoRC4.cifrar(contenidoArchivo, llave);
        } else if (algoritmo.equals("DES")) {
            contenido = new String(AlgoritmoDES.decifrar(Base64.decodeBase64(contenidoArchivo.getBytes("UTF-8")), llave.getBytes("UTF-8")));
        } else if (algoritmo.equals("IDEA")) {
            contenido = new String(AlgoritmoIDEA.decifrar(Base64.decodeBase64(contenidoArchivo.getBytes("UTF-8")), llave.getBytes("UTF-8")));
        } else if (algoritmo.equals("AES-128")) {
            contenido = algoritmoAES.decifrar(contenidoArchivo, llave);
        }

        return contenido;

    }

    /**
     * Método que se encarga de realizar las validaciones 
     * y ejecutar el cifrado o decifrado de los archivos.
    */
    private void ejecucion() throws Exception {
        if (validarTamanoLlave() && validarRadioButon() && validarArchivo()) {
            if (radioButonSeleccionado() == 0) {
                if (esImagen(jTFNombreArchivo.getText())) {
                    generarImagenCifrada();
                } else {
                    generarArchivoCifrado();
                }
            } else {
                if (esImagen(jTFNombreArchivo.getText())) {
                    generarImagenDecifrada();
                } else {
                    generarArchivoDecifrado();
                }
            }
            archivoGenerado();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jCBAlgoritmos = new javax.swing.JComboBox<>();
        jTFNombreArchivo = new javax.swing.JTextField();
        jBtnBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jRBDecifrar = new javax.swing.JRadioButton();
        jRBCifrar = new javax.swing.JRadioButton();
        jBtnEjecutar = new javax.swing.JButton();
        jTFLlave = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jCBAlgoritmos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RC4", "DES", "IDEA", "AES-128" }));
        jCBAlgoritmos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAlgoritmosActionPerformed(evt);
            }
        });

        jTFNombreArchivo.setEditable(false);
        jTFNombreArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNombreArchivoActionPerformed(evt);
            }
        });

        jBtnBuscar.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jBtnBuscar.setText("Buscar");
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 22)); // NOI18N
        jLabel1.setText("Llave");

        buttonGroup.add(jRBDecifrar);
        jRBDecifrar.setText("Decifrar");
        jRBDecifrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBDecifrarActionPerformed(evt);
            }
        });

        buttonGroup.add(jRBCifrar);
        jRBCifrar.setText("Cifrar");
        jRBCifrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBCifrarActionPerformed(evt);
            }
        });

        jBtnEjecutar.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jBtnEjecutar.setText("Ejecutar");
        jBtnEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEjecutarActionPerformed(evt);
            }
        });

        jTFLlave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFLlaveActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 22)); // NOI18N
        jLabel3.setText("Algoritmo");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 22)); // NOI18N
        jLabel4.setText("Archivo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jBtnBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(112, 112, 112))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jTFNombreArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(jCBAlgoritmos, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(64, 64, 64))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jRBCifrar)
                        .addGap(40, 40, 40)
                        .addComponent(jRBDecifrar)
                        .addGap(148, 148, 148))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jBtnEjecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(178, 178, 178))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTFLlave, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBAlgoritmos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFNombreArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBtnBuscar)
                        .addGap(99, 99, 99))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFLlave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRBDecifrar)
                    .addComponent(jRBCifrar))
                .addGap(46, 46, 46)
                .addComponent(jBtnEjecutar)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRBCifrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBCifrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRBCifrarActionPerformed

    private void jBtnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEjecutarActionPerformed
        try {
            ejecucion();
        } catch (Exception ex) {
            Logger.getLogger(GUICriptografiaSimetrica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnEjecutarActionPerformed

    private void jTFLlaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFLlaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFLlaveActionPerformed

    private void jBtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarActionPerformed
        buscarArchivo();
    }//GEN-LAST:event_jBtnBuscarActionPerformed

    private void jCBAlgoritmosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAlgoritmosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBAlgoritmosActionPerformed

    private void jTFNombreArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNombreArchivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNombreArchivoActionPerformed

    private void jRBDecifrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBDecifrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRBDecifrarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUICriptografiaSimetrica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUICriptografiaSimetrica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUICriptografiaSimetrica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUICriptografiaSimetrica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUICriptografiaSimetrica().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jBtnEjecutar;
    private javax.swing.JComboBox<String> jCBAlgoritmos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton jRBCifrar;
    private javax.swing.JRadioButton jRBDecifrar;
    private javax.swing.JTextField jTFLlave;
    private javax.swing.JTextField jTFNombreArchivo;
    // End of variables declaration//GEN-END:variables

    private AlgoritmoAES algoritmoAES = new AlgoritmoAES();
    private AlgoritmoDES algoritmoDES = new AlgoritmoDES();
    private AlgoritmoRC4 algoritmoRC4 = new AlgoritmoRC4();
    private AlgoritmoIDEA algoritmoIDEA = new AlgoritmoIDEA();
    private BufferedImage imagenSeleccionada;
    private File archivoUtilizado;
}
