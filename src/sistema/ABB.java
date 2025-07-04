package sistema;

/**
 * Clase que implementa un Árbol AVL (ABB balanceado) para almacenar tickets resueltos
 * ordenados por el tiempo de resolución.
 */
public class ABB {
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

    public ABB() {
        this.raiz = null;
    }

    /**
     * Inserta un ticket en el árbol, manteniéndolo balanceado (AVL).
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
                conseguirAltura(nodo.derecho)
        );

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
        return (node == null) ? 0 :
                conseguirAltura(node.izquierdo) - conseguirAltura(node.derecho);
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
                conseguirAltura(y.derecho)
        );
        x.altura = 1 + Math.max(
                conseguirAltura(x.izquierdo),
                conseguirAltura(x.derecho)
        );

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
                conseguirAltura(x.derecho)
        );
        y.altura = 1 + Math.max(
                conseguirAltura(y.izquierdo),
                conseguirAltura(y.derecho)
        );

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
        if (nodo == null) return;
        buscarTicketsPorTiempoRecursivo(nodo.izquierdo, tiempoBuscado);
        if (nodo.tiempoResolucion == tiempoBuscado) {
            System.out.println("Tiempo de resolución: " + nodo.tiempoResolucion + " segundos");
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
}
