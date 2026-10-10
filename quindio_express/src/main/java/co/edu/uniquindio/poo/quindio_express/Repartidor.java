package co.edu.uniquindio.poo.quindio_express;

public class Repartidor {
    private final String id;
    private String nombre;
    private Municipio zona;
    private boolean disponible;

    public Repartidor(String id, String nombre, Municipio zona, boolean disponible) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID no puede ser vacío");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre no puede ser vacío");
        if (zona == null) throw new IllegalArgumentException("Zona no puede ser nula");
        this.id = id.trim();
        this.nombre = nombre.trim();
        this.zona = zona;
        this.disponible = disponible;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Municipio getZona() {
        return zona;
    }

    public void setZona(Municipio zona) {
        this.zona = zona;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Repartidor: " +
                ". ID: " + id + '\'' +
                ", Nombre: " + nombre + '\'' +
                ". Zona: " + zona +
                ". Disponible?: " + disponible;
    }
}
