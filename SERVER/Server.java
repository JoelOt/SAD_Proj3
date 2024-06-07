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
                MySocket socket = myserversocket.accept();
                serverThread serverThread = new Server().new serverThread(socket, clientList);
                serverThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class serverThread extends Thread {
        private MySocket mysocket;
        private ConcurrentHashMap<String, MySocket> clientList;

        public serverThread(MySocket socket, ConcurrentHashMap<String, MySocket> clientList) {
            this.mysocket = socket;
            this.clientList = clientList;
        }

        @Override
        public void run() {
            try {
                BufferedReader input = mysocket.getInput();
                PrintWriter output = mysocket.getOutput();
                String clientName = input.readLine();
                System.out.println("Nickname received: " + clientName);
                clientList.put(clientName, mysocket);

                // Broadcast new user joined
                broadcast("Server: " + clientName + " has joined the chat.");

                // Infinite loop for server
                while (true) {
                    String outputString = input.readLine();
                    if (outputString == null) {
                        break;
                    }
                    System.out.println("Received from " + clientName + ": " + outputString);
                    broadcast(outputString);
                }

                // Remove user and broadcast they left
                clientList.remove(clientName);
                broadcast("Server: " + clientName + " has left the chat.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void broadcast(String outputString) {
            clientList.forEach((name, value) -> {
                value.getOutput().println(outputString);
            });
        }
    }
}
