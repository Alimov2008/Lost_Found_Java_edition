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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(root, 2560, 1600);

        primaryStage.setTitle("Lost & Found System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}