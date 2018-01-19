package Controller;

import Model.MainClient;
import Model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;
import static javafx.scene.layout.Priority.ALWAYS;

public class ControllerClient implements Initializable {
    public HBox upperPanel;
    public TextField loginField;
    public PasswordField passField;
    public Button btnAuth;
    public Tab root;
    public TextArea textArea;
    public TabPane tabPane;
    public HBox bottomPanel;
    public TextField textField;
    public TabPane clientsTP;
    public Tab clientsTab;
    public ListView<String> clientsList;
    public AnchorPane mainPane;

    public ObservableList<String> obsClients;

    public VBox privateVB;
    public TextArea privateTA;
    public HBox privateBP;
    public TextField privateTF;

    private boolean authorized;

    public void setAuthorized(boolean authorized) {

        this.authorized = authorized;
        if (!this.authorized) {
            mainPane.setRightAnchor(tabPane, 0.0);
            mainPane.setTopAnchor(tabPane, 25.0);
            mainPane.setTopAnchor(clientsTP, 25.0);
            clientsTP.setVisible(false);
            upperPanel.setManaged(true);
            bottomPanel.setManaged(false);
            upperPanel.setVisible(true);
            bottomPanel.setVisible(false);
            loginField.setFocusTraversable(true);
            Platform.runLater(() -> MainClient.mainStage.setTitle("JavaFX Client"));
        } else {
            System.out.println("AUTH OK");
            mainPane.setRightAnchor(tabPane, 200.0);
            mainPane.setTopAnchor(tabPane, 0.0);
            mainPane.setTopAnchor(clientsTP, 0.0);
            upperPanel.setManaged(false);
            upperPanel.setVisible(false);
            bottomPanel.setManaged(true);
            tabPane.setFocusTraversable(false);
            textArea.setFocusTraversable(false);
            textField.setFocusTraversable(true);
            upperPanel.setVisible(false);
            bottomPanel.setVisible(true);
            clientsTP.setVisible(true);
            clientsTab.setDisable(false);
            clientsTab.setContent(clientsList);
            clientsList.setVisible(true);
            root.setClosable(false);
            textArea.clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthorized(false);
        obsClients = FXCollections.observableArrayList();
        clientsList.setItems(obsClients);
    }

    public void onAuthClick(ActionEvent actionEvent) {
        MainClient.user.auth(loginField.getText(), passField.getText());
        loginField.clear();
        passField.clear();
    }

    public void onSendMsg(ActionEvent actionEvent) {
        MainClient.user.sendMsg(textField.getText());
        textField.clear();
        textField.requestFocus();
    }

    public void listClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            textField.setText("/w " + clientsList.getSelectionModel().getSelectedItem() + " ");
            textField.requestFocus();
            textField.selectEnd();
        }
    }

    public void clearClientList() {
        clientsList.getItems().clear();
    }

    public void listClients(String client) {
        String[] s = client.split("\\s");
        for (int i = 1; i < s.length; i++) {
            System.out.println(s[i]);
            clientsList.getItems().add(s[i]);
        }
    }

    public void writeMsg(String msg) {
        textArea.appendText(msg);
    }

    public void privateMsg(String nickFrom, String msg) {
        for (Tab t : tabPane.getTabs()) {
            if (t.getId().equals(nickFrom)) {
                System.out.println("Id: " + t.getId() + " Msg: " + msg);
                VBox vb = (VBox) t.getContent();
                TextArea ta = (TextArea) vb.getChildren().get(0);
                ta.appendText(msg+"\n");

                clientsTP.setFocusTraversable(false);
                clientsList.setFocusTraversable(false);

                HBox hb = (HBox) vb.getChildren().get(1);
                hb.getChildren().get(0).setFocusTraversable(true);
                return;
            }
        }
        newTab(nickFrom, msg);
    }

    private void newTab(String nickFrom, String msg) {
        Tab tab = new Tab(nickFrom);
        tab.setId(nickFrom);

        VBox vb = new VBox();
        vb.setId("VB"+nickFrom);

        TextArea ta = new TextArea();
        ta.setId("TA"+nickFrom);
        ta.setEditable(false);
        ta.setWrapText(true);

        HBox hb = new HBox();
        hb.setId("HB"+nickFrom);

        TextField tf = new TextField();
        tf.setId("TF"+nickFrom);
        tf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onSendPrivateMsg(tf);
            }
        });

        Button bt = new Button("Send to "+nickFrom);
        bt.setId("BT"+nickFrom);
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    onSendPrivateMsg(tf);
            }
        });

        hb.getChildren().addAll(tf,bt);
        hb.setHgrow(tf,ALWAYS);
        vb.getChildren().addAll(ta,hb);
        vb.setVgrow(ta,ALWAYS);
        tab.setContent(vb);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tabPane.getTabs().add(tab);
            }
        });
        System.out.println("Табы - " + tabPane.getTabs().size());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tabPane.getSelectionModel().select(tab);
        ta.setFocusTraversable(false);
        bt.setFocusTraversable(false);
        tf.setFocusTraversable(true);

        privateMsg(nickFrom, msg);
    }

    public void onSendPrivateMsg(TextField tf) {
        MainClient.user.sendMsg("/private " + tabPane.getSelectionModel().getSelectedItem().getText()+" "+tf.getText());
        tf.clear();
        tf.requestFocus();
    }
}
