package Controller;

import Model.MainClient;
import Model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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


}
