package real;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class Client implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static final String ALGORITHM = "RSA";

    private int id;
    private String username;
    private String password;
    private String email;


    public Client()
    {

    }

    public Client(int id, String email, String username, String password) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.password = password;

    }

    public Client(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;

    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String password) {
        this.email = password;
    }

    public int getId() {
        return id;
    }

    public String verschluesseln(String pw) {
        String encryptedString = null;
        try {
            String originalString = pw;
            String secretKey = "DiesIstEinGeheimerSchluessel";

            // Schlüssel generieren
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "Blowfish");

            // Cipher-Objekt initialisieren und Verschlüsselung durchführen
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(originalString.getBytes(StandardCharsets.UTF_8));

            // Base64-Kodierung des verschlüsselten Byte-Arrays
            encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            System.err.println("Fehler beim Verschlüsseln: " + e.getMessage());
        }
        return encryptedString;
    }
}
