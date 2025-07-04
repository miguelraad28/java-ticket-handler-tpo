package sistema;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que implementa un Árbol AVL (ABB balanceado) para almacenar tickets
 * resueltos ordenados por el tiempo de resolución.
 */
public class AVL {
    public class Nodo {
        int tiempoResolucion;
        Nodo izquierdo;
        Nodo derecho;
        Diccionario[] tickets;
        int cantidadTickets;
        int altura;

        Nodo(int tiempoResolucion) {
            this.tiempoResolucion = tiempoResolucion;
            this.izquierdo = null;
            this.derecho = null;
            this.tickets = new Diccionario[10];
            this.cantidadTickets = 0;
            this.altura = 1;
        }

        void agregarTicket(Diccionario ticket) {
            if (cantidadTickets == tickets.length) {
                Diccionario[] nuevoArray = new Diccionario[tickets.length * 2];
                System.arraycopy(tickets, 0, nuevoArray, 0, tickets.length);
                tickets = nuevoArray;
            }
            tickets[cantidadTickets++] = ticket;
        }
    }

    public Nodo raiz;

    public AVL() {
        this.raiz = null;
    }

    /**
     * Inserta un elemento en el árbol manteniéndolo balanceado (AVL).
     * 
     * @param tiempoResolucion Tiempo de resolución del ticket.
     * @param ticket           Ticket que se sumará al nodo del arbol
     */
    public void insertar(int tiempoResolucion, Diccionario ticket) {
        raiz = insertarRecursivo(raiz, tiempoResolucion, ticket);
    }

