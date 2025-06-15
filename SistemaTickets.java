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

    private static void inicializarSistema() {
        colaTickets = new Cola();
        ticketsResueltos = new ABB();
        scanner = new Scanner(System.in);
        random = new Random();
    }

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

    private static void procesarTickets() {
        while (!colaTickets.estaVacia()) {
            Diccionario ticketActual = colaTickets.desencolar();
            mostrarTicket(ticketActual);
            tiempoInicioDeSolucion = System.currentTimeMillis();

            System.out.println("\nIngresa 'Y' cuando el ticket esté resuelto");
            while (!scanner.nextLine().toUpperCase().equals("Y")) {
                System.out.println("Por favor, ingresa 'Y' cuando el ticket esté resuelto");
            }

            long tiempoResolucion = (System.currentTimeMillis() - tiempoInicioDeSolucion) / 1000;
            ticketsResueltos.insertar((int) tiempoResolucion, ticketActual);
            System.out.println("Ticket resuelto en " + tiempoResolucion + " segundos");

            System.out.println("\nPresione cualquier tecla para continuar. Para volver al menu ingrese 'Q'.");
            String entrada = scanner.nextLine().toUpperCase();
            if (entrada.equals("Q")) {
                break;
            }
        }

        System.out.println("\nNo hay más tickets pendientes.");
    }

    private static void mostrarTicket(Diccionario ticket) {
        System.out.println("\n=== TICKET ACTUAL ===");
        System.out.println("ID: " + ticket.obtener("ticketId"));
        System.out.println("Cliente: " + ticket.obtener("nombreCliente"));
        System.out.println("Email: " + ticket.obtener("emailCliente"));
        System.out.println("Problema: " + ticket.obtener("descripcionProblema"));
        System.out.println("Fecha de creación: " + ticket.obtener("fechaCreacion"));
        System.out.println("===================");
    }

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
        System.out.print("Ingrese una opción: ");
    }

    private static void registrarNuevoTicket() {
        Diccionario ticket = new Diccionario();
        scanner.nextLine();

        System.out.println("\n=== REGISTRO DE NUEVO TICKET ===");

        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();

        System.out.print("Ingrese la descripción del problema: ");
        String problema = scanner.nextLine();

        ticket.insertar("ticketId", String.valueOf(colaTickets.tamanio() + 1));
        ticket.insertar("nombreCliente", nombre);
        ticket.insertar("emailCliente", email);
        ticket.insertar("descripcionProblema", problema);
        ticket.insertar("fechaCreacion",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        colaTickets.encolar(ticket);
        System.out.println("\n¡Ticket registrado exitosamente!");
    }

    private static void verTicketsResueltosEnTiempo() {
        System.out.print("\nIngrese el tiempo en segundos: ");
        int tiempoBuscado = scanner.nextInt();

        System.out.println("\n=== TICKETS RESUELTOS EN " + tiempoBuscado + " SEGUNDOS ===");
        ticketsResueltos.buscarTicketsPorTiempo(tiempoBuscado);
    }

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

    public static void main(String[] args) {
        inicializarSistema();
        generarTicketsIniciales();

        int opcion;
        do {
            mostrarMenu();
            opcion = scanner.nextInt();

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
                default:
                    System.out.println("\nOpción inválida. Por favor, intente nuevamente.");
            }
        } while (opcion != 0);
    }
}