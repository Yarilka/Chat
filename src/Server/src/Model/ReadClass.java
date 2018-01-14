package Model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReadClass {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;

    public ReadClass(ClientHandler me) {
        this.in = me.in;
        this.myServer = me.myServer;
        this.socket = me.socket;

        new Thread(() -> {
            try {
                while (true) {
                    socket.setSoTimeout(12000);
                    String str = in.readUTF();
                    if (str.startsWith("/auth")) {
                        System.out.println("Пришла авторизашка - "+str);
                        String[] parts = str.split("\\s");
                        String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                        if (nick != null) {
                            if (!myServer.isNickBusy(nick)) {
                                System.out.println("AUTH OK for "+nick);
                                me.sendMsg("/authok " + nick);
                                socket.setSoTimeout(0);
                                me.setName(nick);
                                me.send.broadcastMsg(me.getName() + " зашел в чат");
                                me.send.subscribe(me);
                                break;
                            } else me.sendMsg("Учетная запись уже используется");
                        } else {
                            me.sendMsg("Неверные логин/пароль");
                        }
                    }
                }
                while (true) {
                    System.out.println("Start read STREAM");
                    String str = in.readUTF();
                    myServer.hist(me.getName() + ": " + str);
                    if (str.startsWith("/")) {
                        if (str.equals("/end")) break;
                        if (str.startsWith("/w ")) {
                            String[] tokens = str.split("\\s");
                            String nick = tokens[1];
                            String msg = str.substring(4 + nick.length());
                            me.send.sendMsgToClient(me, nick, msg);
                        }
                        if (str.startsWith("/changenick ")) {
                            String newNick = str.split("\\s")[1];
                            if (myServer.getAuthService().changeNick(me, newNick)) {
                                me.changeNick(newNick);
                            } else {
                                me.sendMsg("Указанный ник уже кем-то занят");
                            }
                        }
//                        if (str.startsWith("/private")){
//                            String[] tokens = str.split("\\s",3);
//                            me.send.sendPrivateMsg(me, tokens[1], tokens[2]);
//                        }
                    } else {
                        me.send.broadcastMsg(me.getName() + ": " + str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                me.send.unsubscribe(me);
                me.send.broadcastMsg(me.getName() + " вышел из чата");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
