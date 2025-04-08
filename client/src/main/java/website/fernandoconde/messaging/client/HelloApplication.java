//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package website.fernandoconde.messaging.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import website.fernandoconde.messaging.client.network.ApiClients;
import website.fernandoconde.messaging.client.service.MessageClient;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {
    public HelloApplication() throws IOException {
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/website/fernandoconde/messaging/views/login-Screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);


        stage.show();
    }

    public static void main(String[] args) throws IOException {
        // launch();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Messenger Login");
        System.out.println("---------------");


        System.out.print("Benutzername: ");
        String username = scanner.nextLine();

        System.out.print("Passwort: ");
        String password = scanner.nextLine();


        ApiClients apiClient = new ApiClients();
        try {
            System.out.println("\nVersuche Login...");
            String response = apiClient.login(username, password);
            System.out.println("Erfolgreich eingeloggt!");
            System.out.println("Serverantwort: " + response);
        } catch (IOException e) {
            System.err.println("\nFehler beim Login: " + e.getMessage());
        } finally {
        }




        String token = apiClient.login(username, password);

        MessageClient messageClient = new MessageClient(token);

        System.out.print("Empfänger: ");
        String recipient = scanner.nextLine();

        System.out.print("Nachricht: ");
        String msg = scanner.nextLine();

        messageClient.sendMessage(recipient, msg);
    }
}