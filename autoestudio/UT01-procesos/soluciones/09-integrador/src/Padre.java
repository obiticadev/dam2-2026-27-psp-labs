package estudio.t09;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Centro de operaciones: Padre. Referencia, no copiar sobre tu intento. */
public class Padre {
    public static void main(String[] args) throws Exception {
        String java = new File(System.getProperty("java.home"), "bin/java").getPath();
        // Un único directorio de clases, sin dependencias externas.
        String cp = new File(System.getProperty("java.class.path")).getAbsolutePath();
        String dato = args.length > 0 ? args[0] : ".";
        ProcessBuilder pb = new ProcessBuilder(java, "-cp", cp, "estudio.t09.Hijo", dato);
        pb.redirectErrorStream(true);
        Process hijo = pb.start();
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(hijo.getInputStream()))) {
            String linea;
            while ((linea = lector.readLine()) != null) System.out.println(linea);
        }
        int codigo = hijo.waitFor();
        System.out.println("PADRE:codigo=" + codigo);
        System.exit(codigo);
    }
}
