package app;

import dao.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Database.initialize();

        stage.setTitle("Lost and Found System");
        stage.setScene(new Scene(new javafx.scene.control.Label("Dushman"), 400, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
