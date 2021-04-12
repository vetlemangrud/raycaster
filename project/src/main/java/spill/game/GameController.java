package spill.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import spill.rendering.Renderer;
import spill.rendering.BirdseyeRenderer;
import spill.rendering.RaycastRenderer;

public class GameController{
    private static final boolean DEV_MODE = true;
    
    @FXML
    private Canvas canvas;

    @FXML
    private ImageView gameImageView;

    @FXML
    private Pane menuPane;

    @FXML
    private Pane settingsPane;

    private Scene launcherScene;

    private Game game;

    private Renderer rayCastRenderer;
    private Renderer birdseyeRenderer;

    @FXML
    private void onKeyPressed(KeyEvent evt){
        if (evt.getCode().getName().equals("R") && DEV_MODE) {
            toggleRenderer();
        }
        game.registerKeyPress(evt.getCode().getName());
    }

    @FXML
    private void onKeyReleased(KeyEvent evt){
        game.registerKeyRelease(evt.getCode().getName());
    }

    @FXML
    private void onSaveAndQuitButton(ActionEvent evt){
        game.saveState();
        openLauncherScene(evt);
    }

    @FXML
    private void onSettingsButton(){
        menuPane.setVisible(false);
        settingsPane.setVisible(true);
    }

    @FXML
    private void onSettingsBackButton(){
        menuPane.setVisible(true);
        settingsPane.setVisible(false);
    }

    @FXML
    private void onVolumeSliderDrag(){
        System.out.println("dræg");
    }

    @FXML
    private void onResumeButton(){
        menuPane.setVisible(false);
        game.togglePaused();
    }

    

    public void openPauseScreen(){
        menuPane.setVisible(true);
    }

    public void closePauseScreen(){
        menuPane.setVisible(false);
    }

    public void initializeGame(int storageId){
        menuPane.setVisible(false);

        game = new Game(this);
        game.setStorageId(storageId);
        game.loadState();
        
        rayCastRenderer = new RaycastRenderer(canvas.getGraphicsContext2D(),game,gameImageView ,canvas.getWidth(),canvas.getHeight());
        birdseyeRenderer = new BirdseyeRenderer(canvas.getGraphicsContext2D(),game,canvas.getWidth(),canvas.getHeight());
        
        
        game.setRenderer(rayCastRenderer);

        
    }

    private void toggleRenderer(){
        game.getRenderer().clear();
        if (game.getRenderer() == rayCastRenderer) {
            game.setRenderer(birdseyeRenderer);
        } else {
            game.setRenderer(rayCastRenderer);
        }

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
}
