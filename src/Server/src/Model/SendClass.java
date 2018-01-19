package Model;

import java.util.Vector;

public class SendClass {
    private Vector<ClientHandler> clients;

    public SendClass(Vector<ClientHandler> clients) {
        this.clients = clients;
    }

    public synchronized void sendMsgToClient(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nickTo)) {
                o.sendMsg("Вам шепчет " + from.getName() + ": " + msg);
                from.sendMsg("Вы шепчете " + nickTo + ": " + msg);
                return;
            }
        }
        from.sendMsg("Участника с ником " + nickTo + " нет в чат-комнате");
    }

    public synchronized void sendPrivateMsg(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nickTo)) {
                o.sendMsg("/private "+from.name+" "+msg);
                from.sendMsg("/private "+o.name+" "+msg);
                return;
            }
        }
        from.sendMsg("Участника с ником " + nickTo + " нет в чат-комнате");
    }


    public synchronized void broadcastMsg(String msg) {
        System.out.println("Method broadcast - " + msg);
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
        MainServer.contr.countClient(clients.size());
        broadcastClientList();
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
        MainServer.contr.countClient(clients.size());
        broadcastClientList();
    }

    public synchronized void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clients ");
        for (ClientHandler o : clients) {
            sb.append(o.getName() + " ");
        }
        String msg = sb.toString();
        broadcastMsg(msg);
        System.out.println(msg);
    }

//    public void sendMsg(String msg) {
//        System.out.println("Method send - "+msg);
//        try {
//            out.writeUTF(msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
