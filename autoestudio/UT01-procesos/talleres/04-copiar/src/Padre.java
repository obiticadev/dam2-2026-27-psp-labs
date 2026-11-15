import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Preparar una entrega: Padre. Plantilla para resolver. */
public class Padre {
    public static void main(String[] args) throws Exception {
        // TODO: lanza estudio.t04.Hijo con java -cp; configura ProcessBuilder con cada
        // argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.
        String java = System.getProperty("java.home") + "/bin/";

        ProcessBuilder pb = new ProcessBuilder(java + "java.exe", "src\\Hijo.java", "src\\copy.txt");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }

            System.out.println("\nCódigo de error: " + process.waitFor());
        }
    }
}
