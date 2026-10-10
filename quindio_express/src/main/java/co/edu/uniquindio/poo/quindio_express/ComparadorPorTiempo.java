package co.edu.uniquindio.poo.quindio_express;

import java.util.Comparator;

public class ComparadorPorTiempo implements Comparator<Paquete> {
    @Override
    public int compare(Paquete o1, Paquete o2) {
        return Integer.compare(o1.getTiempoEstimado(),  o2.getTiempoEstimado());
    }
}
