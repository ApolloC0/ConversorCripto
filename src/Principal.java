import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Set;

public class Principal {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ConsultaMoneda consulta = new ConsultaMoneda();

    // Set de monedas soportadas (símbolos)
    private static final Set<String> MONEDAS_SOPORTADAS = Set.of(
            "btc", "eth", "ltc", "sol", "bnb", "usdt", "usdc", "usd", "eur", "mxn");

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            try {
                mostrarMenu();
                int opcion = validarOpciones();

                switch (opcion) {
                    case 1 -> verPrecioCrypto();
                    case 2 -> convertirMoneda();
                    case 3 -> continuar = false;
                    default -> System.out.println("¡Opción no válida!");
                }

                if (continuar && !loopContinuar()) {
                    continuar = false;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Limpiar buffer
            }
        }
        System.out.println("\n¡Gracias por usar el Conversor Crypto!");
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("""
            ================================
            CONVERSOR CRYPTO By ApolloC0 (CoinGecko API)
          
            1. Ver precio de criptomoneda (USD)
            2. Convertir Monedas
            3. Salir
            ================================
            """);
    }

    private static int validarOpciones() {
        while (true) {
            try {
                System.out.print("Escoja una opción (1-3): ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                if (opcion >= 1 && opcion <= 3) {
                    return opcion;
                }
                System.out.println("Por favor ingrese un número del 1 al 3");
            } catch (InputMismatchException e) {
                System.out.println("¡Entrada inválida! Debe ser un número.");
                scanner.nextLine(); // Limpiar entrada incorrecta
            }
        }
    }

    private static boolean loopContinuar() {
        while (true) {
            System.out.print("\n¿Desea hacer otra operación? (si/no): ");
            String respuesta = scanner.nextLine().toLowerCase();
            if (respuesta.equals("si") || respuesta.equals("s") || respuesta.equals("yes")
                    || respuesta.equals("y")) {
                return true;
            } else if (respuesta.equals("no") || respuesta.equals("n")) {
                return false;
            }
            System.out.println("Entrada no válida");
        }
    }

    private static void verPrecioCrypto() {
        System.out.print("\nIngrese símbolo crypto (BTC/ETH/SOL/etc): ");
        String crypto = scanner.nextLine().toLowerCase();

        try {
            Moneda resultado = consulta.convert(crypto, "usd", 1);
            System.out.printf("\n1 %s = %.2f USD\n",
                    crypto.toUpperCase(), resultado.tasaDeConversion());
        } catch (Exception e) {
            System.out.println("Error al obtener precio: " + e.getMessage());
        }
    }

    private static void convertirMoneda() {
        String origen = obtenerMonedaValida("\nIngrese la moneda que desea convertir (ej. USD/BTC/ETH): ");
        String destino = obtenerMonedaValida("Convertir a: (ej. USD/EUR/BTC): ");
        double cantidad = calculoDeConversion();

        try {
            Moneda resultado = consulta.convert(origen, destino, cantidad);
            System.out.printf("\n%.2f %s = %.8f %s\n",
                    cantidad, origen.toUpperCase(),
                    resultado.cantidadConvertida(), destino.toUpperCase());
        } catch (Exception e) {
            System.out.println("Error en la conversión: " + e.getMessage());
        }
    }

    private static String obtenerMonedaValida(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String moneda = scanner.nextLine().trim().toLowerCase();

            if (MONEDAS_SOPORTADAS.contains(moneda)) {
                return moneda;
            }

            System.out.println("¡Error! Moneda no soportada. Las opciones son:");
            System.out.println("Cripto: BTC, ETH, LTC, SOL, BNB, USDT, USDC");
            System.out.println("FIAT: USD, EUR, MXN");
            System.out.print("Por favor ingrese un símbolo válido: ");
        }
    }

    private static double calculoDeConversion() {
        while (true) {
            try {
                System.out.print("Cantidad: ");
                double cantidad = scanner.nextDouble();
                scanner.nextLine(); // Limpiar buffer

                if (cantidad <= 0) {
                    System.out.println("¡La cantidad debe ser mayor que 0!");
                    continue;
                }

                return cantidad;
            } catch (InputMismatchException e) {
                System.out.println("¡Error! Debe ingresar un número válido (ej. 100,50)");
                scanner.nextLine(); // Limpiar entrada incorrecta
            }
        }
    }
}

