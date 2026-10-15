package Clases.Animales;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Clases.Granjero;
import Clases.Huevera;
import Clases.Huevo;
import Interfaces.Extractor;

public class Buitre implements Extractor, Runnable {

    private final Granjero granjero;
    private final int CAPACIDAD_ESTOMAGO = 200;
    private int estomagoActual = 0;
    private List<Huevo> listaAnimales;

    public Buitre(Granjero granjero) {
        this.granjero = granjero;
        listaAnimales = new ArrayList<>();
    }

    public String comer(Huevo huevo) throws InterruptedException {
        estomagoActual += huevo.getPeso();
        listaAnimales.add(huevo);
        if (estomagoActual > CAPACIDAD_ESTOMAGO) {
            Map<String, Long> mapaDeAnimales = listaAnimales.stream()
                    .collect(Collectors.groupingBy(Huevo::getNombrePadre, Collectors.counting()));
            String eco = IntStream.range(0, listaAnimales.size())
                    .mapToObj(i -> String.format(
                            "\tEn la posición %d: %s, huevo de %d gramos",
                            i + 1,
                            listaAnimales.get(i).getNombrePadre(),
                            listaAnimales.get(i).getPeso()))
                    .collect(Collectors.joining("\n"));
            String resumen = mapaDeAnimales.entrySet().stream()
                    .map(entrada -> String.format("\t%s: %d huevos", entrada.getKey(), entrada.getValue()))
                    .collect(Collectors.joining("\n"));
            return String.format(
                    "Ya me voy, esto es todo lo que me he comido ->%n\t---DESGLOSE---%n%s%n%s%n\tCon un total de %d",
                    eco, resumen, estomagoActual);
        }
        return String.format(
                "Me he comido un huevo de %s con %d gramos, llevo un total de %d gramos de huevos. Seguiré comiendo",
                huevo.getNombrePadre(), huevo.getPeso(), estomagoActual);
    }

    @Override
    public Huevo extraer() throws InterruptedException {
        return Huevera.retirarHuevo();
    }

    @Override
    public Duration getEspera() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEspera'");
    }

    @Override
    public boolean condicionDeFinalizacion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'condicionDeFinalizacion'");
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
