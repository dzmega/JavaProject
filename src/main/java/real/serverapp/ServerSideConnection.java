package real.serverapp;

import real.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerSideConnection implements Runnable {
    private final ServerSocket socket;
    private final ArrayList<User> users;

    public ServerSideConnection(int port) throws IOException {
        socket = new ServerSocket(port);
        users = new ArrayList<>();
        new Thread(this).start();
    }

    public void broadcastMessage(Object content) {
        users.forEach(user -> {
            try {
                user.sendMessage(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void loginUser(User user, String username, String pw) throws Exception {
        boolean entry;
        Datenbank db = Datenbank.getDb();
        db.update();
        entry = db.loginUser(username, pw);
        Message msg = new Message(entry);
        user.sendLogin(msg);
    }

    public void checkRegister(User user, String email, String username) throws IOException {
        boolean u;
        boolean e;
        Message msg;

        Datenbank db = Datenbank.getDb();
        e = db.checkEmail(email);
        u = db.checkUsername(username);

        if(e == false && u == false)
        {
            msg = new Message(true,1);
        }
        else {
            msg = new Message(false,1);
        }
        user.sendCheckRegister(msg);
    }

    public void registerUser(User user, String email, String username, String password) throws SQLException {
        Datenbank db = Datenbank.getDb();
        db.registerUser(email, username, password);
    }

    @Override
    public void run() {
        while(!socket.isClosed()) {
            try {
                Socket ingoing = socket.accept();
                users.add(new User(this, ingoing));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
