package sistema;

import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SistemaTickets {
    private static Cola colaTickets;
    private static AVL ticketsResueltos;
    private static Scanner scanner;
    private static Random random;
    private static long tiempoInicioDeSolucion;
    private static Empleado empleadoActual;
    private static Map<String, Empleado> empleados;

    /**
     * Verifica si una cadena de texto está vacía o solo contiene espacios.
     * Se utiliza para validar entradas obligatorias.
     */
    private static boolean estaVacio(String entrada) {
        return entrada == null || entrada.trim().isEmpty();
    }

    /**
     * Devuelve true si el nombre es inválido (contiene caracteres no permitidos).
     * Solo se permiten letras (mayúsculas/minúsculas), tildes y espacios.
     */
    private static boolean nombreEsInvalido(String nombre) {
        return !nombre.matches("^[A-Za-záéíóúÁÉÍÓÚñÑ ]+$");
    }

    /**
     * Devuelve true si el email NO tiene un formato válido tipo usuario@dominio.
     * No valida la existencia real del dominio, solo el formato.
     */
    private static boolean emailEsInvalido(String email) {
        return !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    }


    /**
     * Inicializa las estructuras del sistema: cola de tickets, árbol de tickets
     * resueltos,
     * scanner para entrada por consola y generador aleatorio.
     */
    private static void inicializarSistema() {
        colaTickets = new Cola();
        ticketsResueltos = new AVL();
        scanner = new Scanner(System.in);
        random = new Random();

        // Inicialización de empleados
        empleados = new HashMap<>();
        empleados.put("EMP001", new Empleado("EMP001", "Miguel", "pass123"));
        empleados.put("EMP002", new Empleado("EMP002", "Roberto", "pass456"));
        empleados.put("EMP003", new Empleado("EMP003", "Felipe", "pass789"));
    }

    /**
     * Genera automáticamente 5 tickets con datos aleatorios y los encola como
     * pendientes.
     */
    private static void generarTicketsIniciales() {
        String[] nombres = { "Juan", "María", "Carlos", "Ana", "Pedro", "Laura", "Miguel", "Sofía" };
        String[] dominios = { "gmail.com", "hotmail.com", "yahoo.com", "outlook.com" };
        String[] problemas = {
                "No puedo iniciar sesión",
                "Error al procesar el pago",
                "La aplicación se cierra inesperadamente",
                "No puedo actualizar mi perfil",
                "Problemas con la conexión"
        };

        for (int i = 0; i < 5; i++) {
            Diccionario ticket = new Diccionario();
            ticket.insertar("ticketId", String.valueOf(i + 1));
            ticket.insertar("nombreCliente", nombres[random.nextInt(nombres.length)]);
            String email = ticket.obtener("nombreCliente").toString().toLowerCase() + "@" +
                    dominios[random.nextInt(dominios.length)];
            ticket.insertar("emailCliente", email);
            ticket.insertar("descripcionProblema", problemas[random.nextInt(problemas.length)]);
            ticket.insertar("fechaCreacion",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            colaTickets.encolar(ticket);
        }
    }

    /**
     * Permite procesar uno o más tickets de la cola hasta que se resuelvan o el
     * usuario decida salir.
     * Cada ticket resuelto se almacena en un ABB con su tiempo de resolución.
     */
    private static void procesarTickets() {
        if (colaTickets.estaVacia()) {
            System.out.println("No hay tickets pendientes para resolver.");
            return;
        }

        while (!colaTickets.estaVacia()) {
            Diccionario ticketActual = colaTickets.desencolar();
            mostrarTicket(ticketActual);
            tiempoInicioDeSolucion = System.currentTimeMillis();

            String entrada;
            do {
                System.out.println("\n¿Desea resolver este ticket ahora? (Y para resolver, Q para volver al menú):");
                entrada = scanner.nextLine().trim().toUpperCase();

                if (entrada.isEmpty()) {
                    System.out.println("La entrada no puede estar vacía.");
                } else if (!entrada.equals("Y") && !entrada.equals("Q")) {
                    System.out.println("Entrada inválida. Ingrese 'Y' para resolver o 'Q' para volver al menú.");
                }

            } while (entrada.isEmpty() || (!entrada.equals("Y") && !entrada.equals("Q")));


            if (entrada.equals("Q")) {
                colaTickets.encolar(ticketActual);
                break;
            }

            long tiempoResolucion = (System.currentTimeMillis() - tiempoInicioDeSolucion) / 1000;

            ticketActual.insertar("empleadoResolucionId", empleadoActual.getId());

            ticketsResueltos.insertar((int) tiempoResolucion, ticketActual);

            System.out.println("Ticket resuelto en " + tiempoResolucion + " segundos por " + empleadoActual.getNombre()
                    + " (ID: " + empleadoActual.getId() + ").\n");

            if (colaTickets.estaVacia()) {
                System.out.println("Todos los tickets han sido resueltos.");
                break;
            }

            System.out.println(
                    "¿Desea resolver otro ticket? (Y para continuar, cualquier otra tecla para volver al menú):");
            entrada = "";
            while (entrada.isEmpty()) {
                entrada = scanner.nextLine().trim().toUpperCase();
            }

            if (!entrada.equals("Y")) {
                break;
            }
        }
    }

    /**
     * Muestra en consola los detalles de un ticket (ID, cliente, email, problema,
     * fecha).
     */
    private static void mostrarTicket(Diccionario ticket) {
        System.out.println("\n=== TICKET ACTUAL ===");
        System.out.println("ID: " + ticket.obtener("ticketId"));
        System.out.println("Cliente: " + ticket.obtener("nombreCliente"));
        System.out.println("Email: " + ticket.obtener("emailCliente"));
        System.out.println("Problema: " + ticket.obtener("descripcionProblema"));
        System.out.println("Fecha de creación: " + ticket.obtener("fechaCreacion"));
        System.out.println("===================");
    }

    /**
     * Muestra el menú principal del sistema con todas las opciones disponibles.
     */
    private static void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE GESTIÓN DE TICKETS ===");
        System.out.println("Usuario actual: " + empleadoActual.getNombre() + " (ID: " + empleadoActual.getId() + ")");
        System.out.println("1) Empezar jornada para resolver tickets");
        System.out.println("2) Ver cola de tickets y cantidad pendientes");
        System.out.println("3) Ver tickets resueltos por tiempo");
        System.out.println("4) Ver todos los tickets resueltos en X tiempo");
        System.out.println("5) Ver reporte de tickets por empleado");
        System.out.println("6) Registrar nuevo ticket como en calidad de cliente");
        System.out.println("******************************************************");
        System.out.println("7) Cerrar sesión de " + empleadoActual.getNombre());
        System.out.println("0) Salir");
        System.out.println("=====================================");
    }

    /**
     * Permite a un cliente registrar un nuevo ticket manualmente, validando los
     * campos.
     */
    private static void registrarNuevoTicket() {
        Diccionario ticket = new Diccionario();

        System.out.println("\n=== REGISTRO DE NUEVO TICKET ===");

        String nombre;
        do {
            System.out.print("Ingrese su nombre: ");
            nombre = scanner.nextLine().trim();
            if (estaVacio(nombre)) {
                System.out.println("El nombre no puede estar vacío.");
            } else if (nombreEsInvalido(nombre)) {
                System.out.println("El nombre solo puede contener letras.");
            }
        } while (estaVacio(nombre) || nombreEsInvalido(nombre));
        ticket.insertar("nombre", nombre);

        String email;
        do {
            System.out.print("Ingrese su email: ");
            email = scanner.nextLine().trim();
            if (estaVacio(email)) {
                System.out.println("El email no puede estar vacío.");
            } else if (emailEsInvalido(email)) {
                System.out.println("Formato de email inválido. Ej: usuario@dominio.com");
            }
        } while (estaVacio(email) || emailEsInvalido(email));
        ticket.insertar("email", email);

        System.out.print("Describa su problema: ");
        String descripcion = scanner.nextLine().trim();
        while (estaVacio(descripcion)) {
            System.out.print("La descripción no puede estar vacía. \nIntente nuevamente:");
            descripcion = scanner.nextLine().trim();
        }
        ticket.insertar("descripcion", descripcion);
        ticket.insertar("ticketId", String.valueOf(System.currentTimeMillis()));
        ticket.insertar("fechaCreacion", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        colaTickets.encolar(ticket);

        System.out.println("¡Ticket registrado correctamente y encolado como pendiente!");
    }

    /**
     * Solicita un tiempo al usuario y busca todos los tickets resueltos con ese
     * tiempo exacto.
     */
    private static void verTicketsResueltosEnTiempo() {
        System.out.println("\n=== VER TICKETS RESUELTOS POR TIEMPO ===");

        if (ticketsResueltos.raiz == null) {
            System.out.println("No hay tickets resueltos registrados aún.");
            return; // Vuelve automáticamente al menú principal
        }

        ticketsResueltos.mostrarTiemposDisponibles();

        System.out.print("\nIngrese el tiempo en segundos a consultar: ");
        int tiempoBuscado = leerNumero();

        ticketsResueltos.buscarTicketsPorTiempo(tiempoBuscado);
    }

    /**
     * Muestra todos los tickets pendientes en la cola junto con su información.
     */
    private static void verColaTickets() {
        if (colaTickets.estaVacia()) {
            System.out.println("\nNo hay tickets pendientes en la cola.");
            return;
        }

        System.out.println("\n=== TICKETS PENDIENTES ===");
        System.out.println("Cantidad de tickets pendientes: " + colaTickets.tamanio());

        NodoCola nodoActual = colaTickets.frente();
        int contador = 1;

        while (nodoActual != null) {
            Diccionario ticket = nodoActual.elemento;
            System.out.println("\nTicket #" + contador);
            System.out.println("ID: " + ticket.obtener("ticketId"));
            System.out.println("Cliente: " + ticket.obtener("nombreCliente"));
            System.out.println("Email: " + ticket.obtener("emailCliente"));
            System.out.println("Problema: " + ticket.obtener("descripcionProblema"));
            System.out.println("Fecha de creación: " + ticket.obtener("fechaCreacion"));
            System.out.println("------------------------");

            nodoActual = nodoActual.siguiente;
            contador++;
        }
    }

    /**
     * Asegura y valida que los valores ingresados sean validos para el menu y que
     * si se ingresan valores fuera del rango
     * como 'abc', 'cinco' o '@' no se rompa la ejecucion del programa.
     */
    private static int leerOpcionDeMenu(int minimo, int maximo) {
        int opcion = -1;
        boolean valida = false;

        while (!valida) {
            System.out.print("Ingrese una opción: ");
            String entrada = scanner.nextLine().trim();

            try {
                opcion = Integer.parseInt(entrada);
                if (opcion >= minimo && opcion <= maximo) {
                    valida = true;
                } else {
                    System.out.println("Opción fuera de rango. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número.");
            }
        }

        return opcion;
    }

    private static int leerNumero() {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada); // Si es válido, lo retorna
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número entero: ");
            }
        }
    }

    private static void iniciarSesion() {
        System.out.println("\n=== INICIAR SESIÓN ===");

        String id;
        do {
            System.out.print("Ingrese su ID: ");
            id = scanner.nextLine().trim().toUpperCase();
            if (estaVacio(id)) {
                System.out.println("El ID no puede estar vacío.");
            }
        } while (estaVacio(id));

        String password;
        do {
            System.out.print("Ingrese su contraseña: ");
            password = scanner.nextLine().trim();
            if (estaVacio(password)) {
                System.out.println("La contraseña no puede estar vacía.");
            }
        } while (estaVacio(password));

        if (empleados.containsKey(id)) {
            Empleado empleado = empleados.get(id);
            if (empleado.validarPassword(password)) {
                empleadoActual = empleado;
                System.out.println("Bienvenido, " + empleado.getNombre() + "!");
            } else {
                System.out.println("Contraseña incorrecta.");
            }
        } else {
            System.out.println("ID no encontrado.");
        }
    }


    private static void mostrarReportePorEmpleado() {
        System.out.println("\n=== REPORTE DE TICKETS POR EMPLEADO ===");

        if (ticketsResueltos.raiz == null) {
            System.out.println("No hay tickets resueltos registrados aún.");
            return;
        }

        ticketsResueltos.generarReportePorEmpleado(empleados);
    }

    /**
     * Método principal del sistema.
     * Controla el flujo del programa y la interacción con el usuario.
     */
    public static void main(String[] args) {
        inicializarSistema();
        generarTicketsIniciales();

        // 🔐 Bucle que obliga a iniciar sesión válida antes de avanzar
        while (empleadoActual == null) {
            iniciarSesion();
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcionDeMenu(0, 7);

            switch (opcion) {
                case 1:
                    procesarTickets();
                    break;
                case 2:
                    verColaTickets();
                    break;
                case 3:
                    System.out.println("\n=== TICKETS RESUELTOS POR TIEMPO ===");
                    ticketsResueltos.recorridoInorden();
                    break;
                case 4:
                    verTicketsResueltosEnTiempo();
                    break;
                case 5:
                    mostrarReportePorEmpleado();
                    break;
                case 6:
                    registrarNuevoTicket();
                    break;
                case 7:
                    System.out.println("\nCerrando sesión de " + empleadoActual.getNombre() + "...");
                    empleadoActual = null;

                    // 🔁 Requiere nuevo login para continuar
                    while (empleadoActual == null) {
                        iniciarSesion();
                    }
                    break;
                case 0:
                    System.out.println("\n¡Gracias por usar el sistema!");
                    break;
                default:
                    System.out.println("\nOpción inválida. Por favor, intente nuevamente.");
            }

        } while (opcion != 0);
    }
}