package sistema;

public class Empleado {
    private String id;
    private String nombre;
    private String password;

    public Empleado(String id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean validarPassword(String password) {
        return this.password.equals(password);
    }
} 