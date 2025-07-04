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
    
    public int obtenerTamanio() {
        return tamanio;
    }
}