import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class WeatherServiceExercise {

    record WeatherData(String location, String jsonData) {
        @Override
        public String toString() {
            return "Weather in %s: %s"
                    .formatted(location, jsonData);
        }
    }

    static class WeatherClient {
        private final HttpClient client;
        private static final String API_BASE_URL = "https://api.open-meteo.com/v1/forecast";

        public WeatherClient() {
            this.client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();
        }

        public WeatherData getWeather(String location, double latitude, double longitude) throws Exception {
            System.out.println("getWeather");

            String url = String.format(
                    "%s?latitude=%.4f&longitude=%.4f&current_weather=true",
                    API_BASE_URL,
                    latitude,
                    longitude
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            System.out.println("Request: " + request);
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("Response code: " + response.statusCode());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "API error: " + response.statusCode()
                );
            }

            return new WeatherData(location, response.body());
        }

        public CompletableFuture<WeatherData> getWeatherAsync(String location, double latitude, double longitude) {
            String url = String.format(
                    "%s?latitude=%.4f&longitude=%.4f&current_weather=true",
                    API_BASE_URL,
                    latitude,
                    longitude
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        if (response.statusCode() != 200) {
                            throw new RuntimeException(
                                    "API error: " + response.statusCode()
                            );
                        }
                        return new WeatherData(location, response.body());
                    });
        }
    }

    public static void main(String[] args) throws Exception {
        WeatherClient client = new WeatherClient();

        // Synchronous call - London coordinates
        WeatherData syncWeatherCallResult = client.getWeather("London", 51.5074, -0.1278);
        System.out.println("Sync result: " + syncWeatherCallResult);
        // Asynchronous call - Paris coordinates
        client.getWeatherAsync("Paris", 48.8566, 2.3522)
                .thenAccept(weather -> System.out.println("Async result: " + weather))
                .exceptionally(e -> {
                    System.err.println("Async error: " + e.getMessage());
                    return null;
                });

        // Wait a bit for async operation
        Thread.sleep(1000);
    }
}
