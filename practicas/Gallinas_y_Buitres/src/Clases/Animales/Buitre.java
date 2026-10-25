package Clases.Animales;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Clases.Granjero;
import Clases.Huevera;
import Clases.Huevo;
import Interfaces.Extractor;

public class Buitre implements Extractor, Runnable {

    private final int CAPACIDAD_ESTOMAGO = 200;
    private final Duration ESPERA_ENTRE_HUEVOS = Duration.ofSeconds(1);
    private int estomagoActual = 0;
    private List<Huevo> listaAnimales;

    public Buitre() {
        listaAnimales = new ArrayList<>();
    }

    @Override
    public Huevo extraer() throws InterruptedException {
        return Huevera.retirarHuevo();
    }

    public Huevo comer(Huevo huevo) throws InterruptedException {
        estomagoActual += huevo.getPeso();
        listaAnimales.add(huevo);
        return huevo;
    }

    private String eco(Huevo huevo) throws InterruptedException {
        return String.format(
                "Me he comido un huevo de %s con %d gramos, llevo un total de %d gramos de huevos. Seguiré comiendo",
                huevo.getNombrePadre(), huevo.getPeso(), estomagoActual);
    }

    private String ecoLleno() throws InterruptedException {
        String eco = IntStream.range(0, listaAnimales.size())
                .mapToObj(i -> String.format(
                        "\tEn la posición %d: %s, huevo de %d gramos",
                        i + 1,
                        listaAnimales.get(i).getNombrePadre(),
                        listaAnimales.get(i).getPeso()))
                .collect(Collectors.joining("\n"));

        Map<String, Long> mapaDeAnimales = listaAnimales.stream()
                .collect(Collectors.groupingBy(Huevo::getNombrePadre, Collectors.counting()));

        String resumen = mapaDeAnimales.entrySet().stream()
                .map(entrada -> String.format("\t%s: %d huevos", entrada.getKey(), entrada.getValue()))
                .collect(Collectors.joining("\n"));

        return String.format(
                "Ya me voy, esto es todo lo que me he comido ->%n\t---DESGLOSE---%n%s%n%s%n\tCon un total de %d",
                eco, resumen, estomagoActual);
    }

    @Override
    public Duration getEspera() {
        return this.ESPERA_ENTRE_HUEVOS;
    }

    @Override
    public boolean condicionDeFinalizacion() {
        return Granjero.isDespierto() || estomagoActual > CAPACIDAD_ESTOMAGO;
    }

    @Override
    public void run() {
        try {
            Granjero.lockGranjero.lock();
            while (!condicionDeFinalizacion()) {
                Granjero.lockGranjero.unlock();
                Huevo huevo = comer(extraer());
                if (huevo != null) {
                    if (estomagoActual <= CAPACIDAD_ESTOMAGO) {
                        System.out.println(eco(huevo));

                    } else {
                        System.out.println(ecoLleno());
                    }
                }
                Thread.sleep(getEspera());
                Granjero.lockGranjero.lock();
            }
            Granjero.lockGranjero.unlock();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
