package Model;

import Model.Authority.AuthService;
import Model.Authority.DBAuthService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Vector;

import static Model.MainServer.contr;

public class MyServer {
    private ServerSocket server;
    private Socket socket;
    private Vector<ClientHandler> clients;
    private AuthService authService;
    private PrintWriter out;

    public AuthService getAuthService() {
        return authService;
    }

    private final int PORT = 8189;

    public MyServer() {
        try {
            out = new PrintWriter("log.txt");
            server = new ServerSocket(PORT);
            authService = new DBAuthService();
            authService.start();
            clients = new Vector<>();
        } catch (IOException e) {
            e.printStackTrace();
            contr.printTA("Ошибка при работе сервера");
        }
    }

    public Vector<ClientHandler> getClients() {
        return clients;
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nick)) return true;
        }
        return false;
    }

    public synchronized void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        contr.printTA("Сервер ожидает подключения");
                        socket = server.accept();
                        contr.printTA("Клиент подключился");
                        new ClientHandler(MainServer.serv, socket);
                        System.out.println("Размер - " + clients.size());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    contr.printTA("Ошибка при работе сервера");
                } finally {
                    stop();
                }
            }
        }).start();
    }

    public synchronized void stop() {
        for (ClientHandler o : clients) {
            o.sendMsg("/end");
        }
        try {
            contr.countClient(0);
            server.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        authService.stop();
    }

    public void hist(String msg) {
        out.println(Calendar.getInstance().getTime().toString() + " " + msg);
        out.flush();
    }
}
