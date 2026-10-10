package co.edu.uniquindio.poo.quindio_express;

import java.util.Comparator;

public class ComparadorPorPeso implements Comparator<Paquete> {
    @Override
    public int compare(Paquete o1, Paquete o2) {
        return Double.compare(o2.getPeso(), o1.getPeso());
    }
}
