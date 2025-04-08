package website.fernandoconde.messaging.client.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String jwtToken;
    private final String baseUrl = "http://localhost:8080"; // Anpassen falls nötig

    /**
     * Login mit Benutzername/Passwort
     */
    public boolean login(String username, String password) {
        try {
            // 1. JSON Request erstellen
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);
            String jsonRequest = mapper.writeValueAsString(credentials);

            // 2. Request senden
            HttpURLConnection conn = sendPostRequest(baseUrl + "/api/auth/login", jsonRequest);

            // 3. Response verarbeiten
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonNode response = mapper.readTree(conn.getInputStream());
                this.jwtToken = response.get("token").asText();
                return true;
            } else {
                logErrorResponse(conn);
            }
        } catch (IOException e) {
            System.err.println("Netzwerkfehler: " + e.getMessage());
        }
        return false;
    }

    /**
     * Authentifizierte Anfrage
     */
    public String getAuthenticatedData() throws IOException {
        if (jwtToken == null) {
            throw new IllegalStateException("Nicht eingeloggt!");
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(baseUrl + "/api/protected").openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + jwtToken);

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readResponse(conn);
        } else {
            logErrorResponse(conn);
            throw new IOException("HTTP " + conn.getResponseCode());
        }
    }

    // --- Hilfsmethoden ---
    private HttpURLConnection sendPostRequest(String url, String jsonBody) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }

    private String readResponse(HttpURLConnection conn) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    private void logErrorResponse(HttpURLConnection conn) throws IOException {
        System.err.println("Fehler: HTTP " + conn.getResponseCode());
        try (BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("Server sagt: " + errorLine);
            }
        }
    }

    public String getJwtToken() {
        return jwtToken;
    }
}