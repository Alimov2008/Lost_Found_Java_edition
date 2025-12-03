module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens app to javafx.fxml;
    opens app.controllers to javafx.fxml;
    opens app.model to javafx.fxml;

    exports app;
    exports app.controllers;
}