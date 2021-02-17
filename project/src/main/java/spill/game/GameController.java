package spill.game;

import java.util.ArrayList;
import java.util.Collection;

import javafx.animation.AnimationTimer;
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
import spill.storage.Storage;
import spill.storage.StorageInteface;

public class GameController extends AnimationTimer{
    // TODO: Sette dette i storage, så hver save kan ha egne controls
    public static final String FORWARDKEY = "W";
    public static final String BACKKEY = "S";
    public static final String TURNLEFTKEY = "A";
    public static final String TURNRIGHTKEY = "D";
    public static final String MENUKEY = "Esc";

    @FXML
    private Canvas canvas;

    @FXML
    private Pane menuPane;

    private Scene launcherScene;

    private Renderer br;
    private StorageInteface storage;
    private boolean paused;

    //Game data
    private Level currentLevel;
    private Player player;
    private Collection<String> pressedKeys;

    @FXML
    private void onKeyPressed(KeyEvent evt){
        if (evt.getCode().getName() == MENUKEY) {
            togglePaused();
        }
        if (!pressedKeys.contains(evt.getCode().getName())) {
            pressedKeys.add(evt.getCode().getName());
        }
    }

    @FXML
    private void onKeyReleased(KeyEvent evt){
        pressedKeys.remove(evt.getCode().getName());
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
        togglePaused();
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
        pressedKeys = new ArrayList<>();
        br = new BirdseyeRenderer(canvas.getGraphicsContext2D(), this);
        currentLevel = new Level();
        player = new Player(currentLevel);
        paused = false;
        this.start();
    }

    @Override
    public void handle(long now){
        //Runs every frame
        if (pressedKeys.contains(FORWARDKEY)) {
            player.forward();
        }
        if (pressedKeys.contains(BACKKEY)) {
            player.backward();
        }
        if (pressedKeys.contains(TURNLEFTKEY)) {
            player.turnLeft();
        }
        if (pressedKeys.contains(TURNRIGHTKEY)) {
            player.turnRight();
        }

        br.render();
    }

    private void togglePaused(){
        paused = !paused;
        menuPane.setVisible(paused);

        if (paused) {
            this.stop();
        } else {
            this.start();
        }
        
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
