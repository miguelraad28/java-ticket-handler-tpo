package sistema;

/**
 * Clase que implementa una cola (FIFO) para gestionar tickets pendientes.
 */
public class Cola {

    private NodoCola primero;
    private int tamanio;

    public Cola() {
        this.primero = null;
        this.tamanio = 0;
    }

/**
 * Agrega un ticket al final de la cola.
 */
    public void encolar(Diccionario elemento) {
        NodoCola nuevo = new NodoCola(elemento);

        if (primero == null) {
            primero = nuevo;
        } else {
            NodoCola actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamanio++;
    }
 /**
 * Quita y devuelve el ticket del frente de la cola.
 */
    public Diccionario desencolar() {
        if (estaVacia()) {
            return null;
        }
        Diccionario elemento = primero.elemento;
        primero = primero.siguiente;
        tamanio--;
        return elemento;
    }
 /**
 * Retorna el primer nodo de la cola sin removerlo.
 */
    public NodoCola frente() {
        if (estaVacia()) {
            return null;
        }
        return primero;
    }
 /**
 * Retorna true si la cola está vacía, false en caso contrario.
 */
    public boolean estaVacia() {
        return tamanio == 0;
    }
 /**
 * Devuelve la cantidad de tickets en la cola.
 */
    public int tamanio() {
        return tamanio;
    }
}