package spill.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class LauncherApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/laucherUI.fxml"));

        stage.setScene(new Scene(parent));
        stage.setResizable(false);
        stage.setTitle("Sykt kult spill");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        stage.show();
    }

    public static void main(String[] args) {
        launch(LauncherApp.class, args);
    }
}