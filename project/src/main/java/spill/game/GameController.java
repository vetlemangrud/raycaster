package spill.game;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import spill.rendering.Renderer;
import spill.rendering.BirdseyeRenderer;
import spill.storage.Storage;
import spill.storage.StorageInteface;

public class GameController extends AnimationTimer{

    @FXML
    private Canvas canvas;

    private Scene launcherScene;

    private Renderer br;
    private StorageInteface storage;

    //Game data
    private Level currentLevel;
    private Player player;

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
        currentLevel = new Level();
        player = new Player(currentLevel.getWidth()/2, currentLevel.getHeight()/2);
        this.start();
    }

    @Override
    public void handle(long now){
        br.render();
    }

    public double getCanvasWidth(){
        return canvas.getWidth();
    }

    public double getCanvasHeight(){
        return canvas.getHeight();
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }
    
    public Player getPlayer(){
        return player;
    }
}
