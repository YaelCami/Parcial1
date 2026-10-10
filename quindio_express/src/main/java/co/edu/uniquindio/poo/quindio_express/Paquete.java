package co.edu.uniquindio.poo.quindio_express;

import java.util.Objects;

public class Paquete implements Comparable<Paquete>{
    private String codigo;
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


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Municipio getDestino() {
        return destino;
    }

    public void setDestino(Municipio destino) {
        this.destino = destino;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "codigo='" + codigo + '\'' +
                ", destino=" + destino +
                ", peso=" + peso +
                ", prioridad=" + prioridad +
                ", tiempoEstimado=" + tiempoEstimado +
                '}';
    }
}
