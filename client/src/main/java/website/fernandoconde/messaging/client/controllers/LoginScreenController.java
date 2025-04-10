package website.fernandoconde.messaging.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import website.fernandoconde.messaging.client.network.ApiClient;
import java.io.IOException;


public class LoginScreenController {
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;
    @FXML private Label statusField;

    private final ApiClient apiClient = new ApiClient("http://10.101.186.28:8080");


    @FXML
    public void initialize() {
        userNameField.setOnKeyPressed(this::handleUserNameEnter);
        passwordField.setOnKeyPressed(this::handlePasswordEnter);
    }


    @FXML
    private void handleUserNameEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            passwordField.requestFocus(); // Von Benutzername zu Passwort springen
        }
    }

    @FXML
    private void handlePasswordEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onLogin(); // Login mit Enter im Passwortfeld auslösen
        }
    }


    @FXML
    private void onLogin() {  // Kleingeschriebenes 'o'
        String username = userNameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() && password.isEmpty()) {
            statusField.setText("Bitte Benutzername und Passwort eingeben!");
            userNameField.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            statusField.setText("Bitte Benutzername eingeben!");
            userNameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            statusField.setText("Bitte Passwort eingeben!");
            passwordField.requestFocus();
            return;
        }

        try {
            String token = apiClient.login(username, password);
            switchToContactChat(token);
        } catch (IOException e) {
            statusField.setText("Login fehlgeschlagen: " + e.getMessage());
        }
    }

    private void switchToContactChat(String token) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/website/fernandoconde/messaging/views/ContactChatScreen.fxml"));
            Parent root = loader.load();

            ContactChatController chatController = loader.getController();
            chatController.setToken(token); // Token übergeben

            Stage stage = (Stage) userNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MarFer");
            stage.show();

        } catch (IOException e) {
            statusField.setText("Fehler beim Laden des Chats: " + e.getMessage());
        }
    }
}