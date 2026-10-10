package co.edu.uniquindio.poo.quindio_express;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Empresa empresa = new Empresa();
        empresa.cargarDatosPrueba();

        // 1. Orden de llegada
        System.out.println("\n===== 1. Orden de llegada (orden de registro) =====");
        empresa.mostrarOrdenLlegada();

        // 2. Consulta por código
        System.out.println("\n===== 2. Consulta por código =====");
        Paquete encontrado = empresa.consultarPorCodigo("PQ731");
        System.out.println(encontrado != null ? encontrado : "PQ731 no existe");
        Paquete noExiste = empresa.consultarPorCodigo("PQ000");
        System.out.println(noExiste != null ? noExiste : "PQ000 no existe");

        // 3. Código duplicado
        System.out.println("\n===== 3. Registro de un código duplicado =====");
        boolean registrado = empresa.registrarPaquete(new Paquete("PQ731", Municipio.ARMENIA, 1.0, 3, 10));
        System.out.println("¿Se registró? " + registrado);

        // 4. Municipios
        System.out.println("\n===== 4. Municipios con paquetes =====");
        System.out.println("Sin repetir: " + empresa.getMunicipiosUnicos());
        System.out.println("Alfabético:  " + empresa.getMunicipiosOrdenados());

        // 5. Paquetes por municipio
        System.out.println("\n===== 5. Paquetes por municipio =====");
        for (Municipio m : empresa.getMunicipiosOrdenados()) {
            System.out.println(m + " -> " + empresa.getPaquetesPorMunicipio().get(m).size() + " paquetes");
        }

        // 6. Las dos modalidades de atención
        System.out.println("\n===== 6. Atención por orden de llegada y despacho prioritario =====");
        Paquete porLlegada = empresa.atenderPorOrdenLlegada();
        System.out.println("Por llegada: " + porLlegada);
        Paquete porPrioridad = empresa.despacharPorPrioridad();
        System.out.println("Por prioridad: " + porPrioridad);

        // 7. Asignación de repartidores
        System.out.println("\n===== 7. Asignación de repartidores =====");
        System.out.println("Resultado: " + empresa.asignarPaqueteARepartidor("PQ477", "R06")); // no disponible
        System.out.println("Resultado: " + empresa.asignarPaqueteARepartidor("PQ105", "R01")); // zona distinta
        System.out.println("Resultado: " + empresa.asignarPaqueteARepartidor("PQ477", "R01")); // válida
        System.out.println("Resultado: " + empresa.asignarPaqueteARepartidor("PQ105", "R02")); // válida
        System.out.println("Resultado: " + empresa.asignarPaqueteARepartidor("PQ942", "R03")); // válida

        // 8. Entregas e historial
        System.out.println("\n===== 8. Entregas e historial =====");
        empresa.registrarEntrega("PQ105", "R02");
        empresa.registrarEntrega("PQ942", "R03");
        empresa.registrarEntrega("PQ477", "R01");
        empresa.mostrarHistorial();
        System.out.println("Cantidad en el historial: " + empresa.getHistorial().tamano());
        System.out.println("Primer entregado: " + empresa.getHistorial().obtener(0).getCodigo());

        // 9. Eliminación en la lista propia
        System.out.println("\n===== 9. Eliminar del historial =====");
        Paquete aEliminar = empresa.consultarPorCodigo("PQ942");
        System.out.println("Eliminar PQ942 (existe): " + empresa.getHistorial().eliminar(aEliminar));
        System.out.println("Eliminar PQ942 otra vez: " + empresa.getHistorial().eliminar(aEliminar));
        empresa.mostrarHistorial();

        // 10. Análisis recursivo
        System.out.println("\n===== 10. Análisis recursivo =====");
        AnaliticasRecursivas analiticas = new AnaliticasRecursivas();
        List<Paquete> todos = empresa.getTodosLosPaquetes();

        System.out.printf("Peso total de ARMENIA: %.1f kg%n", analiticas.pesoTotalPorMunicipio(todos, Municipio.ARMENIA));
        System.out.println("Paquetes con prioridad >= 4: " + analiticas.contarPorPrioridadMinima(todos, 4));

        // La búsqueda binaria exige la lista ordenada por código (precondición)
        List<Paquete> ordenados = new ArrayList<>(todos);
        Collections.sort(ordenados);   // usa el Comparable de Paquete: orden por código
        Paquete buscado = analiticas.busquedaBinaria(ordenados, "PQ731");
        System.out.println("Búsqueda binaria PQ731: " + (buscado != null ? buscado.getCodigo() : "no encontrado"));
        analiticas.busquedaBinaria(ordenados, "PQ000");   // no existe: el propio método imprime el aviso

        Paquete masPesado = analiticas.masPesadoDyV(todos);
        System.out.println("Más pesado (divide y vencerás): " + masPesado.getCodigo() + " con " + masPesado.getPeso() + " kg");

        // 11. Casos de la lista propia
        System.out.println("\n===== 11. Casos de la lista propia =====");
        HistorialEntregas<String> lista = new HistorialEntregas<>();

        // Lista vacía
        System.out.println("¿Vacía? " + lista.estaVacia());
        System.out.println("Eliminar en vacía: " + lista.eliminar("A"));

        // Un solo elemento
        lista.agregar("A");
        System.out.println("Tamaño: " + lista.tamano() + ", obtener(0): " + lista.obtener(0));
        System.out.println("Eliminar único: " + lista.eliminar("A") + ", ¿vacía? " + lista.estaVacia());

        // Varios elementos: primero, último e inexistente
        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        lista.agregar("D");
        System.out.println("Eliminar primero (A): " + lista.eliminar("A"));
        System.out.println("Eliminar último (D): " + lista.eliminar("D"));
        System.out.println("Eliminar inexistente (Z): " + lista.eliminar("Z"));
        System.out.print("Quedó: ");
        for (String s : lista) {
            System.out.print(s + " ");
        }
        System.out.println();

        // Posición inválida
        try {
            lista.obtener(5);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("obtener(5) lanza IndexOutOfBoundsException");
        }
    }
}
