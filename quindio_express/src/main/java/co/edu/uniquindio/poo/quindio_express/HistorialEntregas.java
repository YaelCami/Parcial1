package co.edu.uniquindio.poo.quindio_express;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HistorialEntregas<T> implements IColeccionSimple<T> {
    private Nodo<T> inicio;
    private int tam;

    public HistorialEntregas() {
        inicio = null;
        tam = 0;
    }

    @Override
    public void agregar(T dato){
        Nodo<T> nuevo = new Nodo<>(dato);
        if(tam == 0 && inicio == null){
            inicio = nuevo;
        }else{
            Nodo<T> aux = inicio;
            while (aux.getSiguiente() != null){
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
        tam++;
    }

    @Override
    public boolean eliminar(T datoBuscar){
        if (inicio == null) return false; // vacía

        // eliminar primer elemento
        if (inicio.getDato().equals(datoBuscar)){
            inicio = inicio.getSiguiente();
            tam--;
            return true;
        }

        // eliminar intermedio o último
        Nodo<T> aux = inicio;
        while (aux.getSiguiente() != null && !aux.getSiguiente().getDato().equals(datoBuscar)){
            aux = aux.getSiguiente();
        }

        if (aux.getSiguiente() == null) return false; // inexistente

        aux.setSiguiente(aux.getSiguiente().getSiguiente());
        tam--;
        return true;
    }

    @Override
    public T obtener(int posicion){
        if (posicion < 0 || posicion >= tam) throw new IndexOutOfBoundsException();
        Nodo<T> aux = inicio;
        for (int i = 0; i < posicion; i++){
            aux = aux.getSiguiente();
        }
        return aux.getDato();
    }
    @Override
    public int tamano(){ return tam; }

    @Override
    public boolean estaVacia(){ return tam == 0; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = inicio;
            @Override public boolean hasNext() { return actual != null; }
            @Override public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }


}
