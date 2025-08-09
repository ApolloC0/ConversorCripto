public record Moneda(
        String fromCurrency,
        String toCurrency,
        double cantidad,
        double tasaDeConversion,
        double cantidadConvertida
) {}