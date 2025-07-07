package sistema;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase Diccionario que almacena pares clave-valor utilizando HashMap.
 * Permite insertar, obtener, eliminar y consultar datos en tiempo constante promedio.
 */
public class Diccionario {
    // Estructura interna basada en HashMap para mejorar el rendimiento de búsqueda
    private Map<String, Object> datos;

    /**
     * Constructor: inicializa el diccionario vacío.
     */
    public Diccionario() {
        datos = new HashMap<>();
    }

    /**
     * Inserta un par clave-valor en el diccionario.
     * Si la clave ya existe, sobrescribe el valor anterior.
     * @param clave la clave asociada al valor
     * @param valor el valor a almacenar
     */
    public void insertar(String clave, Object valor) {
        datos.put(clave, valor);
    }

    /**
     * Obtiene el valor asociado a una clave.
     * @param clave la clave a buscar
     * @return el valor correspondiente o null si no existe
     */
    public Object obtener(String clave) {
        return datos.get(clave);
    }

    /**
     * Elimina la clave y su valor del diccionario.
     * @param clave la clave a eliminar
     */
    public void eliminar(String clave) {
        datos.remove(clave);
    }

    /**
     * Verifica si una clave existe en el diccionario.
     * @param clave la clave a buscar
     * @return true si existe, false si no
     */
    public boolean existe(String clave) {
        return datos.containsKey(clave);
    }

    /**
     * Verifica si el diccionario está vacío.
     * @return true si no hay elementos, false si hay al menos uno
     */
    public boolean estaVacio() {
        return datos.isEmpty();
    }

    /**
     * Devuelve la cantidad de elementos almacenados.
     * @return número de claves registradas
     */
    public int tamanio() {
        return datos.size();
    }

    /**
     * Muestra por consola todas las claves y valores almacenados.
     */
    public void mostrarTodo() {
        for (Map.Entry<String, Object> entrada : datos.entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }
    }
}
