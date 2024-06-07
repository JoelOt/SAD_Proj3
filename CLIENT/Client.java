package CLIENT;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (MySocket socket = new MySocket(args[0], Integer.parseInt(args[1]))) {

            ChatGUI chatGUI = new ChatGUI(socket);
            Scanner scanner = new Scanner(System.in);
            String userInput;
            Client.clientThread clientThread = new Client().new clientThread(socket);
            clientThread.start();
            System.out.println("Nickname: ");
            userInput = scanner.nextLine();
            String clientName = userInput;
            socket.output.println(userInput);

            do {
                userInput = scanner.nextLine();
                socket.output.println(clientName + ":   message : " + userInput);
                if (userInput.equals("exit")) {
                    break;
                }
            } while (!userInput.equals("exit"));
        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        }
    }

    public class clientThread extends Thread {
        private MySocket socket;

        public clientThread(MySocket s) {
            this.socket = s;
        }

        @Override
        public void run() {
            while (true) {
                String response = socket.readLine();
                System.out.println(response);
            }
        }
    }
}