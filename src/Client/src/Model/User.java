package Model;

import Controller.ControllerClient;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.Calendar;

public class User implements ReadUser, SendUser {

    private DataInputStream in;
    private DataOutputStream out;
    private String myNick;
    public ControllerClient contr;
    private Connect connect;
    private PrintWriter log;

    public User(ControllerClient contr) {
        this.contr = contr;
        this.connect = new Connect();
        try {
            log = new PrintWriter("localHistory.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readStream() {
        new Thread(() -> {
            try {
                while (true) {
                    String str = in.readUTF();
                    System.out.println(str);
                    hist(str);
                    if (str.startsWith("/")) {
                        readSys(str);
                    } else {
                        System.out.println("Get msg from srv - " + str);
                        readChat(str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connect.closeConnection();
            }
        }).start();
    }

    @Override
    public void readSys(String msg) {
        System.out.println("System msg -- " + msg);
        if (msg.startsWith("/authok")) {
            contr.setAuthorized(true);
            myNick = msg.split("\\s")[1];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MainClient.mainStage.setTitle("JavaFX Client: " + myNick);
                    contr.textArea.clear();
                }
            });
        }

        if (msg.equals("/end")) {
            contr.setAuthorized(false);
            connect.closeConnection();
        }

        if (msg.startsWith("/clients ")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    contr.clearClientList();
                    contr.listClients(msg);
                }
            });
        }

        if (msg.startsWith("/yournickis")) {
            myNick = msg.split("\\s")[1];
            Platform.runLater(() -> MainClient.mainStage.setTitle("JavaFX Client: " + myNick));
        }
        if (msg.startsWith("/private")) {
            readPrivate(msg);
        }

    }

    @Override
    public void readPrivate(String msg) {
        String[] s = msg.split("\\s", 3);
        contr.privateMsg(s[1], s[2]);
    }

    @Override
    public void readChat(String msg) {
        contr.writeMsg(msg + "\n");
    }

    @Override
    public void sendMsg(String msg) {
        if (msg.equals("/help")) {
            showAlert("HELP:\n\\w 'nick' - шепнуть\n\\changenick 'new nick' - сменить ник\n\\end - выйти из чата\n\\private 'name' - сообщение в приватной комнате");
            //textArea.appendText("HELP:\n\\w - шепнуть\\changenick - сменить ник\\end - выйти из чата");
            return;
        }
        try {
            System.out.println("Sending - " + msg);
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (msg.equals("/end")) {
            contr.setAuthorized(false);
            connect.closeConnection();
        }
    }

    @Override
    public void auth(String login, String pass) {
        contr.writeMsg(connect.start());
        in = connect.getIn();
        out = connect.getOut();
        System.out.println("Авторизуемся");
        try {
            out.writeUTF("/auth " + login.toLowerCase() + " " + pass);
            out.flush();
        } catch (IOException e) {
            contr.setAuthorized(false);
            e.printStackTrace();
        }
        readStream();
    }

    @Override
    public void helpMsg() {
        showAlert("HELP:\n\\w - шепнуть\n\\changenick - сменить ник\n\\end - выйти из чата");
    }

    public void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Возникли проблемы?");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void hist(String msg) {
        log.println(Calendar.getInstance().getTime().toString() + " " + msg);
        log.flush();
    }
}
