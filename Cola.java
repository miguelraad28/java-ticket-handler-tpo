public class Cola {

    private NodoCola primero;
    private int tamanio;

    public Cola() {
        this.primero = null;
        this.tamanio = 0;
    }

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

    public Diccionario desencolar() {
        if (estaVacia()) {
            return null;
        }
        Diccionario elemento = primero.elemento;
        primero = primero.siguiente;
        tamanio--;
        return elemento;
    }

    public NodoCola frente() {
        if (estaVacia()) {
            return null;
        }
        return primero;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int tamanio() {
        return tamanio;
    }
}