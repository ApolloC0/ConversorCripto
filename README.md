Conversor de Criptomonedas

Este conversor utiliza la API de CoinGecko para obtener tasas de cambio en tiempo real.

📌 Características

✅ Conversión entre criptomonedas y monedas FIAT (BTC, ETH, LTC, SOL, USDT, USD, EUR, MXN, etc.)

✅ Consulta de precios actualizados en USD

✅ Manejo de errores para entradas inválidas

✅ Generación de archivos JSON con los resultados de conversión

🚀 Cómo Usarlo

1. Requisitos

Java JDK 17 o superior

Maven (para gestión de dependencias)

Conexión a Internet (para consultar la API de CoinGecko)

2. Instalación

Clona el repositorio o descarga los archivos:

bash
git clone https://github.com/tu-usuario/ConversorCripto.git
cd ConversorCripto

3. Compilación y Ejecución

Con Maven:
bash
mvn clean compile
mvn exec:java -Dexec.mainClass="Principal"
Sin Maven (compilación manual):
bash
javac -cp ".;gson-2.10.1.jar" src/*.java -d out/
java -cp "out;gson-2.10.1.jar" Principal
📋 Métodos Principales

1. ConsultaMoneda.java

Clase encargada de interactuar con la API de CoinGecko.

Métodos clave:
convert(String from, String to, double amount)

Realiza la conversión entre dos monedas.

Si no hay una tasa directa (ej: LTC → USDT), hace una conversión intermedia usando USD.

convertirViaUSD(String from, String to, double amount)

Convierte primero a USD y luego a la moneda destino.

getCoinGeckoId(String symbol)

Mapea símbolos (BTC, ETH) a los IDs de CoinGecko (bitcoin, ethereum).

2. Principal.java

Interfaz de línea de comandos (CLI) para el usuario.

Opciones del menú:

Ver precio de una criptomoneda en USD

Ejemplo: 1 BTC = 50,000 USD

Convertir monedas

Ejemplo: 10 LTC → USDT

Salir del programa

3. Moneda.java (Record)
Almacena los datos de la conversión:

java

public record Moneda(
    String fromCurrency,
    String toCurrency,
    double amount,
    double conversionRate,
    double convertedAmount
) {}

4. GeneradorDeArchivo.java
Guarda los resultados en un archivo JSON.

Ejemplo: BTC_a_USD_20231006_1425.json

🔌 API Utilizada
El proyecto usa la API gratuita de CoinGecko:
🔗 https://www.coingecko.com/en/api

Endpoints usados:

/simple/price

Ejemplo: https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd

⚠️ Posibles Errores y Soluciones

"Moneda no soportada": Verifica que el símbolo esté en la lista (BTC, ETH, USDT, USD, etc.).

"Error API: Too many requests": CoinGecko tiene un límite de solicitudes (50/min en el plan gratuito).

"Error en la conversión": Algunas monedas requieren conversión indirecta (ej: LTC → USDT se hace vía USD).

Nota adicional: la API de Coingecko fue preferida para este proyecto ya que no requiere del uso de una API key específica, facilitando el futuro mantenimiento e implementación de nuevas caracteristicas.
