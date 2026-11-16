import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Java que lanza Java: Hijo. Plantilla para resolver. */
public class Hijo {
    public static void main(String[] args) throws Exception {
        // TODO: configura ProcessBuilder con cada argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.
        String bin = System.getProperty("java.home") + "/bin/";

        String nombre = args.length > 0 ? args[0] : "Ada Lovelace";
        ProcessBuilder pb = new ProcessBuilder(bin + "java.exe", "src\\Eco.java", nombre);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
            int codigo = process.waitFor();
            System.out.println("Error Hijo: " + codigo);
            System.exit(codigo);
        }
    }
}
