package app;

import dao.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Database.initialize();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/main.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/app/styles.css").toExternalForm());

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        stage.setTitle("Lost & Found - JavaFX");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setWidth(screenBounds.getWidth()*0.9);
        stage.setHeight(screenBounds.getHeight()*0.9);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
