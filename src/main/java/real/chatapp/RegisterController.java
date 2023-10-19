package real.chatapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import real.Client;
import real.serverapp.Datenbank;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.*;

public class RegisterController implements Initializable {
    @FXML private TextField tfEmail;
    @FXML private TextField tfUsername;
    @FXML private TextField tfPassword;
    @FXML private TextField tfCPassword;
    ChatApp application;
    private int code;

    private String regexemail = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    private String regexpw = "^(?=.*\\d).{6,500}$";

    public static RegisterController getRegisterController() {
        return registerController;
    }

    private static RegisterController registerController;

    public int getCode() {
        return code;
    }

    @FXML private void register() throws IOException {
        Datenbank db = Datenbank.getDb();
        ClientSideConnection con = new ClientSideConnection(this, tfUsername.getText(), tfEmail.getText());
    }

    public void rest(boolean works)
    {
        if(works == true && patternMatches(tfEmail.getText(), regexemail) && patternMatches(tfPassword.getText(), regexpw))
        {
            if(tfPassword.getText().equals(tfCPassword.getText()))
            {
                Random random = new Random();
                code = random.nextInt(900000) + 100000;

                HttpResponse<String> response = Unirest.post("https://rapidprod-sendgrid-v1.p.rapidapi.com/mail/send")
                        .header("content-type", "application/json")
                        .header("X-RapidAPI-Key", "mykey")
                        .header("X-RapidAPI-Host", "rapidprod-sendgrid-v1.p.rapidapi.com")
                        .body("{\r\n \"personalizations\": [\r\n {\r\n \"to\": [\r\n {\r\n \"email\": \"" + tfEmail.getText() + "\"\r\n }\r\n ],\r\n \"subject\": \"Verification code\"\r\n }\r\n ],\r\n \"from\": {\r\n \"email\": \"chatfia21@gmail.com\"\r\n },\r\n \"content\": [\r\n {\r\n \"type\": \"text/plain\",\r\n \"value\": \"Ihr Verifizierungscode lautet: "+ code + "\"\r\n }\r\n ]\r\n}")
                        .asString();
                Platform.runLater(() -> application.changeToVerification());
            }
            else
            {
                System.out.println("Passwort stimmt nicht überein");
            }
        }
        else {
            System.out.println("Username oder email ändern");
        }
    }

    public static boolean patternMatches(String string, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(string)
                .matches();
    }

    public void addUser() throws SQLException, IOException {
        Client client = new Client();
        String pw = client.verschluesseln(tfPassword.getText());
        ClientSideConnection con = new ClientSideConnection(this, tfEmail.getText(), tfUsername.getText(), pw);
        application.changeToLogin();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerController = this;
    }
}
