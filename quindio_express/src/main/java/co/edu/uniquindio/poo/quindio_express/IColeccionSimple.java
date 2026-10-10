package co.edu.uniquindio.poo.quindio_express;

public interface IColeccionSimple<T> {
    void agregar(T elemento);
    boolean eliminar(T elemento);
    T obtener(int posicion);
    int tamano();
    boolean estaVacia();
}
