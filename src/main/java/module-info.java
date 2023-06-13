module com.example.lab3v4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab3v4 to javafx.fxml;
    exports com.example.lab3v4;
}