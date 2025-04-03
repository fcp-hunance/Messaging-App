package website.fernandoconde.messaging.client.network;
/// src/main/java/network/ApiClient.java
import java.net.http.*;
import java.net.URI;
import java.time.Duration;

public class ApiClient {
    private final HttpClient httpClient;
    private static final String BASE_URL = "http://localhost:8080/api";
    private boolean useMockMode = true; // Standardmäßig Mock für Tests

    public ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    // --- Mock-Modus (true = Testmodus, false = Echter Server) ---
    public void setMockMode(boolean enabled) {
        this.useMockMode = enabled;
    }

    // --- Login (Mock oder echt) ---
    public boolean login(String username, String password) throws Exception {
        if (useMockMode) {
            return mockLogin(username, password); // Test-Login
        } else {
            return realLogin(username, password); // Echter Server-Login
        }
    }

    // Mock-Login (immer true für "test"/"123")
    private boolean mockLogin(String username, String password) {
        return "test".equals(username) && "123".equals(password);
    }

    // Echter Login (HTTP-POST)
    private boolean realLogin(String username, String password) throws Exception {
        String requestBody = "username=" + username + "&password=" + password; // Einfache Form-Daten

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded") // Kein JSON!
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200; // Erfolg bei HTTP 200
    }

    public boolean testServerConnection() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ping")) // Endpoint muss auf Server existieren
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Server nicht erreichbar: " + e.getMessage());
            return false;
        }
    }
}