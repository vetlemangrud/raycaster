package spill.game;

import java.util.ArrayList;
import java.util.Collection;

import javafx.animation.AnimationTimer;
import spill.rendering.Renderer;
import spill.storage.Storage;
import spill.storage.StorageInteface;

public class Game extends AnimationTimer{
    // TODO: Sette dette i storage, så hver save kan ha egne controls
    public static final String FORWARDKEY = "W";
    public static final String BACKKEY = "S";
    public static final String TURNLEFTKEY = "A";
    public static final String TURNRIGHTKEY = "D";
    public static final String MENUKEY = "Esc";

    private GameController gameController;
    private Renderer renderer;
    private StorageInteface storage;
    private boolean paused;

    //Game data
    private Level currentLevel;
    private Player player;
    private Collection<String> pressedKeys;

    public Game(GameController gameController){
        this.gameController = gameController;
        pressedKeys = new ArrayList<>();
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

        renderer.render();
    }

    public void togglePaused(){
        paused = !paused;
        if (paused) {
            gameController.openPauseScreen();
            this.stop();
        } else {
            this.start();
        }
    }

    public void setStorageId(int id) {
        storage = new Storage(id);
    }

    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }

    public Player getPlayer(){
        return player;
    }

    public void registerKeyPress(String keyName){
        if (keyName.equals(MENUKEY)) {
            togglePaused();
        }
        if (!pressedKeys.contains(keyName)) {
            pressedKeys.add(keyName);
        }
    }

    public void registerKeyRelease(String keyName){
        pressedKeys.remove(keyName);
    }
}
