package co.edu.uniquindio.poo.quindio_express;

import javax.swing.*;
import java.util.*;

public class Main {

    private static final String TITULO = "Quindío Express";

    public static void main(String[] args) {
        Empresa empresa = new Empresa();
        empresa.cargarDatosPrueba();

        String menu = """
                ===== QUINDÍO EXPRESS =====
                1. Orden de llegada
                2. Consultar paquete por código
                3. Registrar paquete
                4. Municipios (sin repetir y alfabético)
                5. Paquetes por municipio
                6. Atender por orden de llegada
                7. Despachar por prioridad
                8. Asignar paquete a repartidor
                9. Registrar entrega
                10. Mostrar historial
                11. Eliminar del historial
                12. Análisis recursivo
                13. Pruebas de la lista propia
                0. Salir

                Digite una opción:""";

        while (true) {
            String op = JOptionPane.showInputDialog(null, menu, TITULO, JOptionPane.QUESTION_MESSAGE);
            if (op == null || op.trim().equals("0")) {
                JOptionPane.showMessageDialog(null, "¡Hasta pronto!", TITULO, JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            switch (op.trim()) {
                case "1" -> ordenLlegada(empresa);
                case "2" -> consultar(empresa);
                case "3" -> registrarPaquete(empresa);
                case "4" -> municipios(empresa);
                case "5" -> paquetesPorMunicipio(empresa);
                case "6" -> {
                    Paquete p = empresa.atenderPorOrdenLlegada();
                    mostrar("Atención por llegada", p != null ? p.toString() : "No hay paquetes en cola");
                }
                case "7" -> {
                    Paquete p = empresa.despacharPorPrioridad();
                    mostrar("Despacho prioritario", p != null ? p.toString() : "No hay paquetes en cola");
                }
                case "8" -> asignar(empresa);
                case "9" -> entrega(empresa);
                case "10" -> historial(empresa);
                case "11" -> eliminarHistorial(empresa);
                case "12" -> analisis(empresa);
                case "13" -> pruebasLista();
                default -> JOptionPane.showMessageDialog(null, "Opción no válida", TITULO, JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // ---------- 1 ----------
    private static void ordenLlegada(Empresa empresa) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Paquete p : empresa.getTodosLosPaquetes()) {
            sb.append(i++).append(". ").append(p.getCodigo()).append("\n");
        }
        mostrar("Orden de llegada", sb.toString());
    }

    // ---------- 2 ----------
    private static void consultar(Empresa empresa) {
        String codigo = leer("Código del paquete:");
        if (codigo == null) return;
        Paquete p = empresa.consultarPorCodigo(codigo.trim());
        mostrar("Consulta por código", p != null ? p.toString() : codigo + " no existe");
    }

    // ---------- 3 ----------
    private static void registrarPaquete(Empresa empresa) {
        try {
            String codigo = leer("Código:");
            if (codigo == null) return;
            Municipio destino = elegirMunicipio();
            if (destino == null) return;
            String peso = leer("Peso (kg):");
            if (peso == null) return;
            String prioridad = leer("Prioridad (número):");
            if (prioridad == null) return;
            String tiempo = leer("Tiempo estimado (min):");
            if (tiempo == null) return;

            Paquete nuevo = new Paquete(codigo.trim(), destino,
                    Double.parseDouble(peso.trim()),
                    Integer.parseInt(prioridad.trim()),
                    Integer.parseInt(tiempo.trim()));
            boolean ok = empresa.registrarPaquete(nuevo);
            mostrar("Registro", ok ? "Paquete registrado" : "Error: el código " + codigo + " ya existe");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe digitar números válidos", TITULO, JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- 4 ----------
    private static void municipios(Empresa empresa) {
        mostrar("Municipios",
                "Sin repetir:\n" + empresa.getMunicipiosUnicos() +
                        "\n\nAlfabético:\n" + empresa.getMunicipiosOrdenados());
    }

    // ---------- 5 ----------
    private static void paquetesPorMunicipio(Empresa empresa) {
        StringBuilder sb = new StringBuilder();
        for (Municipio m : empresa.getMunicipiosOrdenados()) {
            List<Paquete> lista = empresa.paquetesDeMunicipioOrdenados(m);
            sb.append(m).append(" (").append(lista.size()).append("): ");
            for (Paquete p : lista) sb.append(p.getCodigo()).append(" ");
            sb.append("\n");
        }
        mostrar("Paquetes por municipio", sb.toString());
    }

    // ---------- 8 ----------
    private static void asignar(Empresa empresa) {
        String codigo = leer("Código del paquete:");
        if (codigo == null) return;
        String id = leer("ID del repartidor (ej. R01):");
        if (id == null) return;
        boolean ok = empresa.asignarPaqueteARepartidor(codigo.trim(), id.trim());
        mostrar("Asignación", ok ? "Asignación exitosa" :
                "No se pudo asignar (revise la consola: no existe, no disponible o zona distinta)");
    }

    // ---------- 9 ----------
    private static void entrega(Empresa empresa) {
        String codigo = leer("Código del paquete entregado:");
        if (codigo == null) return;
        String id = leer("ID del repartidor:");
        if (id == null) return;
        boolean ok = empresa.registrarEntrega(codigo.trim(), id.trim());
        mostrar("Entrega", ok ? "Entrega registrada en el historial" : "El paquete o el repartidor no existe");
    }

    // ---------- 10 ----------
    private static void historial(Empresa empresa) {
        StringBuilder sb = new StringBuilder();
        for (Paquete p : empresa.getHistorial()) {
            sb.append(p).append("\n");
        }
        sb.append("\nCantidad en el historial: ").append(empresa.getHistorial().tamano());
        if (empresa.getHistorial().tamano() > 0) {
            sb.append("\nPrimer entregado: ").append(empresa.getHistorial().obtener(0).getCodigo());
        }
        mostrar("Historial de entregas", sb.toString());
    }

    // ---------- 11 ----------
    private static void eliminarHistorial(Empresa empresa) {
        String codigo = leer("Código del paquete a eliminar del historial:");
        if (codigo == null) return;
        Paquete p = empresa.consultarPorCodigo(codigo.trim());
        if (p == null) {
            mostrar("Eliminar", codigo + " no existe");
            return;
        }
        boolean ok = empresa.getHistorial().eliminar(p);
        mostrar("Eliminar", ok ? "Eliminado del historial" : "Ese paquete no está en el historial");
    }

    // ---------- 12 ----------
    private static void analisis(Empresa empresa) {
        AnaliticasRecursivas analiticas = new AnaliticasRecursivas();
        List<Paquete> todos = empresa.getTodosLosPaquetes();
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Peso total de ARMENIA: %.1f kg%n",
                analiticas.pesoTotalPorMunicipio(todos, Municipio.ARMENIA)));
        sb.append("Paquetes con prioridad >= 4: ")
                .append(analiticas.contarPorPrioridadMinima(todos, 4)).append("\n");

        List<Paquete> ordenados = new ArrayList<>(todos);
        Collections.sort(ordenados); // Comparable de Paquete: por código
        Paquete buscado = analiticas.busquedaBinaria(ordenados, "PQ731");
        sb.append("Búsqueda binaria PQ731: ")
                .append(buscado != null ? buscado.getCodigo() : "no encontrado").append("\n");

        Paquete masPesado = analiticas.masPesadoDyV(todos);
        sb.append("Más pesado (divide y vencerás): ")
                .append(masPesado.getCodigo()).append(" con ").append(masPesado.getPeso()).append(" kg");

        mostrar("Análisis recursivo", sb.toString());
    }

    // ---------- 13 ----------
    private static void pruebasLista() {
        HistorialEntregas<String> lista = new HistorialEntregas<>();
        StringBuilder sb = new StringBuilder();

        sb.append("¿Vacía? ").append(lista.estaVacia()).append("\n");
        sb.append("Eliminar en vacía: ").append(lista.eliminar("A")).append("\n\n");

        lista.agregar("A");
        sb.append("Tamaño: ").append(lista.tamano()).append(", obtener(0): ").append(lista.obtener(0)).append("\n");
        sb.append("Eliminar único: ").append(lista.eliminar("A"))
                .append(", ¿vacía? ").append(lista.estaVacia()).append("\n\n");

        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        lista.agregar("D");
        sb.append("Eliminar primero (A): ").append(lista.eliminar("A")).append("\n");
        sb.append("Eliminar último (D): ").append(lista.eliminar("D")).append("\n");
        sb.append("Eliminar inexistente (Z): ").append(lista.eliminar("Z")).append("\n");
        sb.append("Quedó: ");
        for (String s : lista) sb.append(s).append(" ");
        sb.append("\n\n");

        try {
            lista.obtener(5);
        } catch (IndexOutOfBoundsException e) {
            sb.append("obtener(5) lanza IndexOutOfBoundsException");
        }
        mostrar("Pruebas de la lista propia", sb.toString());
    }

    // ---------- Utilidades ----------
    private static String leer(String mensaje) {
        return JOptionPane.showInputDialog(null, mensaje, TITULO, JOptionPane.QUESTION_MESSAGE);
    }

    private static Municipio elegirMunicipio() {
        return (Municipio) JOptionPane.showInputDialog(null, "Municipio destino:", TITULO,
                JOptionPane.QUESTION_MESSAGE, null, Municipio.values(), Municipio.values()[0]);
    }

    // Muestra texto largo con scroll
    private static void mostrar(String titulo, String texto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(520, 300));
        JOptionPane.showMessageDialog(null, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}



/*
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
*/