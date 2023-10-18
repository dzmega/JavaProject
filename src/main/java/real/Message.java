package real;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import real.Messagetype;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class Message implements Serializable{

    private static final long serialVersionUID = -3147768168830205029L;
    private int num;
    private String email;
    private String username;
    private boolean bool;
    private String pw;
    private String text;
    private transient File file;
    private Messagetype type;


    public byte[] getFileData() {
        return fileData;
    }
    private byte[] fileData;


    public Message(String text, File file, String username) throws IOException {
        if (file != null) {
            String type = Files.probeContentType(file.toPath());

            if (type.startsWith("image")) {
                this.type = Messagetype.IMAGE;
                this.file = file;
                fileData = Files.readAllBytes(file.toPath());
            } else {
                if (type.startsWith("video")) {
                    this.type = Messagetype.VIDEO;
                    this.file = file;
                    Media media = new Media(file.toURI().toString());
                    fileData = media.getSource().getBytes();
                }
            }
        }
        else
        {
            this.type = Messagetype.TEXT;
            this.file = null;
        }
        this.username = username;
        this.text = text;
    }
    public Message(String username, String pw)
    {
        this.type = Messagetype.LOGIN;
        this.username = username;
        this.pw = pw;
    }

    public Message(String email, String username, int num)
    {
        this.type = Messagetype.CHECKREGISTER;
        this.email = email;
        this.username = username;
    }

    public Message(String email, String username, String pw)
    {
        this.type = Messagetype.REGISTER;
        this.username = username;
        this.pw = pw;
        this.email = email;
    }

    public Message(boolean bool)
    {
        this.type = Messagetype.BOOLEAN;
        this.bool = bool;
    }

    public Message(boolean bool, int num)
    {
        this.type = Messagetype.BACKREGISTER;
        this.bool = bool;
        this.num = num;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public File getFile() {
        return file;
    }

    public Messagetype getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getPw() {
        return pw;
    }

    public boolean getBool() {
        return bool;
    }
}
