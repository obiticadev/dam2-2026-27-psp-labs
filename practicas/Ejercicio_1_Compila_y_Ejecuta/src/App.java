/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws Exception {
        ProcessBuilder pB1 = new ProcessBuilder("javac", "-d", "../Ejercicio_1_Llamada_a_YouTube/bin", "../Ejercicio_1_Llamada_a_YouTube/src/App.java");
        Process process1 = pB1.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process1.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        process1.waitFor();

        ProcessBuilder pB2 = new ProcessBuilder("jar", "cfe", "../Ejercicio_1_Llamada_a_YouTube/Application.jar", "App", "-C", "../Ejercicio_1_Llamada_a_YouTube/bin", ".");
        Process process2 = pB2.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process2.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        process2.waitFor();

        ProcessBuilder pB3 = new ProcessBuilder("java", "-jar", "../Ejercicio_1_Llamada_a_YouTube/Application.jar");
        Process process3 = pB3.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process3.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        process3.waitFor();

        ProcessBuilder pB4 = new ProcessBuilder("rm", "-f", "../Ejercicio_1_Llamada_a_YouTube/Application.jar");
        Process process4 = pB4.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process4.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        process4.waitFor();
        
    }
}

*/
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

    public class App {
    public static void main(String[] args) throws Exception {

        File dirYoutube = new File("../Ejercicio_1_Llamada_a_YouTube");

        // 1. Compilar
        ProcessBuilder pB1 = new ProcessBuilder("javac", "-d", "bin", "src/App.java");
        pB1.directory(dirYoutube);
        ejecutarProceso(pB1, "Compilación");

        // 2. Crear JAR
        ProcessBuilder pB2 = new ProcessBuilder("jar", "cfe", "Application.jar", "App", "-C", "bin", ".");
        pB2.directory(dirYoutube);
        ejecutarProceso(pB2, "Empaquetado JAR");

        // 3. Ejecutar JAR
        ProcessBuilder pB3 = new ProcessBuilder("java", "-jar", "Application.jar");
        pB3.directory(dirYoutube);
        ejecutarProceso(pB3, "Ejecución JAR");

        // 4. Limpiar JAR temporal
        ProcessBuilder pB4 = new ProcessBuilder("rm", "-f", "Application.jar");
        pB4.directory(dirYoutube);
        ejecutarProceso(pB4, "Limpieza");
    }

    private static void ejecutarProceso(ProcessBuilder pb, String etapa) throws Exception {

        // Redirige errores a la salida estándar para verlos si algo falla
        pb.redirectErrorStream(true);
        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int exitCode = p.waitFor();
        
        if (exitCode != 0) {
            System.err.println("❌ Falló en la etapa: " + etapa + " (código " + exitCode + ")");
        }
    }
}