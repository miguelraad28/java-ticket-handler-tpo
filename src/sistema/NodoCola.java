package sistema;

/**
 * Representa un nodo en la cola de tickets pendientes.
 */
public class NodoCola {
    Diccionario elemento;
    NodoCola siguiente;

    NodoCola(Diccionario elemento) {
        this.elemento = elemento;
        this.siguiente = null;
    }
}
