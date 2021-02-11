package spill.launcher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.regex.Pattern;

import javafx.event.Event;

public class LauncherController {

    @FXML
    private ScrollPane saveSelectScrollPane;

    @FXML
    private VBox saveSelectVBox;

    @FXML
    private TextField saveNameTextField;

    // @FXML
    // void initialize() throws IOException{
    //     for (int i = 0; i < 10; i++){
    //         saveSelectVBox.getChildren().add(createStartSaveButton("Save" + i, i));
    //     }
        
    // }


    @FXML
    private void createNewSaveButtonAction(Event evt) {
        String saveName = validateSaveName(saveNameTextField.getText());
        saveSelectVBox.getChildren().add(createStartSaveButton(saveName, 1));
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

    private String validateSaveName(String saveName){
        Pattern saveNamePattern = Pattern.compile("^(?=.*[A-Za-z])(\\w| |-)*$");
        if (!saveNamePattern.matcher(saveName).find()) {
            throw new IllegalArgumentException("Save name must contain at loast one letter a-z, space, underscore and dash");
        }
        return saveName;
    }

}
