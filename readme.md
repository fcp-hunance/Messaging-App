# Messaging App

## Version 0.3
A full-stack messaging application with Spring Boot backend and JavaFX desktop client, supporting OAuth2 GitHub login, contact management, and message exchange.
## Feature

User Registration and Login:
- Register and login using OAuth2 GitHub authentication.
- Login with pre-saved credentials in the database.
Contacts Management:
- Add and save contacts by username (check if the user exists in the database before adding).
- Delete contacts using the username.
Messaging:
- Send messages to other users by username.
- Messages are saved both locally and on the server database as temporary data, if the client is offline.
JavaFX Desktop Client:
- Built-in login screen.
- Real-time messaging view.
- Auto-polling for new messages every 3 seconds.
- Predefined contact for demo purposes.

## Server Setup
### Prerequisites:
Ensure the following are installed on your system before running the application:
- Java 11 or higher
- Maven for dependency management
- MySQL (or any other preferred database)
- GitHub OAuth2 credentials for GitHub login integration

### Installation:
1. Clone the repository:
```bash
git clone <your-github-repo-url>
cd <your-repo-directory>
```
2. Configure application properties: The application expects certain variables to be set in the application.properties file. You need to add your credentials and configuration. Below are the necessary variables:
```properties
server.port=8080
spring.security.oauth2.client.registration.github.client-id=<your-github-client-id>
spring.security.oauth2.client.registration.github.client-secret=<your-github-client-secret>
spring.security.oauth2.client.registration.github.scope=read:user, user:email
spring.security.oauth2.client.registration.github.redirect-uri=<your-redirect-uri>

spring.datasource.url=<your-db-path>
spring.datasource.username=<your-db-username>
spring.datasource.password=<your-db-password>

app.jwt.secret=<your-jwt-secret>
app.jwt.issuer=<your-issuer>
```
Replace `<your-github-client-id>`, `<your-github-client-secret>`, and the database credentials with your actual values.  
Ensure the database (MySQL or other) is running and configured.

3. Run the application: Once you have the repository set up and configured, you can start the application with the following command:
```bash
mvn spring-boot:run
```
This will start the server at http://localhost:8080.

## **JavaFX Client**
### Path
All JavaFX client logic is located under:
`src/main/java/website/fernandoconde/messaging/client/`

### Login Screen
Handles username/password input. On Enter, logs in and switches to the chat view.
```java
String token = apiClient.login(username, password);
switchToContactChat(token);
```

### Chat View
- Single predefined contact: "<Usertest>"
- Messages sent via API
- Polling undelivered messages every 3 seconds
- Messages displayed in chatArea

### Message Polling Example
```java
messagePoller.scheduleAtFixedRate(() -> {
    List<String> newMessages = apiClient.pollUndeliveredMessages(currentToken);
    Platform.runLater(() -> newMessages.forEach(msg -> 
        chatArea.appendText(FIXED_CONTACT + ": " + msg + "\n")));
}, 0, 3, TimeUnit.SECONDS);
```

## API Endpoints:
1. Login with Username and Password:
- URL: `http://localhost:8080/api/auth/login
- Method: `POST`
- Request Body (JSON):
```json
{
    "username": "testuser1",
    "password": "testpassword1"
}   
```
* Response:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoidGVzdHVzZXIiLCJpc3MiOiJtZXNzYWdpbmctYXBwLW1hcmZlciIsImlhdCI6MTc0NDA1MzIwMCwiZXhwIjoxNzQ0MTM5NjAwfQ.gMnRPpdmuDvOftVCna0JI5KRFh78xgDnIf_oeHEo6GA"
}
```
2. OAuth2 Login with Github:
- URL: `http://localhost:8080/oauth2/authorization/github`
- Method: `GET`
- Response (after GitHub login):
```json
{"token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZmNwLWh1bmFuY2UiLCJpc3MiOiJtZXNzYWdpbmctYXBwLW1hcmZlciIsImlhdCI6MTc0NDA5OTc1NywiZXhwIjoxNzQ0MTg2MTU3fQ.xK_1xHXqdym8CIobrymPuAFLhlukK60J_32aaRatI2I",
"userId": "27f279b4-13e1-4e04-8b16-d301e8be0cb9"}
```
3. Send Message:
- URL: `http://localhost:8080/message/send`
- Method: `POST`
- Headers:
    - Authorization-Type: Bearer <JWT_TOKEN> 
- Request Body (JSON):
```json
{
    "username": "user2",
    "content": "Welcome! This is a test." 
}
```
- Response:
```json
{
    "content": "Welcome! This is a test.",
    "messageId": "e5571de8-c129-4693-af61-b8e018699d65",
    "recipient": "user2",
    "timestamp": "2025-04-08T13:45:16.3691284"
}
```
4. Get undelivered Messages:
- URL: `http://localhost:8080/message/undelivered`
- Method: `GET`
- Headers:
    - Authorization-Type: Bearer <JWT_TOKEN>  
- Response:
```json
[
    {
        "recipient": "user2",
        "id": "e5571de8-c129-4693-af61-b8e018699d65",
        "content": "Welcome! This is a test.",
        "timestamp": "2025-04-08T13:45:16.369128"
    },
    {
        "recipientId": "user2",
        "id": "e5571de8-c129-4693-af61-b8e018699d65",
        "content": "Another message.",
        "timestamp": "2025-04-08T13:45:16.369128"
    }
]
```
5. Check if User Exits:
- URL: `http://localhost:8080/api/users/find`
- Method: `GET`
- Headers:
    - Authorization-Type: Bearer <JWT_TOKEN>  
- Request Body (JSON):
```json
{
    "username": "user2"
}
```
- Response:
```json
{
    "message": "User not found",
    "status": 404
}
```
```json
{
    "message": "User exists in DB",
    "status": 200
}
```
## Testing & Usage:
You can test the login using Postman or any API testing tool by sending requests to the URLs mentioned above.  
Important: Make sure you are passing the correct Authorization token (either from the login or GitHub OAuth) as a bearer token in the request headers.

## Future Improvements
- Add contact search in client
- Multi-user chat
- Persistent chat history

## License

This project is licensed under the [MIT License](LICENSE.txt).
