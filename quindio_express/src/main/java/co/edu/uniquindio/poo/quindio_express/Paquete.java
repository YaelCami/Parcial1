package co.edu.uniquindio.poo.quindio_express;

import java.util.Objects;

public class Paquete implements Comparable<Paquete>{
    private final String codigo; // NOTA JUANMA: Puse el codigo como final ya que el codigo es muy sensible, y que cambie puede ser peligroso
    private Municipio destino;
    private double peso;
    private int prioridad;
    private int tiempoEstimado;

    public Paquete(String codigo, Municipio destino, double peso, int prioridad, int tiempoEstimado) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Código no puede ser nulo/vacío");
        if (destino == null) throw new IllegalArgumentException("Destino no puede ser nulo");
        if (peso <= 0) throw new IllegalArgumentException("Peso debe ser > 0");
        if (prioridad < 1 || prioridad > 5) throw new IllegalArgumentException("Prioridad debe estar entre 1 y 5");
        if (tiempoEstimado <= 0) throw new IllegalArgumentException("Tiempo estimado debe ser > 0");

        this.codigo = codigo.trim().toUpperCase();
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    public Paquete(String codigo, Municipio destino, double peso, int tiempoEstimado) {
        this(codigo, destino, peso, 3, tiempoEstimado);
    }



    @Override
    public int compareTo(Paquete p){
        return this.codigo.compareTo(p.codigo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete)) return false;
        return codigo.equals(((Paquete)o).codigo);
    }

    @Override
    public int hashCode() { return Objects.hash(codigo); }

    /* NOTA JUANMA: Tambien quite los setters, ya que ellos no validan, al quitar los setters dejamos el
    paquete inmutable
     */

    public String getCodigo() {
        return codigo;
    }

    public Municipio getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    @Override
    public String toString() {
        return "Paquete: " +
                "Codigo: '" + codigo + '\'' +
                ". Destino: " + destino +
                ". Peso: " + peso +
                ". Prioridad: " + prioridad +
                ". Tiempo Estimado: " + tiempoEstimado;
    }
}
