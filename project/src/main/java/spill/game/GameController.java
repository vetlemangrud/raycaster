package spill.game;

import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import spill.rendering.Renderer;
import spill.storage.Storage;
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

    @FXML
    private Slider volumeSlider;

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
        game.stopMusic();
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
        game.changeVolume(volumeSlider.valueProperty().intValue());
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

    private void loadSettings(int storageId){
        //Loads settings to settings pane or sets defaults
        
        Storage storage = new Storage(storageId);
        if(!validateVolume(storage.readSave("VOLUME"))){
            try {
                storage.writeSave("VOLUME", "50");
            } catch (Exception e) {
                System.out.println("Could not save game: " + e.getMessage());
            }
        }
        volumeSlider.adjustValue(Integer.parseInt(storage.readSave("VOLUME")));
    }

    private boolean validateVolume(String volume){
        Pattern volumePattern = Pattern.compile("^(\\d\\d?|100)$");
        return volumePattern.matcher(volume).find();
    }

    public void initializeGame(int storageId){
        menuPane.setVisible(false);
        settingsPane.setVisible(false);

        //Load settings
        loadSettings(storageId);

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
