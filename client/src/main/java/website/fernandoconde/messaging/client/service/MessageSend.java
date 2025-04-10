package website.fernandoconde.messaging.client.service;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MessageSend {

    private final String apiUrl;
    private final String jwtToken;

    public MessageSend(String apiUrl, String jwtToken) {
        this.apiUrl = apiUrl;
        this.jwtToken = jwtToken;
    }

    public void sendMessage(String recipient, String message) throws IOException {
        String sendUrl = apiUrl + "/message/send";

        HttpURLConnection connection = (HttpURLConnection) new URL(sendUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("username", recipient);
        json.put("content", message);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.toString().getBytes(StandardCharsets.UTF_8));
        }

        int status = connection.getResponseCode();
        if (status == 201) {
            System.out.println("Nachricht erfolgreich gesendet.");
        } else {
            System.out.println("Fehler beim Senden der Nachricht. Code: " + status);
        }
    }
}
