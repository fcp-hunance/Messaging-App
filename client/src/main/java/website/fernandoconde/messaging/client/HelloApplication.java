//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package website.fernandoconde.messaging.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import website.fernandoconde.messaging.client.network.ApiClient;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {
    public HelloApplication() {
    }

    public void start(Stage stage) throws IOException {
       FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/website/fernandoconde/messaging/views/login-Screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);


        stage.show();
    }

    public static void main(String[] args) {
       // launch();

        ApiClient client = new ApiClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Messenger Login");
        System.out.print("Benutzername: ");
        String username = scanner.nextLine();
        System.out.print("Passwort: ");
        String password = scanner.nextLine();

        if (client.login(username, password)) {
            System.out.println("Login erfolgreich! JWT: " + client.getJwtToken());
            // Hier könntest du den Messenger-Client starten
        } else {
            System.out.println("Login fehlgeschlagen.");
        }
    }
}
