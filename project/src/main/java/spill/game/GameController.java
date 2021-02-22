package spill.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import spill.rendering.Renderer;
import spill.rendering.BirdseyeRenderer;
import spill.rendering.RaycastRenderer;

public class GameController{
    
    @FXML
    private Canvas canvas;

    @FXML
    private Pane menuPane;

    private Scene launcherScene;

    private Game game;

    @FXML
    private void onKeyPressed(KeyEvent evt){
        game.registerKeyPress(evt.getCode().getName());
    }

    @FXML
    private void onKeyReleased(KeyEvent evt){
        game.registerKeyRelease(evt.getCode().getName());
    }

    @FXML
    private void onSaveAndQuitButton(){
        System.out.println("Quit");
    }

    @FXML
    private void onSettingsButton(){
        System.out.println("Settings");
    }

    @FXML
    private void onResumeButton(){
        menuPane.setVisible(false);
        game.togglePaused();
    }

    public void openPauseScreen(){
        menuPane.setVisible(true);
    }

    public void initializeGame(int storageId){
        game = new Game(this);
        game.setStorageId(storageId);
        Renderer renderer = new RaycastRenderer(canvas.getGraphicsContext2D(),game,canvas.getWidth(),canvas.getHeight());
        game.setRenderer(renderer);
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
        game.start();
    }
}
