

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;

    public class CurrencyConverterClient {
        private static final String API_KEY = "e3785297910eaff452c50714"; // Tu clave API
        private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/e3785297910eaff452c50714/latest/USD";

        private final HttpClient httpClient;
        private final Gson gson;

        public CurrencyConverterClient() {
            this.httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            this.gson = new Gson();
        }

        public JsonObject getExchangeRate(String baseCurrency, String targetCurrency) throws IOException, InterruptedException {
            String url = BASE_URL + API_KEY + "/pair/" + baseCurrency + "/" + targetCurrency;
            System.out.println("URL: " + url); // Imprimir la URL para depuración

            // Crear la solicitud
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .header("User-Agent", "Java HttpClient")
                    .GET()
                    .build();

            // Enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
            handleResponse(response);

            // Procesar el cuerpo de la respuesta
            return gson.fromJson(response.body(), JsonObject.class);
        }

        private void handleResponse(HttpResponse<String> response) throws IOException {
            // Acceder al código de estado
            int statusCode = response.statusCode();
            System.out.println("Código de estado: " + statusCode);

            // Acceder a los encabezados
            System.out.println("Encabezados:");
            response.headers().map().forEach((key, value) -> System.out.println(key + ": " + value));

            // Manejar errores
            if (statusCode != 200) {
                throw new IOException("Código inesperado: " + statusCode + ", Respuesta: " + response.body());
            }
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduce la moneda base (por ejemplo, USD): ");
            String baseCurrency = scanner.nextLine().toUpperCase();
            System.out.println("Introduce la moneda objetivo (por ejemplo, EUR): ");
            String targetCurrency = scanner.nextLine().toUpperCase();
            System.out.println("Introduce la cantidad a convertir: ");
            double amount = scanner.nextDouble();

            CurrencyConverterClient client = new CurrencyConverterClient();
            try {
                JsonObject result = client.getExchangeRate(baseCurrency, targetCurrency);
                double conversionRate = result.get("conversion_rate").getAsDouble();
                double convertedAmount = amount * conversionRate;
                System.out.println(amount + " " + baseCurrency + " es igual a " + convertedAmount + " " + targetCurrency);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


