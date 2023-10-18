package real.chatapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import real.Message;
import real.Messagetype;

import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static java.io.File.createTempFile;


public class MainViewController implements Initializable {
    @FXML private Button startButton;
    @FXML private Button sendButton;
    @FXML private TextField portInput;
    @FXML private TextField usernameInput;
    @FXML private TextField messageInput;
    @FXML private ListView<String> chatView;
    @FXML private VBox testChatlog;
    @FXML protected TextField ipInput;

    private File fBuffer;
    private ClientSideConnection con;
    private ObservableList<Message> messages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messages = FXCollections.observableArrayList();
        // chatView.setItems(messages);
    }

    public void updateView(Message message) {
        messages.add(message);
        if (message.getType() == Messagetype.TEXT)
        {
            testChatlog.getChildren().add(new Text(message.getUsername()+": "+message.getText()));
        }
        else if(message.getType() == Messagetype.IMAGE)
        {
            testChatlog.getChildren().add(new Text(message.getUsername()+": "+message.getText()));
            ImageView view;
            try
            {

                BufferedImage image = ImageIO.read(new ByteArrayInputStream(message.getFileData()));
                Image viewImage = SwingFXUtils.toFXImage(image, null);
                view = new ImageView(viewImage);

                view.setPreserveRatio(true);
                view.setFitHeight(200);
                view.setFitWidth(200);
                testChatlog.getChildren().add(view);


            }
            catch (IOException e)
            {
                System.out.println("Bild konnte nicht gelesen werden");
            }
        }
        else if(message.getType() == Messagetype.VIDEO)
        {
            testChatlog.getChildren().add(new Text(message.getUsername()+": "+message.getText()));
            byte[] bytePuffer = message.getFileData();
        }

    }

    @FXML
    private void fileChooser() throws IOException {
        FileChooser chooser = new FileChooser();
        fBuffer = chooser.showOpenDialog(null);

        System.out.println(fBuffer.getClass().toString());
    }
    @FXML
    private void sendMessage() throws IOException {
        con.sendMessage(messageInput.getText(),fBuffer);
        messageInput.clear();
        fBuffer = null;
    }
}
