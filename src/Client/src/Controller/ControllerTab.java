package Controller;

import Model.MainClient;
import Model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ControllerTab {
    public VBox privateTab;
    public HBox bottomPrivate;
    public TextArea privateTA;
    public TextField privateTF;

    public void privateMsg(String nickFrom, String msg) {
        for (Tab t : MainClient.user.contr.tabPane.getTabs()) {
            if (t.getId().equals(nickFrom)) {
                System.out.println("Id: "+t.getId()+" Msg: "+msg);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(privateTA.getId()+"\n"+t.getContent().getClip().toString());
                        privateTA.appendText(msg);
                    }
                });
                return;
            }
        }
        newTab(nickFrom, msg);
    }

    private void newTab(String nickFrom, String msg) {
        Tab tab = new Tab(nickFrom);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/tab.fxml"));
            if (loader.getLocation() != null) {

                tab.setContent(loader.load());

            } else System.out.println("Файл отсутствует - " + loader.getLocation());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tab.setClosable(true);
        tab.setId(nickFrom);
//        tab.getContent().lookup("#privateTA").setId("privateTA" + nickFrom);
//        tab.getContent().lookup("#privateTF").setId("privateTF" + nickFrom);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainClient.user.contr.tabPane.getTabs().add(tab);
            }
        });

        privateMsg(nickFrom, msg);
    }

    public void onSendMsg(ActionEvent actionEvent) {
        MainClient.user.sendMsg(privateTF.getText());
        privateTF.clear();
        privateTF.requestFocus();
    }
}
