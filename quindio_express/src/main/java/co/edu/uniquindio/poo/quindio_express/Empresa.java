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
        // 6 repartidores
        registrarRepartidor(new Repartidor("R01", "Carlos", Municipio.ARMENIA, true));
        registrarRepartidor(new Repartidor("R02", "Ana", Municipio.SALENTO, true));
        registrarRepartidor(new Repartidor("R03", "Luis", Municipio.CALARCA, true));
        registrarRepartidor(new Repartidor("R04", "Marta", Municipio.QUIMBAYA, true));
        registrarRepartidor(new Repartidor("R05", "Jorge", Municipio.CIRCASIA, true));
        registrarRepartidor(new Repartidor("R06", "Sofia", Municipio.ARMENIA, false));

        // 30 paquetes, 5 destinos, códigos no consecutivos
        registrarPaquete(new Paquete("PQ731", Municipio.ARMENIA, 2.5, 5, 30));
        registrarPaquete(new Paquete("PQ105", Municipio.SALENTO, 3.0, 5, 25));
        registrarPaquete(new Paquete("PQ942", Municipio.CALARCA, 1.2, 4, 20));
        registrarPaquete(new Paquete("PQ318", Municipio.QUIMBAYA, 5.5, 3, 40));
        registrarPaquete(new Paquete("PQ567", Municipio.CIRCASIA, 4.0, 5, 15));
        registrarPaquete(new Paquete("PQ829", Municipio.ARMENIA, 1.8, 2, 60));
        registrarPaquete(new Paquete("PQ214", Municipio.SALENTO, 6.2, 4, 35));
        registrarPaquete(new Paquete("PQ421", Municipio.CALARCA, 2.0, 1, 50));
        registrarPaquete(new Paquete("PQ842", Municipio.QUIMBAYA, 3.3, 3, 22));
        registrarPaquete(new Paquete("PQ360", Municipio.CIRCASIA, 7.0, 5, 18));
        registrarPaquete(new Paquete("PQ477", Municipio.ARMENIA, 2.2, 4, 28));
        registrarPaquete(new Paquete("PQ593", Municipio.SALENTO, 1.5, 2, 45));
        registrarPaquete(new Paquete("PQ650", Municipio.CALARCA, 4.4, 5, 12));
        registrarPaquete(new Paquete("PQ184", Municipio.QUIMBAYA, 3.8, 3, 33));
        registrarPaquete(new Paquete("PQ276", Municipio.CIRCASIA, 2.9, 4, 27));
        registrarPaquete(new Paquete("PQ905", Municipio.ARMENIA, 5.0, 1, 70));
        registrarPaquete(new Paquete("PQ118", Municipio.SALENTO, 2.7, 3, 31));
        registrarPaquete(new Paquete("PQ739", Municipio.CALARCA, 6.5, 5, 19));
        registrarPaquete(new Paquete("PQ452", Municipio.QUIMBAYA, 1.1, 2, 55));
        registrarPaquete(new Paquete("PQ861", Municipio.CIRCASIA, 3.6, 4, 24));
        registrarPaquete(new Paquete("PQ327", Municipio.ARMENIA, 4.8, 5, 15));  // empata con PQ567
        registrarPaquete(new Paquete("PQ694", Municipio.SALENTO, 2.3, 1, 65));
        registrarPaquete(new Paquete("PQ513", Municipio.CALARCA, 3.1, 3, 29));
        registrarPaquete(new Paquete("PQ988", Municipio.QUIMBAYA, 5.9, 4, 21));
        registrarPaquete(new Paquete("PQ246", Municipio.CIRCASIA, 2.6, 2, 48));
        registrarPaquete(new Paquete("PQ775", Municipio.ARMENIA, 1.9, 3, 38));
        registrarPaquete(new Paquete("PQ139", Municipio.SALENTO, 4.1, 5, 14));
        registrarPaquete(new Paquete("PQ602", Municipio.CALARCA, 3.4, 2, 42));
        registrarPaquete(new Paquete("PQ384", Municipio.QUIMBAYA, 2.8, 5, 17));
        registrarPaquete(new Paquete("PQ957", Municipio.CIRCASIA, 6.0, 3, 26));

        System.out.println("Datos cargados: 30 paquetes y 6 repartidores");
    }

}
