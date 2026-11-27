import Clases.Refrigerador;
import Clases.Repartidor;

public class Main {
    public static void main(String[] args) throws Exception {
        Repartidor repartidor = new Repartidor();
        Refrigerador refrigerador = new Refrigerador();

        repartidor.cargarCamion();
        repartidor.repartirEn(refrigerador);

        /*
         * 5 pizzeros
         * 1 repartidor llena el frigorífico de ingredientes por la mañana
         * 500 ingredientes como máximo caben en el frogorífico
         * 10 clientes (si es posible consumirán una pizza)
         * 
         * cada pizzero recoge de 1 en 1 los ingredientes hasta 5 para una pizza
         * las sobras finales tambien se juntan en una pizza
         * 
         * en 30s el repartidor retira todos los ingredientes en su camión (max500)
         */

    }
}
