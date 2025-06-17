package sistema;

/**
 * Clase que implementa un Árbol Binario de Búsqueda (ABB).
 * Se utiliza para almacenar tickets resueltos ordenados por el tiempo de resolución.
 */
public class ABB {
    public class Nodo {
        int tiempoResolucion;
        Nodo izquierdo;
        Nodo derecho;
        Diccionario[] tickets;
        int cantidadTickets;

        Nodo(int tiempoResolucion) {
            this.tiempoResolucion = tiempoResolucion;
            this.izquierdo = null;
            this.derecho = null;
            this.tickets = new Diccionario[10]; // Array inicial de 10 tickets
            this.cantidadTickets = 0;
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

    public ABB() {
        this.raiz = null;
    }

/**
 * Inserta un elemento en el árbol binario de búsqueda.
 * @param clave Clave a insertar.
 * @param valor Valor asociado a la clave.
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
        }

        return nodo;
    }

    /**
 * Muestra todos los tickets del ABB en orden ascendente de tiempo de resolución.
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

    public void buscarTicketsPorTiempo(int tiempoBuscado) {
        if (raiz == null) {
            System.out.println("No hay tickets resueltos en el sistema con el tiempo " + tiempoBuscado +".");
            return;
        }
        buscarTicketsPorTiempoRecursivo(raiz, tiempoBuscado);
    }

    private void buscarTicketsPorTiempoRecursivo(Nodo nodo, int tiempoBuscado) {
        if (nodo == null) {
            return;
        }

        // Buscar en el subárbol izquierdo
        buscarTicketsPorTiempoRecursivo(nodo.izquierdo, tiempoBuscado);

        // Verificar si este nodo tiene el tiempo buscado
        if (nodo.tiempoResolucion == tiempoBuscado) {
            System.out.println("\nTiempo de resolución: " + nodo.tiempoResolucion + " segundos");
            System.out.println("Cantidad de tickets con este tiempo: " + nodo.cantidadTickets);
            System.out.println("------------------------");

            for (int i = 0; i < nodo.cantidadTickets; i++) {
                Diccionario ticket = nodo.tickets[i];
                System.out.println("\nTicket #" + (i + 1));
                System.out.println("ID: " + ticket.obtener("ticketId"));
                System.out.println("Cliente: " + ticket.obtener("nombreCliente"));
                System.out.println("Email: " + ticket.obtener("emailCliente"));
                System.out.println("Problema: " + ticket.obtener("descripcionProblema"));
                System.out.println("Fecha de creación: " + ticket.obtener("fechaCreacion"));
                System.out.println("------------------------");
            }
        }

        // Buscar en el subárbol derecho
        buscarTicketsPorTiempoRecursivo(nodo.derecho, tiempoBuscado);
    }

    /**
     * Muestra todos los tiempos únicos de resolución disponibles en el ABB.
     */
    public void mostrarTiemposDisponibles() {
        if (raiz == null) {
            System.out.println("No hay tiempos registrados aún.");
        } else {
            System.out.println("\nTiempos registrados disponibles:");
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
}