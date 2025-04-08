package website.fernandoconde.messaging.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class LoginScreenController {

    @FXML
    private TextField passwordField;

    @FXML
    private TextField userNameField;

    @FXML
    void OnLogin(ActionEvent event) {

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
