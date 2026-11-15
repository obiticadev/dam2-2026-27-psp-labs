package estudio.t01;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Diagnóstico de conexión: Padre. Plantilla para resolver. */
public class Padre {
    public static void main(String[] args) throws Exception {
        // TODO: lanza estudio.t01.Hijo con java -cp; configura ProcessBuilder con cada
        // argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.
        String destino = args.length > 0 ? args[0] : "localhost";
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "talleres/01-ping/bin", "estudio.t01.Hijo", destino);
        Process proceso = pb.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
        System.out.println(proceso.waitFor());

    }
}
