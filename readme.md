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
Request JSON to Login.
* First create a TestUser in DB with a Post request to http://localhost:8080/api/test/create-test-user:
```
// Test User will be created with a Post request
@PostMapping("/test/create-test-user")
    public String createUser() {
        User user = User.createLocalUser(
                "testuser",
                "test@example.com",
                passwordEncoder.encode("testpassword"),
                UserRole.ROLE_USER
        );
        userRepository.save(user);
        return "Test user created!";
    }
```
* Payload/Body in JSON Request to http://localhost:8080/api/auth/login:
```
{
    "username": "testuser",
    "password": "testpassword"
}   
```
* Response JWT with token in Payload:
```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoidGVzdHVzZXIiLCJpc3MiOiJtZXNzYWdpbmctYXBwLW1hcmZlciIsImlhdCI6MTc0NDA1MzIwMCwiZXhwIjoxNzQ0MTM5NjAwfQ.gMnRPpdmuDvOftVCna0JI5KRFh78xgDnIf_oeHEo6GA"
}
```
## Ideas


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