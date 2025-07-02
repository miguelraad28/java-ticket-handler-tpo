package sistema;

import java.util.Scanner;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SistemaTickets {
    private static Cola colaTickets;
    private static ABB ticketsResueltos;
    private static Scanner scanner;
    private static Random random;
    private static long tiempoInicioDeSolucion;

/**
 * Inicializa las estructuras del sistema: cola de tickets, árbol de tickets resueltos,
 * scanner para entrada por consola y generador aleatorio.
 */
    private static void inicializarSistema() {
        colaTickets = new Cola();
        ticketsResueltos = new ABB();
        scanner = new Scanner(System.in);
        random = new Random();
    }

/**
 * Genera automáticamente 5 tickets con datos aleatorios y los encola como pendientes.
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
 * Permite procesar uno o más tickets de la cola hasta que se resuelvan o el usuario decida salir.
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

            String entrada = "";
            System.out.println("\n¿Desea resolver este ticket ahora? (Y para resolver, Q para volver al menú)");

            // Asegura que se lea una línea no vacía
            while (entrada.isEmpty()) {
                entrada = scanner.nextLine().trim().toUpperCase();
            }

            while (!entrada.equals("Y") && !entrada.equals("Q")) {
                System.out.println("Entrada inválida. Ingrese 'Y' para resolver o 'Q' para volver al menú:");
                entrada = scanner.nextLine().trim().toUpperCase();
                while (entrada.isEmpty()) {
                    entrada = scanner.nextLine().trim().toUpperCase();
                }
            }

            if (entrada.equals("Q")) {
                colaTickets.encolar(ticketActual);  // lo devolvemos
                break;
            }

            long tiempoResolucion = (System.currentTimeMillis() - tiempoInicioDeSolucion) / 1000;
            ticketsResueltos.insertar((int) tiempoResolucion, ticketActual);
            System.out.println("Ticket resuelto en " + tiempoResolucion + " segundos.\n");

            if (colaTickets.estaVacia()) {
                System.out.println("Todos los tickets han sido resueltos.");
                break;
            }

            System.out.println("¿Desea resolver otro ticket? (Y para continuar, cualquier otra tecla para volver al menú):");
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
 * Muestra en consola los detalles de un ticket (ID, cliente, email, problema, fecha).
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
        System.out.println("1) Empezar jornada para resolver tickets");
        System.out.println("2) Ver cola de tickets y cantidad pendientes");
        System.out.println("3) Ver tickets resueltos por tiempo");
        System.out.println("4) Ver todos los tickets resueltos en X tiempo");
        System.out.println("******************************************************");
        System.out.println("5) Registrar nuevo ticket como en calidad de cliente");
        System.out.println("0) Salir");
        System.out.println("=====================================");
    }

    /**
 * Permite a un cliente registrar un nuevo ticket manualmente, validando los campos.
 */
    private static void registrarNuevoTicket() {
        Diccionario ticket = new Diccionario();

        System.out.println("\n=== REGISTRO DE NUEVO TICKET ===");

        String nombre;
        do {
            System.out.print("Ingrese su nombre: ");
            nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
            }
        } while (nombre.isEmpty());

        String email;
        do {
            System.out.print("Ingrese su email: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("El email no puede estar vacío.");
            }
        } while (email.isEmpty());

        String problema;
        do {
            System.out.print("Ingrese la descripción del problema: ");
            problema = scanner.nextLine().trim();
            if (problema.isEmpty()) {
                System.out.println("La descripción del problema no puede estar vacía.");
            }
        } while (problema.isEmpty());

        ticket.insertar("ticketId", String.valueOf(colaTickets.tamanio() + 1));
        ticket.insertar("nombreCliente", nombre);
        ticket.insertar("emailCliente", email);
        ticket.insertar("descripcionProblema", problema);
        ticket.insertar("fechaCreacion", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        colaTickets.encolar(ticket);
        System.out.println("\n¡Ticket registrado exitosamente!");
    }

    /**
 * Solicita un tiempo al usuario y busca todos los tickets resueltos con ese tiempo exacto.
 */
    private static void verTicketsResueltosEnTiempo() {
        System.out.println("\n=== VER TICKETS RESUELTOS POR TIEMPO ===");

        if (ticketsResueltos.raiz == null) {
            System.out.println("No hay tickets resueltos registrados aún.");
            return; // 🔁 Vuelve automáticamente al menú principal
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
 * Lee y valida la opción ingresada en el menú, asegurándose de que sea un número válido.
 * Evita que se rompa el programa por entradas inválidas como letras o símbolos.
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
                return Integer.parseInt(entrada); // ✔️ Si es válido, lo retorna
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número entero: ");
            }
        }
    }

    /**
 * Método principal del sistema.
 * Controla el flujo del programa y la interacción con el usuario mediante el menú.
 */
    public static void main(String[] args) {
        inicializarSistema();
        generarTicketsIniciales();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcionDeMenu(0, 5); // Cambiá 5 si tenés más opciones

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
                    registrarNuevoTicket();
                    break;
                case 0:
                    System.out.println("\n¡Gracias por usar el sistema!");
                    break;
                default :
                    System.out.println("\nOpción inválida. Por favor, intente nuevamente.");
            }
        } while (opcion != 0);
    }
}