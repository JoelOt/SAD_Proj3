package SERVER;

import CLIENT.MySocket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public static void main(String[] args) {
        ConcurrentHashMap<String, MySocket> clientList = new ConcurrentHashMap<>();
        try (MyServerSocket myserversocket = new MyServerSocket(Integer.parseInt(args[0]))) {

            while (true) {
                MySocket mysocket = myserversocket.accept();
                serverThread serverThread = new Server().new serverThread(mysocket, clientList);
                serverThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class serverThread extends Thread {
        private MySocket mysocket;
        private ConcurrentHashMap<String, MySocket> clientList;

        public serverThread(MySocket mysocket, ConcurrentHashMap<String, MySocket> clientList) {
            this.mysocket = mysocket;
            this.clientList = clientList;
        }

        @Override
        public void run() {
            try {;;
                String clientName = mysocket.rebreMsg();
                System.out.println("Client rebut: " + clientName);
                clientList.put(clientName, mysocket);
                broadcast("Server: Benvingut " + clientName);

                while (true) {
                    String outputString = mysocket.rebreMsg();
                    if (outputString == null) {
                        break;
                    }
                    System.out.println("Rebut de " + clientName + ": " + outputString);
                    broadcast(outputString);
                }

                clientList.remove(clientName);
                broadcast("Server: " + clientName + " ha abandonat la conversa.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void broadcast(String msg) {
            clientList.forEach((nick, socket) -> {
                System.out.println("Enviat a " + nick + ": " + msg);
                socket.getOutput().println(msg);
            });
        }
    }
}
