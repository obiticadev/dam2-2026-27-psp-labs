import java.io.BufferedReader;
import java.io.InputStreamReader;

/** Explorar directorios: Padre. Plantilla para resolver. */
public class Padre {
    public static void main(String[] args) throws Exception {
        // TODO: lanza estudio.t02.Hijo con java -cp; configura ProcessBuilder con cada
        // argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.
        String java = System.getProperty("java.home") + "/bin/";

        String destino = args.length > 0 ? args[0] : ".";
        ProcessBuilder pb = new ProcessBuilder(java + "java.exe", "src/Hijo.java", destino);
        Process process = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
