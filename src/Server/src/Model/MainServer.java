package Model;

import Controller.ControllerServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainServer extends Application {
    public static ControllerServer contr;
    public static MyServer serv;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/server.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Server chat JavaFX");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("server.css");
        primaryStage.show();
        contr = (ControllerServer) loader.getController();
        serv = new MyServer();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
