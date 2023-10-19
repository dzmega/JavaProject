package real.chatapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class VerificationController implements Initializable{
    @FXML private TextField tfCode;
    @FXML private Button btnCheck;
    @FXML private Label lblText;
    @FXML private TextField tfEmail;
    @FXML private Button btnSend;
    private int code;
    private static VerificationController verificationController;

    public static VerificationController getVerificationController() {
        return verificationController;
    }


    @FXML private void send()
    {
        Random random = new Random();
        code = random.nextInt(900000) + 100000;

        HttpResponse<String> response = Unirest.post("https://rapidprod-sendgrid-v1.p.rapidapi.com/mail/send")
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "mykey")
                .header("X-RapidAPI-Host", "rapidprod-sendgrid-v1.p.rapidapi.com")
                .body("{\r\n \"personalizations\": [\r\n {\r\n \"to\": [\r\n {\r\n \"email\": \"" + tfEmail.getText() + "\"\r\n }\r\n ],\r\n \"subject\": \"Verification code\"\r\n }\r\n ],\r\n \"from\": {\r\n \"email\": \"chatfia21@gmail.com\"\r\n },\r\n \"content\": [\r\n {\r\n \"type\": \"text/plain\",\r\n \"value\": \"Ihr Verifizierungscode lautet: "+ code + "\"\r\n }\r\n ]\r\n}")
                .asString();
        tfEmail.clear();
    }
    @FXML public void check() throws SQLException, IOException {
        RegisterController registerController = RegisterController.getRegisterController();
        if(Integer.parseInt(tfCode.getText()) == registerController.getCode())
        {
            System.out.println("Code passt");
            registerController.addUser();
        }
        else if(Integer.parseInt(tfCode.getText()) == code)
        {
            System.out.println("Code passt");
            registerController.addUser();
        }
        else {
            System.out.println("Code erneut eingeben");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        verificationController = this;
    }
}
