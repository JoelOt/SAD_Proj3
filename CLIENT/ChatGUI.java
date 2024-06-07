package CLIENT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatGUI {
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private MySocket socket;
    private String clientName;

    public ChatGUI(MySocket socket) {
        this.socket = socket;
        initialize();
        new ClientThread(socket).start();
    }

    private void initialize() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        frame.add(messageScrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        frame.add(inputField, BorderLayout.SOUTH);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0));
        frame.add(userScrollPane, BorderLayout.EAST);

        frame.setVisible(true);

        clientName = JOptionPane.showInputDialog(frame, "Enter your nickname:");
        if (clientName != null && !clientName.trim().isEmpty()) {
            socket.output.println(clientName);
        } else {
            System.exit(0);
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.trim().isEmpty()) {
            socket.output.println(clientName + ": " + message);
            inputField.setText("");
        }
    }

    public void addMessage(String message) {
        messageArea.append(message + "\n");
    }

    public void addUser(String user) {
        userListModel.addElement(user);
    }

    public void removeUser(String user) {
        userListModel.removeElement(user);
    }

    class ClientThread extends Thread {
        private MySocket socket;

        public ClientThread(MySocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String response = socket.readLine();
                    if (response != null) {
                        addMessage(response);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
