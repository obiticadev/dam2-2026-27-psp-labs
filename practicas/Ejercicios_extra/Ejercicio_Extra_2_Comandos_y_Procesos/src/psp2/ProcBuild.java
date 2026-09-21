/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleja
 */
public class ProcBuild {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ProcessBuilder pb = new ProcessBuilder("CMD", "/C", "DIR");
        Process p = null;

        String linea;
        try {

            p = pb.start();

        } catch (IOException ex) {
            Logger.getLogger(PspDir.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);

            }
        } catch (IOException ex) {
            Logger.getLogger(PspDir.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        
        ///Saber variables de entorno
        System.out.println(System.getenv());
        //SS.OO
        System.out.println(System.getProperty("os.name"));
        
    }

}
