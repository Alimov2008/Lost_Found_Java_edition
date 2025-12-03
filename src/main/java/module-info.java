module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    opens app to javafx.fxml;
    opens app.controllers to javafx.fxml;
    opens app.dao to javafx.fxml;
    opens app.model to javafx.fxml;

    exports app;
    exports app.controllers;
    exports app.dao;
    exports app.model;
}