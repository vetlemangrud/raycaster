package spill.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import spill.rendering.BirdseyeRenderer;

public class GameController {

    @FXML
    private Canvas canvas;

    private Scene launcherScene;

    private BirdseyeRenderer br;
    private int saveId;

    @FXML
    void initialize(){
        br = new BirdseyeRenderer(canvas.getGraphicsContext2D());
        br.render();
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
        saveId = id;
    }
    
}
