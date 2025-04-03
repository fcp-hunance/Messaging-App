package website.fernandoconde.messaging.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import website.fernandoconde.messaging.client.network.ApiClient;

public class LoginScreenController {

    @FXML
    private TextField passwordField;

    @FXML
    private TextField userNameField;

    @FXML
    void OnLogin(ActionEvent event) {
        ApiClient client = new ApiClient();
        client.setMockMode(false); // Echten Server-Login aktivieren

        try {
            boolean success = client.login("test", "123"); // Ruft intern realLogin() auf
            System.out.println(success ? "Erfolg!" : "Fehler!");
        } catch (Exception e) {
            System.err.println("Netzwerkfehler: " + e.getMessage());
        }

    }

    @FXML
    void onClear(ActionEvent event) {
        passwordField.clear();
        userNameField.clear();
    }

    @FXML
    void onSignIn(ActionEvent event) {
        System.out.println("Sign In clicked");
    }

}
