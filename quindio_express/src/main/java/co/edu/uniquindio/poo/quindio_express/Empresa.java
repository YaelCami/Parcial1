package co.edu.uniquindio.poo.quindio_express;

import java.util.*;

public class Empresa {
    private LinkedHashMap<String, Paquete> paquetesEnOrdenLlegada;

    // Necesidad 2 y 7: consulta rápida por código O(1)
    private HashMap<String, Paquete> paquetesPorCodigo;

    // Necesidad 3: municipios sin duplicados
    private HashSet<Municipio> municipiosUnicos;

    // Necesidad 4: municipios ordenados alfabéticamente
    private TreeSet<Municipio> municipiosOrdenados;

    // Agrupación por destino
    private HashMap<Municipio, List<Paquete>> paquetesPorMunicipio;

    // Necesidad: atención por orden de llegada - FIFO
    private Queue<Paquete> colaLlegada;

    // Necesidad: despacho prioritario - prioridad DESC, tiempo ASC, código ASC
    private PriorityQueue<Paquete> colaPrioridad;

    // Necesidad 6: historial con TU lista propia
    private IColeccionSimple<Paquete> historialEntregas;

    // Repartidores
    private HashMap<String, Repartidor> repartidoresPorId;

    public Empresa(){
        paquetesEnOrdenLlegada = new LinkedHashMap<>();
        paquetesPorCodigo = new HashMap<>();
        municipiosUnicos = new HashSet<>();
        municipiosOrdenados = new TreeSet<>();
        paquetesPorMunicipio = new HashMap<>();
        colaLlegada = new LinkedList<>();
        colaPrioridad = new PriorityQueue<>();
        repartidoresPorId = new HashMap<>();
    }
    public boolean registrarPaquete(Paquete p) {
        if (paquetesPorCodigo.containsKey(p.getCodigo())) {
            System.out.println("Error: código duplicado " + p.getCodigo());
            return false;
        }
        paquetesEnOrdenLlegada.put(p.getCodigo(), p);
        paquetesPorCodigo.put(p.getCodigo(), p);
        municipiosUnicos.add(p.getDestino());
        municipiosOrdenados.add(p.getDestino());
        paquetesPorMunicipio.computeIfAbsent(p.getDestino(), k -> new ArrayList<>()).add(p);
        colaLlegada.add(p);
        colaPrioridad.add(p);
        return true;
    }
    public boolean registrarRepartidor(Repartidor r) {
        // Regla: No pueden existir dos repartidores con la misma identificación
        if (repartidoresPorId.containsKey(r.getId())) {
            System.out.println("Error: repartidor duplicado " + r.getId());
            return false;
        }
        repartidoresPorId.put(r.getId(), r);
        return true;
    }

    // --- CONSULTAS ---

    public Paquete consultarPorCodigo(String codigo) {
        return paquetesPorCodigo.get(codigo); // O(1) - esto es lo que evalúan
    }

    public Repartidor consultarRepartidor(String id) {
        return repartidoresPorId.get(id);
    }
    // --- ORDEN DE LLEGADA Y PRIORIDAD ---

    public Paquete atenderPorOrdenLlegada() {
        // Respeta orden original de registro - FIFO
        Paquete p = colaLlegada.poll();
        if (p != null) colaPrioridad.remove(p);
        return p;
    }

    public Paquete despacharPorPrioridad() {
        // Mayor prioridad, menor tiempo, código alfabético
        Paquete p = colaPrioridad.poll();
        if (p != null) colaLlegada.remove(p);
        return p;
    }

    // --- ASIGNACIÓN A REPARTIDOR CON REGLAS ---

    public boolean asignarPaqueteARepartidor(String codigoPaquete, String idRepartidor) {
        Paquete p = consultarPorCodigo(codigoPaquete);
        Repartidor r = consultarRepartidor(idRepartidor);

        if (p == null || r == null) {
            System.out.println("Paquete o repartidor no existe");
            return false;
        }
        // Regla: repartidor no disponible no puede recibir asignación
        if (!r.isDisponible()) {
            System.out.println("Repartidor no disponible");
            return false;
        }
        // Regla: zona del repartidor debe corresponder al destino
        if (r.getZona() != p.getDestino()) {
            System.out.println("Zona no coincide: " + r.getZona() + " vs " + p.getDestino());
            return false;
        }

        r.setDisponible(false);
        System.out.println("Asignado " + p.getCodigo() + " a " + r.getNombre());
        return true;
    }

    // --- HISTORIAL CON TU LISTA PROPIA ---

    public void registrarEntregaEnHistorial(Paquete p) {
        // Conserva orden real de entrega
        historialEntregas.agregar(p);
    }

    public void mostrarHistorial() {
        System.out.println("--- Historial de entregas (tu lista propia) ---");
        for (Paquete p : historialEntregas) { // Iterable obligatorio
            System.out.println(p);
        }
    }

    // --- REPORTES PARA EL MENU ---

    public void mostrarOrdenLlegada() {
        System.out.println(paquetesEnOrdenLlegada.keySet());
    }

    public Set<Municipio> getMunicipiosUnicos() { return municipiosUnicos; }
    public Set<Municipio> getMunicipiosOrdenados() { return municipiosOrdenados; }
    public Map<Municipio, List<Paquete>> getPaquetesPorMunicipio() { return paquetesPorMunicipio; }
    public List<Paquete> getTodosLosPaquetes() { return new ArrayList<>(paquetesEnOrdenLlegada.values()); }
    public IColeccionSimple<Paquete> getHistorial() { return historialEntregas; }

    // Para probar rápido - el parcial pide mínimo 30 paquetes, 5 destinos, 6 repartidores
    public void cargarDatosPrueba() {
        
    }

}
