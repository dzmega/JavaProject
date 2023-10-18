module real.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires unirest.java;
    requires javafx.media;
    requires java.desktop;
    requires javafx.swing;

    opens real.serverapp to javafx.fxml;
    exports real.serverapp;

    opens real.chatapp to javafx.fxml;
    exports real.chatapp;

    exports real;
    opens real to javafx.fxml;
}