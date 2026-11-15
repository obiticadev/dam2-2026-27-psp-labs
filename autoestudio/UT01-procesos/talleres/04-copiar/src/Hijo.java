import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Preparar una entrega: Hijo. Plantilla para resolver. */
public class Hijo {
    public static void main(String[] args) throws Exception {
        // Ensayo en Linux: misma estructura con ping -c, ls -1, find, cp, mv o cat (sin
        // cmd /c).
        // TODO: configura ProcessBuilder con cada argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.

        String original = "original.txt";
        String destino = args.length > 0 ? args[0] : "copy.txt";
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "copy", "/y", original, destino);
        Process process = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        }
    }
}
