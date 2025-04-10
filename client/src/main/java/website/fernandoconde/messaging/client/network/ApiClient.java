package website.fernandoconde.messaging.client.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {

    private final String apiUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    // LOGIN
    public String login(String username, String password) throws IOException {
        URL url = new URL(apiUrl + "/api/auth/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() == 200) {
            JsonNode response = objectMapper.readTree(conn.getInputStream());
            return response.get("token").asText();
        } else {
            throw new IOException("Login failed: " + conn.getResponseCode());
        }
    }



    // Neue Nachrichten (ungeliefert) abrufen -> wie MessageReceiver
    public List<String> pollUndeliveredMessages(String token) throws IOException {
        List<String> messages = new ArrayList<>();

        URL url = new URL(apiUrl + "/message/undelivered");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            JsonNode json = objectMapper.readTree(response.toString());
            if (json.isEmpty()) {
                System.out.println("Keine neuen Nachrichten.");
            } else {
                for (JsonNode msg : json) {
                    String content = msg.get("content").asText();
                    messages.add(content);
                    System.out.println("Nachricht: " + content);
                    System.out.println("--------------");
                }
            }
        } else {
            throw new IOException("Fehler beim Empfangen der Nachrichten: " + conn.getResponseCode());
        }

        return messages;
    }

    // Nachricht senden
    public void sendMessage(String token, String recipient, String message) throws IOException {
        URL url = new URL(apiUrl + "/message/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = String.format("{\"username\":\"%s\",\"content\":\"%s\"}", recipient, message);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() == 201) {
            System.out.println("Nachricht erfolgreich gesendet.");
        } else {
            throw new IOException("Failed to send message: " + conn.getResponseCode());
        }
    }
}
