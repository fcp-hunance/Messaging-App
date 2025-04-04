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

A good Idea: Working with environment variables to update later urls/paths and passwords
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
# Spring Boot Application flow
## 1.Application Startup
```
sequenceDiagram
    participant Server
    participant WebSecurityConfig
    participant OAuth2Config
    participant Controllers

    Server->>WebSecurityConfig: Load security rules first
    Server->>OAuth2Config: Auto-configure OAuth2 client
    Server->>Controllers: Initialize REST endpoints
```
## 2.GitHub OAuth2 (Update to Add Normal login comes soon)
```
sequenceDiagram
    participant User
    participant Browser
    participant GitHub
    participant YourSpringBootApp

    User->>Browser: Visits /login/github
    Browser->>YourSpringBootApp: GET /login/github
    YourSpringBootApp->>GitHub: 302 Redirect to GitHub Auth
    User->>GitHub: Logs in & approves
    GitHub->>Browser: 302 Redirect to /api/auth/callback/github?code=XYZ
    Browser->>YourSpringBootApp: GET /api/auth/callback/github?code=XYZ
    YourSpringBootApp->>GitHub: Exchange code for token (POST)
    GitHub->>YourSpringBootApp: Returns access_token
    YourSpringBootApp->>GitHub: Get user info (with access_token)
    GitHub->>YourSpringBootApp: Returns user profile (login, email, etc.)
    YourSpringBootApp->>UserRepository: Save/update user data
    YourSpringBootApp->>Browser: Sets session cookie
```
## 3. Message FLow
```
sequenceDiagram
    participant DesktopApp
    participant SpringAPI
    participant Database

    DesktopApp->>SpringAPI: POST /api/auth/login (or OAuth2)
    SpringAPI->>DesktopApp: JWT/session cookie

    loop Message Sync
        DesktopApp->>SpringAPI: GET /api/messages/pending
        SpringAPI->>Database: Query undelivered messages
        Database->>SpringAPI: Return messages
        SpringAPI->>DesktopApp: Return messages as JSON
        
        DesktopApp->>SpringAPI: POST /api/messages/confirm {messageIds}
        SpringAPI->>Database: Mark messages as delivered
    end

    DesktopApp->>SpringAPI: POST /api/messages/send {recipient, content}
    SpringAPI->>Database: Save new message
```