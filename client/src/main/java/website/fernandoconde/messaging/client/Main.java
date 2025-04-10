//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package website.fernandoconde.messaging.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import website.fernandoconde.messaging.client.network.ApiClient;
//import website.fernandoconde.messaging.client.service.MessageSend;
//import website.fernandoconde.messaging.client.service.MessageReceiver;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    public Main() throws IOException {
    }

    static String response ;



    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/website/fernandoconde/messaging/views/login-Screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);



        stage.show();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        launch();
//                Konsolen Test!
//                Scanner scanner = new Scanner(System.in);
//
//                String apiUrl = "http://localhost:8080";
//
//                ApiClient apiClient = new ApiClient(apiUrl);
//
//                try {
//                    System.out.print("Benutzername: ");
//                    String username = scanner.nextLine();
//
//                    System.out.print("Passwort: ");
//                    String password = scanner.nextLine();
//
//                    String token = apiClient.login(username, password);
//                    System.out.println("Login erfolgreich!\n");
//
//                    System.out.print("Empfänger: ");
//                    String empfaenger = scanner.nextLine();
//
//                    System.out.print("Nachricht: ");
//                    String nachricht = scanner.nextLine();
//
//                    MessageSend messageSend = new MessageSend(apiUrl, token);
//                    messageSend.sendMessage(empfaenger, nachricht);
//
//                    MessageReceiver messageReceiver = new MessageReceiver(apiUrl, token);
//                    messageReceiver.pollMessages();
//
//                } catch (Exception e) {
//                    System.err.println("Fehler: " + e.getMessage());
//                    e.printStackTrace();
//                } finally {
//                    scanner.close();
//                }
//            }
    }
}

