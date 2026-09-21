/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleja
 */
public class Psp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            // linux    Process p2= Runtime.getRuntime().exec("firefox http://www.youtube.com");
            Process p = Runtime.getRuntime().exec("cmd /c start chrome " + args[0]);

        } catch (IOException ex) {
            Logger.getLogger(Psp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
