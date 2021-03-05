package spill.game;

import java.util.ArrayList;
import java.util.Collection;

import javafx.animation.AnimationTimer;
import spill.rendering.Renderer;
import spill.storage.LevelLoader;
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
    private LevelLoader levelLoader;
    private boolean paused;

    private double deltaTime; //Time between frames (So we can have speed per time instead of speed per frame. The frames are not necessarly equally spaced)
    private long lastFrameTime;

    //Game data
    private Level currentLevel;
    private Player player;
    private Collection<String> pressedKeys;

    public Game(GameController gameController){
        this.gameController = gameController;
        levelLoader = new LevelLoader();
        pressedKeys = new ArrayList<>();
        currentLevel = levelLoader.load(1);
        player = new Player(currentLevel);
        paused = false;
        lastFrameTime = 0;
        this.start();
    }

    @Override
    public void handle(long now){
        deltaTime = (double) (now - lastFrameTime);
        lastFrameTime = now;
        System.out.println("FPS: " + (int) (1/(deltaTime/1000000000)));
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
            gameController.closePauseScreen();
            this.start();
        }
    }

    public void setStorageId(int id) {
        storage = new Storage(id);
    }

    public void saveState() {
        try {
            storage.writeSave("PLAYER_X", String.valueOf(player.getPos().getX()));
            storage.writeSave("PLAYER_Y", String.valueOf(player.getPos().getY()));
            storage.writeSave("PLAYER_ANGLE", String.valueOf(player.getDirection().getAngle()));
        } catch (Exception e) {
            System.out.println("Could not save game: " + e.getMessage());
        }
    }

    public void loadState() {
        try {
            double x = Double.parseDouble(storage.readSave("PLAYER_X"));
            double y = Double.parseDouble(storage.readSave("PLAYER_Y"));
            double angle = Double.parseDouble(storage.readSave("PLAYER_ANGLE"));
            player = new Player(currentLevel, x, y, angle);
        } catch (Exception e) {
            System.out.println("Previous player position not found or is corrupted");
        }
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
