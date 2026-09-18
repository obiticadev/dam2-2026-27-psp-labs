import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws Exception {
        ProcessBuilder pB = new ProcessBuilder("bash", "-c", "ping -c 4 \"www.youtube.com\"");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(pB.start().getInputStream()));
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
