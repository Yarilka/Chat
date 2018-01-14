package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    protected MyServer myServer;
    protected Socket socket;
    protected DataInputStream in;
    protected DataOutputStream out;
    protected String name;
    public SendClass send;
    public ReadClass read;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            this.read = new ReadClass(this);
            this.send = new SendClass(myServer.getClients());
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }


    public void changeNick(String newNick) {
        send.broadcastMsg(name + " сменил ник на " + newNick);
        name = newNick;
        sendMsg("/yournickis " + newNick);
        send.broadcastClientList();
    }

    public void sendMsg(String msg) {
        System.out.println(name);
        System.out.println("Method send - "+msg);
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
