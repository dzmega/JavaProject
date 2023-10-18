package real.chatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApp extends Application {
    public static Stage st;
    public static Scene scLogin;
    public static Scene scRegister;
    public static Scene scVerification;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApp.class.getResource("login.fxml"));
        scLogin = new Scene(fxmlLoader.load());
        FXMLLoader fxmlLoader2 = new FXMLLoader(ChatApp.class.getResource("register.fxml"));
        scRegister = new Scene(fxmlLoader2.load());
        FXMLLoader fxmlLoader3 = new FXMLLoader(ChatApp.class.getResource("verification.fxml"));
        scVerification = new Scene(fxmlLoader3.load());
        st = stage;
        stage.setTitle("Hello!");
        stage.setScene(scLogin);
        stage.sizeToScene();
        stage.show();
    }

    public static void changeToRegister()
    {
        st.setScene(scRegister);
    }

    public static void changeToLogin()
    {
        st.setScene(scLogin);
    }

    public static void changeToVerification()
    {
        st.setScene(scVerification);
    }

    public static void main(String[] args) {
        launch();
    }
}