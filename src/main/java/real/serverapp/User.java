package real.serverapp;

import real.Message;
import real.Messagetype;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class User implements Runnable {
    private final Socket socket;
    private final ServerSideConnection con;
    private final ObjectOutputStream outgoing;
    private final ObjectInputStream ingoing;

    public User(ServerSideConnection con, Socket socket) throws IOException {
        this.con = con;
        this.socket = socket;
        outgoing = new ObjectOutputStream(socket.getOutputStream());
        ingoing = new ObjectInputStream(socket.getInputStream());
        new Thread(this).start();
    }

    public void sendMessage(Object content) throws IOException {
        outgoing.writeObject(content);
        outgoing.flush();
    }

    public void sendLogin(Object login) throws IOException {
        outgoing.writeObject(login);
        outgoing.flush();
    }

    public void sendCheckRegister(Object checkRegister) throws IOException {
        outgoing.writeObject(checkRegister);
        outgoing.flush();
    }

    @Override
    public void run() {
        while(socket.isConnected()) {
            Message msgBuffer;

            try {
                if((msgBuffer = (Message) ingoing.readObject()) != null) {

                    if(msgBuffer.getType() == Messagetype.LOGIN)
                    {
                        try {
                            con.loginUser(this, msgBuffer.getUsername(), msgBuffer.getPw());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if (msgBuffer.getType() == Messagetype.REGISTER)
                    {
                        try {
                            con.registerUser(this, msgBuffer.getEmail(), msgBuffer.getUsername(), msgBuffer.getPw());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(msgBuffer.getType() == Messagetype.CHECKREGISTER)
                    {
                        con.checkRegister(this, msgBuffer.getEmail() ,msgBuffer.getUsername());
                    }
                    else {
                        con.broadcastMessage(msgBuffer);
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