    private Nodo insertarRecursivo(Nodo nodo, int tiempoResolucion, Diccionario ticket) {
        if (nodo == null) {
            Nodo nuevo = new Nodo(tiempoResolucion);
            nuevo.agregarTicket(ticket);
            return nuevo;
        }
        if (tiempoResolucion < nodo.tiempoResolucion) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, tiempoResolucion, ticket);
        } else if (tiempoResolucion > nodo.tiempoResolucion) {
            nodo.derecho = insertarRecursivo(nodo.derecho, tiempoResolucion, ticket);
        } else {
            nodo.agregarTicket(ticket);
            return nodo;
        }

        // Actualizar altura
        nodo.altura = 1 + Math.max(
                conseguirAltura(nodo.izquierdo),
                conseguirAltura(nodo.derecho));

        // Rebalancear
        int balance = conseguirBalance(nodo);

        // Caso Izquierda-Izquierda: rotación a la derecha
        if (balance > 1 && tiempoResolucion < nodo.izquierdo.tiempoResolucion) {
            return rotarDerecha(nodo);
        }
        // Caso Derecha-Derecha: rotación a la izquierda
        if (balance < -1 && tiempoResolucion > nodo.derecho.tiempoResolucion) {
            return rotarIzquierda(nodo);
        }
        // Caso Izquierda-Derecha
        if (balance > 1 && tiempoResolucion > nodo.izquierdo.tiempoResolucion) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }
        // Caso Derecha-Izquierda
        if (balance < -1 && tiempoResolucion < nodo.derecho.tiempoResolucion) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    private int conseguirAltura(Nodo node) {
        return (node == null) ? 0 : node.altura;
    }

    private int conseguirBalance(Nodo node) {
        return (node == null) ? 0 : conseguirAltura(node.izquierdo) - conseguirAltura(node.derecho);
    }

    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo T2 = x.derecho;

        // Rotación
        x.derecho = y;
        y.izquierdo = T2;

        // Actualizar alturas
        y.altura = 1 + Math.max(
                conseguirAltura(y.izquierdo),
                conseguirAltura(y.derecho));
        x.altura = 1 + Math.max(
                conseguirAltura(x.izquierdo),
                conseguirAltura(x.derecho));

        return x;
    }

    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo T2 = y.izquierdo;

        // Rotación
        y.izquierdo = x;
        x.derecho = T2;

        // Actualizar alturas
        x.altura = 1 + Math.max(
                conseguirAltura(x.izquierdo),
                conseguirAltura(x.derecho));
        y.altura = 1 + Math.max(
                conseguirAltura(y.izquierdo),
                conseguirAltura(y.derecho));

        return y;
    }

    /**
     * Recorre el árbol en in-order mostrando tiempos y cantidad de tickets.
     */
    public void recorridoInorden() {
        recorridoInordenRecursivo(raiz);
    }

    private void recorridoInordenRecursivo(Nodo nodo) {
        if (nodo != null) {
            recorridoInordenRecursivo(nodo.izquierdo);

            System.out.println("Tiempo de resolución: " + nodo.tiempoResolucion + " segundos");
            System.out.println("Cantidad de tickets: " + nodo.cantidadTickets);
            System.out.println("------------------------");

            recorridoInordenRecursivo(nodo.derecho);
        }
    }

    /**
     * Busca y muestra tickets con un tiempo específico.
     */
    public void buscarTicketsPorTiempo(int tiempoBuscado) {
        if (raiz == null) {
            System.out.println("No hay tickets resueltos en el sistema con el tiempo " + tiempoBuscado + ".");
        } else {
            buscarTicketsPorTiempoRecursivo(raiz, tiempoBuscado);
        }
    }

    private void buscarTicketsPorTiempoRecursivo(Nodo nodo, int tiempoBuscado) {
        if (nodo == null)
            return;
        buscarTicketsPorTiempoRecursivo(nodo.izquierdo, tiempoBuscado);
        if (nodo.tiempoResolucion == tiempoBuscado) {
            System.out.println("\nTiempo de resolución: " + nodo.tiempoResolucion + " segundos");
            System.out.println("Cantidad de tickets con este tiempo: " + nodo.cantidadTickets);
            System.out.println("------------------------");
            for (int i = 0; i < nodo.cantidadTickets; i++) {
                Diccionario ticket = nodo.tickets[i];
                System.out.println("Ticket #" + (i + 1));
                System.out.println("ID: " + ticket.obtener("ticketId"));
                System.out.println("Cliente: " + ticket.obtener("nombreCliente"));
                System.out.println("Email: " + ticket.obtener("emailCliente"));
                System.out.println("Problema: " + ticket.obtener("descripcionProblema"));
                System.out.println("Fecha de creación: " + ticket.obtener("fechaCreacion"));
                System.out.println("------------------------");
            }
        }
        buscarTicketsPorTiempoRecursivo(nodo.derecho, tiempoBuscado);
    }

    /**
     * Muestra todos los tiempos únicos disponibles.
     */
    public void mostrarTiemposDisponibles() {
        if (raiz == null) {
            System.out.println("No hay tiempos registrados aún.");
        } else {
            System.out.println("Tiempos registrados disponibles:");
            mostrarTiempos(raiz);
        }
    }

    private void mostrarTiempos(Nodo nodo) {
        if (nodo != null) {
            mostrarTiempos(nodo.izquierdo);
            System.out.println("• " + nodo.tiempoResolucion + " segundos");
            mostrarTiempos(nodo.derecho);
        }
    }

    public void generarReportePorEmpleado(Map<String, Empleado> empleados) {
        // <ID empl, cant>
        Map<String, Integer> cantidadPorEmpleado = new HashMap<>();
        Map<String, Integer> mejorTiempoPorEmpleado = new HashMap<>();
        Map<String, Integer> peorTiempoPorEmpleado = new HashMap<>();
        Map<String, Long> sumaTiemposPorEmpleado = new HashMap<>();

        // Esta función actualiza las variables directamente, no retorna nada
        recolectarEstadisticasEmpleado(raiz, cantidadPorEmpleado, mejorTiempoPorEmpleado,
                peorTiempoPorEmpleado, sumaTiemposPorEmpleado);

        // Creamos un array con los keySet() de cantidadPorEmpleado (IDs de empleados)
        for (String empleadoId : cantidadPorEmpleado.keySet()) {
            Empleado empleado = empleados.get(empleadoId);
            if (empleado != null) {
                System.out.println("\nEmpleado: " + empleado.getNombre() + " (ID: " + empleadoId + ")");
                System.out.println("Total de tickets resueltos: " + cantidadPorEmpleado.get(empleadoId));
                System.out
                        .println("Mejor tiempo de resolución: " + mejorTiempoPorEmpleado.get(empleadoId) + " segundos");
                System.out.println("Peor tiempo de resolución: " + peorTiempoPorEmpleado.get(empleadoId) + " segundos");

                double promedio = sumaTiemposPorEmpleado.get(empleadoId)
                        / (double) cantidadPorEmpleado.get(empleadoId);

                // %.2f es un formateador de decimales para que se muestren solo 2 decimales
                System.out.printf("Tiempo promedio de resolución: %.2f segundos\n", promedio);
                System.out.println("------------------------");
            }
        }
    }

    /*
     * Recorre el árbol y recolecta estadísticas por empleado.
     * 
     * La complejidad es de O(n + m) donde n es el número de nodos del árbol y m es
     * el número de tickets por nodo.
     * 
     */
    private void recolectarEstadisticasEmpleado(Nodo nodo,
            Map<String, Integer> cantidadPorEmpleado,
            Map<String, Integer> mejorTiempoPorEmpleado,
            Map<String, Integer> peorTiempoPorEmpleado,
            Map<String, Long> sumaTiemposPorEmpleado) {
        if (nodo == null)
            return;

        // Recorrer subárbol izquierdo
        recolectarEstadisticasEmpleado(nodo.izquierdo, cantidadPorEmpleado, mejorTiempoPorEmpleado,
                peorTiempoPorEmpleado, sumaTiemposPorEmpleado);

        // En 1 sólo recorrido de los tickets, vamos alternando frente a qué empleado
        // adicionarle las estadísticas
        for (int i = 0; i < nodo.cantidadTickets; i++) {
            // Paso 1: Obtenemos el ticket
            Diccionario ticket = nodo.tickets[i];

            // Paso 2: Obtenemos el ID del empleado que resolvió el ticket
            String empleadoId = (String) ticket.obtener("empleadoResolucionId");

            // Paso 3: Actualizamos la cantidad de tickets resueltos por el empleado
            cantidadPorEmpleado.merge(empleadoId, 1, Integer::sum);

            // Paso 3.1: Actualizamos el mejor tiempo de resolución del empleado
            mejorTiempoPorEmpleado.merge(empleadoId, nodo.tiempoResolucion,
                    (actual, nuevo) -> Math.min(actual, nuevo));

            // Paso 3.2: Actualizamos el peor tiempo de resolución del empleado
            peorTiempoPorEmpleado.merge(empleadoId, nodo.tiempoResolucion,
                    (actual, nuevo) -> Math.max(actual, nuevo));

            // Paso 3.3: Actualizamos la suma de tiempos para el promedio
            sumaTiemposPorEmpleado.merge(empleadoId, (long) nodo.tiempoResolucion, Long::sum);
        }

        // Recorrer subárbol derecho
        recolectarEstadisticasEmpleado(nodo.derecho, cantidadPorEmpleado, mejorTiempoPorEmpleado,
                peorTiempoPorEmpleado, sumaTiemposPorEmpleado);
    }
}
