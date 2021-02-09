package spill.launcher;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;

import java.io.IOException;

import javafx.event.Event;

public class LauncherController {

    @FXML
    private VBox saveSelectVBox;

    @FXML
    void initialize() throws IOException{
        //Button saveButton = FXMLLoader.load(getClass().getResource("/fxml/savedGameButton.fxml"));
        //saveSelectVBox.getChildren().add(saveButton);
    }


    @FXML
    private void createNewSaveButtonAction(Event evt) {
        System.out.println(((Control)evt.getSource()).getId());
    }

    @FXML
    private void playSavedGameButtonAction(Event evt) {
        System.out.println(((Control)evt.getSource()).getId());
    }

}
