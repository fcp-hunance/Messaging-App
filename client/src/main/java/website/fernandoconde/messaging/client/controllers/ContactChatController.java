package website.fernandoconde.messaging.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import website.fernandoconde.messaging.client.network.ApiClient;

import java.io.IOException;
import java.util.List;

public class ContactChatController {
    @FXML private ListView<String> contactListView;
    @FXML private Label chatWithLabel;
    @FXML private TextArea chatArea;
    @FXML private TextField messageField;

    private String currentToken;
    private String currentContact;
    private final ApiClient apiClient = new ApiClient("http://localhost:8080");

    @FXML
    public void initialize() {
        contactListView.setOnMouseClicked(this::onContactSelected);
    }

    public void setToken(String token) {
        this.currentToken = token;
        loadContacts();
    }

    private void loadContacts() {
        try {
            List<String> contacts = apiClient.getContacts(currentToken);
            contactListView.getItems().setAll(contacts);
        } catch (IOException e) {
            showAlert("Fehler beim Laden der Kontakte", e.getMessage());
        }
    }

    private void onContactSelected(MouseEvent event) {
        currentContact = contactListView.getSelectionModel().getSelectedItem();
        if (currentContact != null) {
            chatWithLabel.setText("Chat mit: " + currentContact);
            loadChatHistory();
        }
    }

    private void loadChatHistory() {
        try {
            List<String> messages = apiClient.getMessages(currentToken, currentContact);
            chatArea.clear();
            messages.forEach(msg -> chatArea.appendText(msg + "\n"));
        } catch (IOException e) {
            showAlert("Fehler beim Laden des Chats", e.getMessage());
        }
    }

    @FXML
    private void onSendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && currentContact != null) {
            try {
                apiClient.sendMessage(currentToken, currentContact, message);
                chatArea.appendText("Du: " + message + "\n");
                messageField.clear();
            } catch (IOException e) {
                showAlert("Nachricht konnte nicht gesendet werden", e.getMessage());
            }
        }
    }

    @FXML
    private void onAddContact() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Kontakt hinzufügen");
        dialog.setHeaderText("Username des neuen Kontakts:");

        dialog.showAndWait().ifPresent(username -> {
            try {
                if (apiClient.addContact(currentToken, username)) {
                    loadContacts();
                } else {
                    showAlert("Fehler", "Kontakt konnte nicht hinzugefügt werden");
                }
            } catch (IOException e) {
                showAlert("Serverfehler", e.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}