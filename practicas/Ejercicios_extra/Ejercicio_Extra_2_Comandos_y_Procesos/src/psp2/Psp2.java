/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleja
 */
public class Psp2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            String cadena="www.youtube.com";
            Process p= Runtime.getRuntime().exec("java -jar Psp.jar " +cadena);
        } catch (IOException ex) {
            Logger.getLogger(Psp2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
