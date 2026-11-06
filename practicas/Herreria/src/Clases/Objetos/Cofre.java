package Clases.Objetos;

import java.util.ArrayDeque;
import java.util.Deque;

import ClasesAbstractas.Arma;

public class Cofre {
    final int CAPACIDAD_HERREROS = 20;
    Deque<Arma> armas = new ArrayDeque<Arma>();
}
