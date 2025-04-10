package website.fernandoconde.messaging.client.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import website.fernandoconde.messaging.client.model.Contact;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
            os.write(jsonInput.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() == 200) {
            JsonNode response = objectMapper.readTree(conn.getInputStream());
            return response.get("token").asText();
        } else {
            throw new IOException("Login failed: " + conn.getResponseCode());
        }
    }

    // Alle Kontakte laden
    public List<String> getContacts(String token) throws IOException {
        List<String> contacts = new ArrayList<>();
        Contact user2 = new Contact("user2");
        contacts.add(user2.getUsername()); //fixme users need to be found
        URL url = new URL(apiUrl + "/contacts");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        if (conn.getResponseCode() == 200) {
            JsonNode response = objectMapper.readTree(conn.getInputStream());
            response.forEach(node -> contacts.add(node.get("username").asText()));
        } else {
            throw new IOException("Failed to fetch contacts: " + conn.getResponseCode());
        }

        return contacts;
    }

    // Kontakt hinzufügen
    public boolean addContact(String token, String username) throws IOException {
        URL url = new URL(apiUrl + "/contacts/add");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = String.format("{\"username\":\"%s\"}", username);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() != 200) {
            throw new IOException("Failed to add contact: " + conn.getResponseCode());
        }
        return false;
    }

    // Nachrichten abrufen
    public List<String> getMessages(String token, String contact) throws IOException {
        List<String> messages = new ArrayList<>();

        URL url = new URL(apiUrl + "/messages?contact=" + contact);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        if (conn.getResponseCode() == 200) {
            JsonNode response = objectMapper.readTree(conn.getInputStream());
            for (JsonNode msg : response) {
                String sender = msg.get("sender").asText();
                String text = msg.get("text").asText();
                messages.add(sender + ": " + text);
            }
        } else {
            throw new IOException("Failed to fetch messages: " + conn.getResponseCode());
        }

        return messages;
    }

    // Nachricht senden
    public void sendMessage(String token, String recipient, String message) throws IOException {
        URL url = new URL(apiUrl + "/messages/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = String.format("{\"recipient\":\"%s\",\"message\":\"%s\"}", recipient, message);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() != 200) {
            throw new IOException("Failed to send message: " + conn.getResponseCode());
        }
    }
}
