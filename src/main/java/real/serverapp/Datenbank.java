package real.serverapp;


import real.Client;
import real.chatapp.RegisterController;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Datenbank
{
    ArrayList<Client> clients = new ArrayList<>();
    RegisterController regicon;
    Connection con = null;
    Statement stm = null;
    ResultSet rs = null;


    //singleton pattern
    private static Datenbank db;
    public static Datenbank getDb()
    {
        if(db == null)
        {
            db = new Datenbank();
        }
        return db;
    }

    private Datenbank()
    {
        regicon = new RegisterController();

        //connection to database
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try {
                con = DriverManager.getConnection("jdbc:ucanaccess://" + "src/main/resources/UserDatenbank.accdb");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Fehler!");
            e.printStackTrace();
        }
        update();
    }

    public void update()
    {
        clients.clear();
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("select * from user");

            while(rs.next()) {
                clients.add(new Client(rs.getInt(1), rs.getString(2) ,rs.getString(3), rs.getString(4)));
            }
        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
        finally {
            try {
                if(con != null) {
                    rs.close();
                    stm.close();
                }
            }
            catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }

    public boolean loginUser(String username, String password) throws Exception {
        Boolean entry = false;
        for(Client c: clients)
        {
            if(c.getUsername().equals(username.toUpperCase()) && c.getPassword().equals(password))
            {
                System.out.println("You´re in!");
                entry = true;
            }
        }
        if(!entry)
        {
            System.out.println("Please try again!");
        }
        return entry;
    }

    public boolean checkEmail(String email)
    {
        Boolean inUse = false;
        for(Client c: clients)
        {
            if(c.getEmail().equals(email))
            {
                inUse = true;
            }
        }
        if(inUse == true)
        {
            return true;
        }
        return false;
    }

    public boolean checkUsername(String username)
    {
        Boolean inUse = false;
        for(Client c: clients)
        {
            if(c.getUsername().equals(username))
            {
                inUse = true;
            }
        }
        if(inUse == true)
        {
            return true;
        }
        return false;
    }

    public void registerUser(String email, String user, String pw) throws SQLException
    {
        stm = con.createStatement();
        clients.add(new Client(email, user, pw));
        String q = "INSERT INTO User (email,username,password) VALUES (?, ?, ?)";
        PreparedStatement st = con.prepareStatement (q);
        st.setString(1, email );
        st.setString(2, user.toUpperCase());
        st.setString(3, pw);
        st.executeUpdate();
        st.close();
    }
}