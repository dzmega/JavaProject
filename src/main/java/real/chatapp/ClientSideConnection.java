package real.chatapp;

import javafx.application.Platform;
import real.Message;
import real.Client;
import real.Messagetype;

import java.io.*;
import java.net.Socket;

public class ClientSideConnection implements Runnable {
    private final Socket socket;
    private final String username;
    private String password;
    private String email;
    private MainViewController con;
    private final ObjectOutputStream oOutput;
    private final ObjectInputStream oInput;
    private LoginController loginController;
    private RegisterController registerController;

    public ClientSideConnection(LoginController loginController, String username, String password) throws IOException {
        this.loginController = loginController;
        this.username = username;
        this.password = password;
        socket = new Socket("localhost", 5555);
        oOutput = new ObjectOutputStream(socket.getOutputStream());
        oInput = new ObjectInputStream(socket.getInputStream());
        new Thread(this).start();
        sendLogin(username, password);
    }

    public ClientSideConnection(RegisterController registerController, String username, String email) throws IOException {
        this.registerController = registerController;
        this.username = username;
        this.email = email;
        socket = new Socket("localhost",5555);
        oOutput = new ObjectOutputStream(socket.getOutputStream());
        oInput = new ObjectInputStream(socket.getInputStream());
        new Thread(this).start();
        sendCheckRegister(username, email, 1);
    }

    public ClientSideConnection(RegisterController registerController, String email, String username, String password) throws IOException {
        this.registerController = registerController;
        this.username = username;
        this.email = email;
        this.password = password;
        socket = new Socket("localhost",5555);
        oOutput = new ObjectOutputStream(socket.getOutputStream());
        oInput = new ObjectInputStream(socket.getInputStream());
        new Thread(this).start();
        sendRegister(email, username, password);
    }

    public void sendMessage(String text, File file) throws IOException {
        Message message = new Message(text,file,username);
        oOutput.writeObject(message);
        oOutput.flush();
    }

    public void sendLogin(String username, String pw) throws IOException {
        Message message = new Message(username, pw);
        oOutput.writeObject(message);
        oOutput.flush();
    }

    public void sendCheckRegister(String email, String username, int help) throws IOException {
        Message message = new Message(email, username, 1);
        oOutput.writeObject(message);
        oOutput.flush();
    }

    public void sendRegister(String email, String username, String password) throws IOException {
        Message message = new Message(email, username, password);
        oOutput.writeObject(message);
        oOutput.flush();
    }

    @Override
    public void run() {
        while(socket.isConnected()) {
            Message msgBuffer;

            try {

                if((msgBuffer = (Message) oInput.readObject()) != null ) {
                    if(msgBuffer.getType() == Messagetype.BOOLEAN)
                    {
                        loginController.checkEntry(msgBuffer.getBool());
                    }
                    else if(msgBuffer.getType() == Messagetype.BACKREGISTER)
                    {
                        registerController.rest(msgBuffer.getBool());
                    }
                    else
                    {
                        Platform.runLater(() -> con.updateView(msgBuffer));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
