package website.fernandoconde.messaging.client.network;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONObject;

public class ApiClients {
    private final String apiUrl;

    public ApiClients(String apiUrl) {
        this.apiUrl = "http://localhost:8080/api/auth/login";
    }

    /**
     * Sendet Login-Daten als JSON an den Server, um sich für den Messenger anzumelden.
     *
     * @param username Benutzername
     * @param password Passwort

     * @return Antwort des Servers
     * @throws IOException Wenn ein Fehler bei der Kommunikation auftritt
     */
    public String login(String username, String password) throws IOException {
        // JSON-Objekt erstellen
        JSONObject jsonData = new JSONObject();
        jsonData.put("username", username);
        jsonData.put("password", password);
        System.out.println(jsonData);


        // HTTP-Verbindung vorbereiten
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.println("Mit dieser URL wird verbunden: " + apiUrl);
        try {
            // Verbindung konfigurieren
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            // connection.setRequestProperty("Content-Type", "application/json");
            // connection.setRequestProperty("Accept", "application/json");


            // JSON-Daten senden
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (Exception e) {
                System.err.println("Error while sending request: " + e.getMessage());
                e.printStackTrace();
            }

            // Antwort vom Server lesen
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } else {
                // Fehlerantwort lesen, falls vorhanden
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    throw new IOException("Server returned HTTP response code: " + responseCode
                            + "\nError details: " + errorResponse.toString());
                }
            }
        } finally {
            connection.disconnect();
        }
    }


}