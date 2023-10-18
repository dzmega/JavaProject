package real.chatapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import real.Client;

public class LoginController {
    ChatApp application;
    private ClientSideConnection con;
    @FXML private TextField tfUsername;
    @FXML private TextField tfPassword;
    @FXML private Button btnLogin;

    @FXML private void login() throws Exception
    {
        Client client = new Client();
        String pw = client.verschluesseln(tfPassword.getText());
        con = new ClientSideConnection(this, tfUsername.getText(), pw);
    }

    public void checkEntry(boolean entry)
    {
        if(entry == true)
        {
            // TODO: 15.06.2023 FENSTER WECHSELN
            System.out.println("Komm rein");
        }
        else {
            System.out.println("Fehlgeschlagen");
        }
    }

    @FXML private void register()
    {
        application.changeToRegister();
    }

}
