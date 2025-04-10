//package website.fernandoconde.messaging.client.service;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//
//public class MessageReceiver {
//
//    private final String apiUrl;
//    private final String jwtToken;
//
//    public MessageReceiver(String apiUrl, String jwtToken) {
//        this.apiUrl = apiUrl;
//        this.jwtToken = jwtToken;
//    }
//
////    public void pollMessages() throws IOException {
////        String receiveUrl = apiUrl + "/message/undelivered";
////
////        HttpURLConnection connection = (HttpURLConnection) new URL(receiveUrl).openConnection();
////        connection.setRequestMethod("GET");
////        connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
////        connection.setRequestProperty("Accept", "application/json");
////
////        int status = connection.getResponseCode();
////
////        if (status == 200) {
////            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
////            StringBuilder response = new StringBuilder();
////            String line;
////            while ((line = br.readLine()) != null) {
////                response.append(line);
////            }
////
////            JSONArray messages = new JSONArray(response.toString());
////
////            if (messages.isEmpty()) {
////                System.out.println("Keine neuen Nachrichten.");
////            } else {
////                for (int i = 0; i < messages.length(); i++) {
////                    JSONObject msg = messages.getJSONObject(i);
////                    System.out.println("Nachricht: " + msg.getString("content"));
////                    System.out.println("--------------");
////                }
////            }
////
////        } else {
////            System.out.println("Fehler beim Empfangen der Nachrichten. Code: " + status);
////        }
////    }
//}
