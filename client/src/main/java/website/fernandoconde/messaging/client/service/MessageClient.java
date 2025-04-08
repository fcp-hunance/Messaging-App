package website.fernandoconde.messaging.client.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONObject;

public class MessageClient {
    private final String jwtToken;

    public MessageClient(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void sendMessage(String recipient, String message) throws IOException {
        URL url = new URL("http://localhost:8080/message/send");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("recipient", recipient);
        json.put("message", message);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.toString().getBytes(StandardCharsets.UTF_8));
        }

        int status = connection.getResponseCode();
        if (status == 200) {
            System.out.println("Nachricht erfolgreich gesendet.");
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            System.out.println("Fehler beim Senden der Nachricht. Code: " + status);
            System.out.println("Server Response: " + response);
        }

    }
}
