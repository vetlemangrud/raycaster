package spill.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javafx.animation.AnimationTimer;
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import spill.rendering.Renderer;
import spill.storage.LevelLoader;
import spill.storage.SimpleLevelLoader;
import spill.storage.Storage;
import spill.storage.StorageInteface;
import spill.storage.TiledLevelLoader;

public class Game extends AnimationTimer{
    // TODO: Sette dette i storage, så hver save kan ha egne controls
    public static final String FORWARDKEY = "W";
    public static final String BACKKEY = "S";
    public static final String TURNLEFTKEY = "A";
    public static final String TURNRIGHTKEY = "D";
    public static final String MENUKEY = "Esc";

    private GameController gameController;
    private Renderer renderer;
    private MediaPlayer mediaPlayer;
    private StorageInteface storage;
    private LevelLoader levelLoader;
    private boolean paused;

    private long lastFrameTime;

    //Game data
    private Level currentLevel;
    private Player player;
    private Collection<String> pressedKeys;

    public Game(GameController gameController){
        this.gameController = gameController;
        levelLoader = new TiledLevelLoader();
        pressedKeys = new ArrayList<>();
        currentLevel = levelLoader.load(1);
        player = new Player(currentLevel);
        paused = false;
        lastFrameTime = 0;
        this.start();

        try{
            Media bgMusic = new Media(new File("project/src/main/resources/sound/" + currentLevel.getMusicName() + ".mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(bgMusic);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception err) {
            System.err.println(err.getMessage());
        }
        
    }

    @Override
    public void handle(long now){
        double deltaTime = (double) (now - lastFrameTime)/1e9; //Time between frames [s] (So we can have speed per time instead of speed per frame. The frames are not necessarly equally spaced)
        lastFrameTime = now;
        //System.out.println("FPS: " + (int) (1/(deltaTime)));
        //Runs every frame
        if (pressedKeys.contains(FORWARDKEY)) {
            player.forward(deltaTime);
        }
        if (pressedKeys.contains(BACKKEY)) {
            player.backward(deltaTime);
        }
        if (pressedKeys.contains(TURNLEFTKEY)) {
            player.turnLeft(deltaTime);
        }
        if (pressedKeys.contains(TURNRIGHTKEY)) {
            player.turnRight(deltaTime);
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
            changeVolume(Integer.parseInt(storage.readSave("VOLUME")));
        } catch (Exception e) {
            System.out.println("Saved state not found or is corrupted");
        }
    }

    public void stopMusic(){
        if (mediaPlayer != null) {
            mediaPlayer.stop(); //Most important line of the whole project 👂🔫
        }
    }

    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
    }

    public Renderer getRenderer(){
        return renderer;
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

    public void changeVolume(int volume){
        //Map [0, 100] to [0, 1]
        mediaPlayer.setVolume(((double)volume)/ 100);
        try {
            storage.writeSave("VOLUME", String.valueOf(volume));
        } catch (Exception e) {
            System.out.println("Saving error: " + e.getMessage());
        }
    }
}
