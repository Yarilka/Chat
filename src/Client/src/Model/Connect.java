package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connect {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public String start() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket("localhost", 8189);
                if (socket.isConnected()) {
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Сервер отключен\n";
            }
        }
        return "Подключение успешно\n";
    }

    public void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
