package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClient extends Application {
    public static Stage mainStage;
    public static User user;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/client.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Client chat JavaFX");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("client.css");
        primaryStage.show();
        mainStage = primaryStage;
        user = new User(loader.getController());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
