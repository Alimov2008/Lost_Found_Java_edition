package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import dao.Database;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Database.initialize();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_new.fxml"));

        Scene scene = new Scene(root, 1600, 800); // Larger window for new layout

        primaryStage.setTitle("Lost & Found System - Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}