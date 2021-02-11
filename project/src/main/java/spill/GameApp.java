package spill;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class GameApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        
        stage.setResizable(false);
        stage.setTitle("Sykt kult spill");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        stage.setScene(launcher());

        stage.show();
    }

    public static void main(String[] args) {
        launch(GameApp.class, args);
    }

    private Scene launcher() throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/laucherUI.fxml"));
        return new Scene(parent);
    }

    private Scene game() throws IOException{
        Canvas gameCanvas = new Canvas(800,600);
        Group root = new Group(gameCanvas);
        return new Scene(root, 800, 600); 
    }
}