package co.edu.uniquindio.poo.quindio_express;

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

        // 10. Análisis recursivo (pendiente, lo agrega el compañero de la recursividad)
        // - Peso total de los paquetes de un municipio (recursivo)
        // - Cantidad de paquetes con prioridad igual o superior a un valor (recursivo)
        // - Búsqueda binaria de un paquete por código
        // - Paquete de mayor peso (divide y vencerás)
    }
}
