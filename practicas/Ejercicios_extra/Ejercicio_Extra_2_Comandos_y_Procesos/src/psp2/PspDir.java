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
public class PspDir {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Process p = null;
        String linea;
        try {

            p = Runtime.getRuntime().exec("cmd /c dir");

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

    }

}
