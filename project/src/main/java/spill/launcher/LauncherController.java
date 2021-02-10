package spill.launcher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import javafx.event.Event;

public class LauncherController {

    @FXML
    private ScrollPane saveSelectScrollPane;

    @FXML
    private VBox saveSelectVBox;

    @FXML
    void initialize() throws IOException{
        for (int i = 0; i < 10; i++){
            saveSelectVBox.getChildren().add(createStartSaveButton("Save" + i, i));
        }
        
    }


    @FXML
    private void createNewSaveButtonAction(Event evt) {
        //Start new save
        System.out.println(((Button)evt.getSource()).getId());
    }

    private void startSaveButtonAction(Event evt) {
        System.out.println(((Button)evt.getSource()).getId());
    }

    private Button createStartSaveButton(String text, int saveId){
        //Vanskelig å lage dype kloner av JavaFX-elementer, så det er bedre å lage et nytt for hver knapp enn å definere i SceneBuilder og prøve å klone
        Button startSaveButton = new Button(text);
        startSaveButton.setMaxWidth(Double.MAX_VALUE);
        startSaveButton.setPrefHeight(50);
        startSaveButton.setStyle("-fx-background-color:#000000");
        startSaveButton.setTextFill(Color.WHITE);
        startSaveButton.setId(String.valueOf(saveId));
        startSaveButton.setOnAction(evt -> startSaveButtonAction(evt));
        return startSaveButton;
    }

}
