//package website.fernandoconde.messaging.client.service;
//
//import org.json.JSONObject;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//public class TokenManager {
//
//    private final String apiUrl;
//    private final String username;
//    private final String password;
//    private String jwtToken;
//
//    public TokenManager(String apiUrl, String username, String password) {
//        this.apiUrl = apiUrl;
//        this.username = username;
//        this.password = password;
//    }
//
//    public String getToken() throws IOException {
//        if (jwtToken == null || isTokenExpired(jwtToken)) {
//            jwtToken = loginAndGetToken();
//        }
//        return jwtToken;
//    }
//
//    private String loginAndGetToken() throws IOException {
//        URL url = new URL(apiUrl + "/api/auth/login");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setDoOutput(true);
//
//        JSONObject json = new JSONObject();
//        json.put("username", username);
//        json.put("password", password);
//
//        try (OutputStream os = connection.getOutputStream()) {
//            os.write(json.toString().getBytes(StandardCharsets.UTF_8));
//        }
//
//        int status = connection.getResponseCode();
//
//        if (status == 200) {
//            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String response = br.readLine();
//            JSONObject responseJson = new JSONObject(response);
//            return responseJson.getString("token");
//        } else {
//            throw new IOException("Login fehlgeschlagen! Status: " + status);
//        }
//    }
//
//    private boolean isTokenExpired(String token) {
//        try {
//            String[] parts = token.split("\\.");
//            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
//            JSONObject json = new JSONObject(payload);
//            long exp = json.getLong("exp");
//            long now = System.currentTimeMillis() / 1000;
//            return now > exp;
//        } catch (Exception e) {
//            return true; // Sicher ist sicher
//        }
//    }
//}
