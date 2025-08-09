import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConsultaMoneda {
    private static final HttpClient client = HttpClient.newHttpClient();

    // Lista de monedas con sus IDs en CoinGecko
    private static final Set<String> CRYPTO_SOPORTADAS = Set.of(
            "btc", "eth", "ltc", "sol", "bnb", "usdt", "usdc"
    );

    private static final Set<String> FIAT_SOPORTADAS = Set.of(
            "usd", "eur", "mxn"
    );

    public Moneda convert(String from, String to, double amount) throws Exception {
        // Validar monedas primero
        if (!verificacionMoneda(from) || !verificacionMoneda(to)) {
            throw new IllegalArgumentException("Moneda no soportada. Criptos: " + CRYPTO_SOPORTADAS +
                    " | Fiat: " + FIAT_SOPORTADAS);
        }

        String fromId = getCoinGeckoId(from);
        String toId = getCoinGeckoId(to);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(
                        "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=%s",
                        fromId, toId)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error API: " + response.body());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        if (!json.has(fromId)) {
            throw new RuntimeException("Moneda origen no encontrada: " + from);
        }

        JsonObject rates = json.getAsJsonObject(fromId);

        if (!rates.has(toId)) {
            // Si no hay tasa directa, intentar conversión via USD
            return convertirViaUSD(from, to, amount);
        }

        double rate = rates.get(toId).getAsDouble();
        return new Moneda(from, to, amount, rate, amount * rate);
    }

    private Moneda convertirViaUSD(String from, String to, double amount) throws Exception {
        // Primero: FROM → USD
        Moneda toUsd = convert(from, "usd", amount);
        // Luego: USD → TO
        return convert("usd", to, toUsd.cantidadConvertida());
    }

    private boolean verificacionMoneda(String moneda) {
        String lower = moneda.toLowerCase();
        return CRYPTO_SOPORTADAS.contains(lower) || FIAT_SOPORTADAS.contains(lower);
    }

    private String getCoinGeckoId(String symbol) {
        return switch (symbol.toLowerCase()) {
            case "btc" -> "bitcoin";
            case "eth" -> "ethereum";
            case "ltc" -> "litecoin";
            case "sol" -> "solana";
            case "bnb" -> "binancecoin";
            case "usdt" -> "tether";
            case "usdc" -> "usd-coin";
            case "usd" -> "usd";
            case "eur" -> "eur";
            case "mxn" -> "mxn";
            default -> throw new IllegalArgumentException("Moneda no soportada: " + symbol);
        };
    }
}