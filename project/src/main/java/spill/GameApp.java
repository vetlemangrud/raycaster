package spill;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import spill.gamelogic.GameController;
import spill.launcher.LauncherController;
import spill.rendering.BirdseyeRenderer;
import javafx.scene.image.Image;

public class GameApp extends Application {
    BirdseyeRenderer br;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        stage.setTitle("Sykt kult spill");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        //Launcher UI is loaded from FXML
        FXMLLoader launcherUILoader = new FXMLLoader(getClass().getResource("/fxml/laucherUI.fxml"));
        Parent launcherUI = launcherUILoader.load();
        Scene launcherScene = new Scene(launcherUI);

        //Game scene is not made in FXML because it's just a canvas :P
        FXMLLoader gameUILoader = new FXMLLoader(getClass().getResource("/fxml/gameUI.fxml"));
        Parent gameUI = launcherUILoader.load();
        Scene gameScene = new Scene(launcherUI);

        //Injecting scenes into the controller of the other scene to be able to switch scenes back and forth
        //Based on code by AbstractVoid: https://stackoverflow.com/questions/12804664/how-to-swap-screens-in-a-javafx-application-in-the-controller-class
        LauncherController launcherController = (LauncherController) launcherUILoader.getController();
        GameController gameController = (GameController) launcherUILoader.getController();

        launcherController.setGameScene(gameScene);
        gameController.setLauncherScene(launcherScene);

        stage.setScene(launcherScene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(GameApp.class, args);
    }
}