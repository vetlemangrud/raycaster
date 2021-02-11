package spill.gamelogic;

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

    @FXML
    void initialize(){
        br = new BirdseyeRenderer(canvas.getGraphicsContext2D());
        br.render();
    }

    private void openLauncherScene(ActionEvent actionEvent){
		Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(launcherScene);
    }

    public void setLauncherScene(Scene launcherScene){
        this.launcherScene = launcherScene;
    }
    
}
