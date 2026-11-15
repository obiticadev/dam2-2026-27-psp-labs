import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Mover y renombrar: Padre. Plantilla para resolver. */
public class Padre {
    public static void main(String[] args) throws Exception {
        // TODO: lanza estudio.t05.Hijo con java -cp; configura ProcessBuilder con cada
        // argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.
        String rutaJava = System.getProperty("java.home") + "/bin/";
        String destino = "src\\moved.txt";
        ProcessBuilder pb = new ProcessBuilder(rutaJava + "java.exe", "src\\Hijo.java", destino);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
            System.out.println("Código de error: " + process.waitFor());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
