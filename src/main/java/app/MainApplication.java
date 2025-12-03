package app;

import dao.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Database.initialize();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_new.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1600, 800);

        primaryStage.setTitle("Lost & Found System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}