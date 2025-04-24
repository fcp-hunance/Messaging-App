package website.fernandoconde.messaging.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import website.fernandoconde.messaging.client.network.ApiClient;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ContactChatController {
    @FXML private ListView<String> contactListView;
    @FXML private Label chatWithLabel;
    @FXML private TextArea chatArea;
    @FXML private TextField messageField;

    private String currentToken;
    private String currentContact;
    private final ApiClient apiClient = new ApiClient("http://10.101.186.28:8080");
    private ScheduledExecutorService messagePoller;
    private boolean isPollingActive = false;

    // Konstante für den festen Kontakt
    private static final String FIXED_CONTACT = <your-username-here>;

    @FXML
    public void initialize() {
        contactListView.setOnMouseClicked(this::onContactSelected);
        loadFixedContact(); // Lädt automatisch den festen Kontakt
        contactListView.setOnMouseClicked(this::onContactSelected);
        loadFixedContact();
        messageField.setOnKeyPressed(this::handleEnterPressed);
        Platform.runLater(() -> messageField.requestFocus());
        Platform.runLater(() -> {
            Stage stage = (Stage) chatArea.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                handleClose();
            });
        });

    }

    public void setToken(String token) {
        this.currentToken = token;
        startMessagePolling(); // Startet das Nachrichten-Polling
    }

    private void loadFixedContact() {
        // Setzt nur den festen Kontakt in die Liste
        contactListView.getItems().setAll(FIXED_CONTACT);

        // Automatisch den Kontakt auswählen
        contactListView.getSelectionModel().selectFirst();
        currentContact = FIXED_CONTACT;
        chatWithLabel.setText("Chat mit: " + FIXED_CONTACT);
        //loadChatHistory();
    }

    private void onContactSelected(MouseEvent event) {
        // Da wir nur einen Kontakt haben, ist dies redundant aber für die Struktur enthalten
        currentContact = contactListView.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onSendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && currentContact != null) {
            try {
                apiClient.sendMessage(currentToken, currentContact, message);
                chatArea.appendText("Du: " + message + "\n");
                messageField.clear();
                messageField.requestFocus(); // Cursor zurück ins Textfeld
            } catch (IOException e) {
                showAlert("Nachricht konnte nicht gesendet werden", e.getMessage());
            }
        }
    }

    @FXML
    private void onAddContact() {
        showAlert("Info", "In dieser Demo können keine weiteren Kontakte hinzugefügt werden.");
    }

    private void startMessagePolling() {
        if (isPollingActive || currentToken == null) return;
        System.out.println(currentToken);
        isPollingActive = true;
        messagePoller = Executors.newSingleThreadScheduledExecutor();
        messagePoller.scheduleAtFixedRate(() -> {
            try {
                List<String> newMessages = apiClient.pollUndeliveredMessages(currentToken);
                if (!newMessages.isEmpty()) {
                    Platform.runLater(() -> {
                        newMessages.forEach(msg -> chatArea.appendText(FIXED_CONTACT + ": " + msg + "\n"));
                    });
                }
            } catch (IOException e) {
                Platform.runLater(() ->
                        System.err.println("Polling error: " + e.getMessage()));
            }
        }, 0, 3, TimeUnit.SECONDS); // Polling alle 3 Sekunden
    }

    public void stopMessagePolling() {
        if (messagePoller != null) {
            messagePoller.shutdown();
            isPollingActive = false;
        }
    }

    public void shutdown() {
        stopMessagePolling();
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FXML
    private void handleClose() {
        shutdown();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onSendMessage();
            event.consume(); // Verhindert unerwünschtes Verhalten der Enter-Taste
        }
    }
}
