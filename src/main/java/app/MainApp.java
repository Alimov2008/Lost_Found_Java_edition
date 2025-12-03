package app;

import dao.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Database.initialize();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/main.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/app/styles.css").toExternalForm());

        stage.setTitle("Lost & Found - JavaFX");
        stage.setScene(scene);
        stage.setWidth(1600);
        stage.setHeight(900);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
