import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Java que lanza Java: Padre. Plantilla para resolver. */
public class Padre {
    public static void main(String[] args) throws Exception {
        // TODO: lanza estudio.t07.Hijo con java -cp; configura ProcessBuilder con cada
        // argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.
        String bin = System.getProperty("java.home") + "/bin/";
        String nombre = "Hola Paloma";
        ProcessBuilder pb = new ProcessBuilder(bin + "java.exe", "src\\Hijo.java", nombre);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
            int codigo = process.waitFor();
            System.out.println("Error código: " + codigo);
            System.exit(codigo);
        }
    }
}
