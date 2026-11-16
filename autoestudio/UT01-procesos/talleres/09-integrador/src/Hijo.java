package estudio.t09;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Centro de operaciones: Hijo. Plantilla para resolver. */
public class Hijo {
    public static void main(String[] args) throws Exception {
        // Ensayo en Linux: misma estructura con ping -c, ls -1, find, cp, mv o cat (sin
        // cmd /c).
        // TODO: configura ProcessBuilder con cada argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.

        File carpeta = new File(args.length > 0 ? args[0] : ".");
        int codigo = ejecutar(carpeta, "cmd", "/c", "dir", "/s", "/b", "*.java");
        if (codigo != 0) {
            System.exit(codigo);
        }

    }

    private static int ejecutar(File carpeta, String... comando) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.directory(carpeta);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        }
        int codigo = process.waitFor();
        System.out.println("Código de la etapa " + codigo);
        return codigo;
    }
}
