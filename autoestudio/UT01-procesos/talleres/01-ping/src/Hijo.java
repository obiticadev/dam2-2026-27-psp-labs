import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/** Diagnóstico de conexión: Hijo. Plantilla para resolver. */
public class Hijo {
    public static void main(String[] args) throws Exception {
        // Ensayo en Linux: misma estructura con ping -c, ls -1, find, cp, mv o cat (sin
        // cmd /c).
        // TODO: configura ProcessBuilder con cada argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.
        String destino = args.length > 0 ? args[0] : "localhost";
        ProcessBuilder pb = new ProcessBuilder("ping", "-n", "2", destino);
        Process proceso = pb.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
        String linea;
        ArrayList<String> lista = new ArrayList<>();
        while ((linea = br.readLine()) != null) {
            lista.add(linea);
        }
        for (String l : lista) {
            System.out.println(l);
        }
        int codigo = proceso.waitFor();
        System.out.println(codigo);
    }
}
