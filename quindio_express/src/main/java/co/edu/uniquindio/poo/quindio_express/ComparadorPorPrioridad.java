package co.edu.uniquindio.poo.quindio_express;

import java.util.Comparator;

public class ComparadorPorPrioridad implements Comparator<Paquete> {
    @Override
    public int compare(Paquete o1, Paquete o2) {
        int cmp = Integer.compare(o2.getPrioridad(), o1.getPrioridad());
        if (cmp != 0) return cmp;

        cmp = Integer.compare(o1.getTiempoEstimado(), o2.getTiempoEstimado());
        if (cmp != 0) return cmp;

        return o1.getCodigo().compareTo(o2.getCodigo());
    }
}
