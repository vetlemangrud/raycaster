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
    private static final String FORWARDKEY = "W";
    private static final String BACKKEY = "S";
    private static final String TURNLEFTKEY = "A";
    private static final String TURNRIGHTKEY = "D";
    private static final String MENUKEY = "Esc";
    private static final int STARTLEVEL = 1;

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
        currentLevel = levelLoader.load(STARTLEVEL);
        player = new Player(currentLevel);
        paused = false;
        startMusic();
        this.start();
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

        Collection<Exit> exitsInRange = player.getExitsInRange();
        if (exitsInRange.size() > 0) {
            Exit exit = exitsInRange.iterator().next();
            currentLevel = levelLoader.load(exit.getTargetLevel());
            startMusic();
            player = new Player(currentLevel, exit.getTargetPos().getX(), exit.getTargetPos().getY(), player.getDirection().getAngle());
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
            storage.writeSave("LEVEL", String.valueOf(currentLevel.getId()));
            storage.writeSave("PLAYER_X", String.valueOf(player.getPos().getX()));
            storage.writeSave("PLAYER_Y", String.valueOf(player.getPos().getY()));
            storage.writeSave("PLAYER_ANGLE", String.valueOf(player.getDirection().getAngle()));
        } catch (Exception e) {
            System.out.println("Could not save game: " + e.getMessage());
        }
    }

    public void loadState() {
        try {
            currentLevel = levelLoader.load(Integer.parseInt(storage.readSave("LEVEL")));
            startMusic();
            double x;
            double y;
            double angle;
            try {
                x = Double.parseDouble(storage.readSave("PLAYER_X"));
                y = Double.parseDouble(storage.readSave("PLAYER_Y"));
                angle = Double.parseDouble(storage.readSave("PLAYER_ANGLE"));
            } catch (Exception e) {
                System.out.println("Saved player state not found or is corrupted");
                x = currentLevel.getStartPosition().getX();
                y = currentLevel.getStartPosition().getY();
                angle = 0;
            }
            
            player = new Player(currentLevel, x, y, angle);
            changeVolume(Integer.parseInt(storage.readSave("VOLUME")));
        } catch (Exception e) {
            System.out.println("Saved state not found or is corrupted");
        }
    }

    private void startMusic(){
        try{
            double volume = 50;
            if (mediaPlayer != null) {
                volume = mediaPlayer.getVolume();
                mediaPlayer.stop();
            }
            Media bgMusic = new Media(getClass().getResource("/sound/" + currentLevel.getMusicName() + ".mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(bgMusic);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception err) {
            System.err.println(err.getMessage());
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
