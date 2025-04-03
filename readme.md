# Messaging App
## Version 0.1


## Feature
* Register and Login New User (Server prove to unique Username)
* Add and save Contacts by Username
* delete Contacts with id (id will be fetch after successfully added from Contact)
* Messaging to other Users by username.
* Messages will be saved locally and in server Database as temporal Data, if the client is Offline.

## Extra

## Annotations
* Claims in JWT () {"roles": ["ADMIN", "USER"], "email": "user@example.com"}


```
    
```

## Ideas
Try to implement GitHub Login in JavaFX Controller:
```
public class OAuth2Login {

    public static void loginWithGitHub() {
        try {
            URI uri = new URI("http://localhost:8080/auth/login/github");
            Desktop.getDesktop().browse(uri); // Opens system browser
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//And Retrieves the Access Token after login:

public class OAuth2TokenFetcher {

    public static String fetchAccessToken() {
        try {
            URL url = new URL("http://localhost:8080/auth/callback/google");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Extract token from JSON response (use a JSON parser for better handling)
            return response.contains("token") ? response.split("\"token\":\"")[1].split("\"")[0] : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

Use API Requests with token

public class SecureRequest {

    public static String sendMessage(String token, String message) {
        try {
            URL url = new URL("http://localhost:8080/messages/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setDoOutput(true);
            conn.getOutputStream().write(("message=" + message).getBytes());

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

