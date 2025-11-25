module org.example.integradora2ced {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.example.integradora2ced to javafx.fxml;
    exports org.example.integradora2ced;
    exports org.example.integradora2ced.controller;
    opens org.example.integradora2ced.controller to javafx.fxml;
}