package Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import static Model.MainServer.serv;

public class ControllerServer {

    @FXML
    public Label clientCount;
    @FXML
    private TextArea textArea;

    @FXML
    private Button but;

    @FXML
    private Label status;

    public void startServ(ActionEvent actionEvent) {
        serv.start();
        but.setText("Стоп");
        but.setId("stop");
        status.setText("Сервер он-лайн");
        status.setId("worked");
        but.setOnAction(this::stopServ);
    }

    public void stopServ(ActionEvent actionEvent) {
        serv.stop();
        but.setText("Старт");
        but.setId("start");
        status.setText("Сервер офф-лайн");
        status.setId("label");
        but.setOnAction(this::startServ);
    }

    public void printTA(String msg) {
        this.textArea.appendText(msg + "\n");
    }

    public void countClient (int clients){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientCount.setText(Integer.toString(clients));
            }
        });
    }
}
