package CLIENT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MySocket extends Socket {
    BufferedReader input;
    PrintWriter output;
    Socket socket;

    public MySocket(String host, int port) throws Exception {
        socket = new Socket(host, port);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    public MySocket(Socket s) throws Exception {
        try {
            this.socket = s;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Error creating socket: " + e.getStackTrace());
        }
    }

    public String readLine() {
        try {
            return this.input.readLine();
        } catch (Exception e) {
            System.out.println("Error reading line: " + e.getStackTrace());
        }
        return null;
    }

    public BufferedReader getInput() {
        return this.input;
    }

    public PrintWriter getOutput() {
        return this.output;
    }

    public Socket getSocket() {
        return this.socket;
    }
}
