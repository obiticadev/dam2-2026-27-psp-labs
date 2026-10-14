package Interfaces;

import Clases.Huevo;

public interface Extractor extends Finalizador {

    Huevo extraer() throws InterruptedException;
}
