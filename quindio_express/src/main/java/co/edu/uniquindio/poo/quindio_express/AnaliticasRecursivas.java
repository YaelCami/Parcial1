package co.edu.uniquindio.poo.quindio_express;

import co.edu.uniquindio.poo.quindio_express.Municipio;
import co.edu.uniquindio.poo.quindio_express.Paquete;

import java.util.List;

public class AnaliticasRecursivas {

    // 1. Peso total por municipio / Recursivo

    public double pesoTotalPorMunicipio(List<Paquete> lista, Municipio municipio){

        int indice = 0;

        return pesoTotalPorMunicipio(lista, municipio, indice);
    }

    public static double pesoTotalPorMunicipio(List<Paquete> lista, Municipio municipio, int indice) {
        // caso base
        if (indice >= lista.size()){
            return 0;
        }

        double actual = lista.get(indice).getDestino() == municipio ? lista.get(indice).getPeso() : 0;

        // caso recursivo
        return actual + pesoTotalPorMunicipio(lista, municipio, indice + 1);
    }

    // 2. Contar por prioridad mínima / Recursivo

    public int contarPorPrioridadMinima(List<Paquete> lista, int minPrioridad){

        int indice = 0;

        return contarPorPrioridadMinima(lista, minPrioridad, indice);
    }

    public static int contarPorPrioridadMinima(List<Paquete> lista, int minPrioridad, int indice) {

        //Caso Base
        if (indice >= lista.size()) {
            return 0;
        }

        int cuenta = lista.get(indice).getPrioridad() >= minPrioridad ? 1 : 0;

        // Caso Recursivo
        return cuenta + contarPorPrioridadMinima(lista, minPrioridad, indice + 1);
    }

    // 3. Búsqueda binaria recursiva - necesita lista ordenada por código

    public Paquete busquedaBinaria(List<Paquete> lista, String codigo){

        int izq = 0;
        int der = lista.size() - 1;

        return busquedaBinaria(lista, codigo, izq, der);
    }
    public static Paquete busquedaBinaria(List<Paquete> ordenados, String codigo, int izq, int der) {

        // Caso ecexión
        if (izq > der) {
            System.out.println("Paquete no encontrado.");
            return null;
        }

        int medio = (izq + der) / 2;

        String codigoMedio = ordenados.get(medio).getCodigo();

        // Caso Base
        if (codigoMedio.equals(codigo)){
            return ordenados.get(medio);
        }

        // Casos Recursivos
        if (codigo.compareTo(codigoMedio) < 0) {
            return busquedaBinaria(ordenados, codigo, izq, medio - 1);
        }
        else {
            return busquedaBinaria(ordenados, codigo, medio + 1, der);
        }
    }

    // 4. Paquete más pesado con Divide y Vencerás

    // MÉTODO QUE LLAMAS DESDE EL MAIN - FALTABA ESTE
    public static Paquete masPesadoDyV(List<Paquete> lista) {
        if (lista == null || lista.isEmpty()) {
            return null; // no se cae si está vacía
        }
        return masPesadoDyV(lista, 0, lista.size() - 1);
    }

    // TU MÉTODO PERO CON VALIDACIÓN EXTRA
    public static Paquete masPesadoDyV(List<Paquete> lista, int izq, int der) {
        if (lista == null || lista.isEmpty() || izq > der) {
            return null;
        }
        // Caso Base
        if (izq == der) {
            return lista.get(izq);
        }

        int mitad = (izq + der) / 2;

        Paquete izqMax = masPesadoDyV(lista, izq, mitad);
        Paquete derMax = masPesadoDyV(lista, mitad + 1, der);

        // por si alguna mitad viene nula
        if (izqMax == null) return derMax;
        if (derMax == null) return izqMax;

        return (izqMax.getPeso() >= derMax.getPeso()) ? izqMax : derMax;
    }
}
