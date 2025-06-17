package sistema;

/**
 * Implementación simple de un diccionario clave-valor usando arrays paralelos.
 */
public class Diccionario {
    private class Nodo {
        String clave;
        Object valor;
        Nodo siguiente;

        Nodo(String clave, Object valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Nodo primero;
    private int tamanio;

    public Diccionario() {
        this.primero = null;
        this.tamanio = 0;
    }

/**
 * Inserta una nueva entrada al diccionario.
 * @param clave Clave de la entrada.
 * @param valor Valor asociado a la clave.
 */
    /**
 * Inserta o actualiza un valor asociado a una clave específica.
 */
    public void insertar(String clave, Object valor) {
        Nodo nuevo = new Nodo(clave, valor);
        if (primero == null) {
            primero = nuevo;
        } else {
            Nodo actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    /**
 * Devuelve el valor asociado a la clave dada, o null si no existe.
 */
    public Object obtener(String clave) {
        Nodo actual = primero;
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public void eliminar(String clave) {
        if (primero == null)
            return;

        if (primero.clave.equals(clave)) {
            primero = primero.siguiente;
            tamanio--;
            return;
        }

        Nodo actual = primero;
        while (actual.siguiente != null) {
            if (actual.siguiente.clave.equals(clave)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamanio--;
                return;
            }
            actual = actual.siguiente;
        }
    }

    public boolean existe(String clave) {
        return obtener(clave) != null;
    }

    public boolean estaVacio() {
        return tamanio == 0;
    }

    public int tamanio() {
        return tamanio;
    }
}