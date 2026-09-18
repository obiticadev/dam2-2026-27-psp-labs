import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws Exception {
        ProcessBuilder pB = new ProcessBuilder("java", "../Ejercicio_1_Llamada_a_YouTube/src/App.java");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(pB.start().getInputStream()));
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
