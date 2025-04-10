
package website.fernandoconde.messaging.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {
    public Main() throws IOException {
    }

    static String response ;



    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/website/fernandoconde/messaging/views/login-Screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MarFer");
        stage.setScene(scene);



        stage.show();
    }

    public static void main(String[] args) {launch();}
}

