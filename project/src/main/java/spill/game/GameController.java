package spill.game;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import spill.rendering.BirdseyeRenderer;
import spill.storage.Storage;
import spill.storage.StorageInteface;

public class GameController extends AnimationTimer{

    @FXML
    private Canvas canvas;

    private Scene launcherScene;

    private BirdseyeRenderer br;
    private StorageInteface storage;

    @FXML
    void initialize(){
        br = new BirdseyeRenderer(canvas.getGraphicsContext2D(), this);
    }

    //Exits game to launcher
    private void openLauncherScene(ActionEvent actionEvent){
		Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(launcherScene);
    }

    //Used by GameApp
    public void setLauncherScene(Scene launcherScene){
        this.launcherScene = launcherScene;
    }

    public void startGame(int id){
        storage = new Storage(id);
        this.start();
        br.render();
    }

    @Override
    public void handle(long now){
        System.out.println("Hello");
    }
    
}
