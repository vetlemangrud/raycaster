package spill.launcher;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import spill.game.GameController;
import spill.storage.Storage;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;

public class LauncherController {

    @FXML
    private ScrollPane saveSelectScrollPane;

    @FXML
    private VBox saveSelectVBox;

    @FXML
    private TextField saveNameTextField;

    @FXML Text errorText;

    private Scene gameScene;
    private GameController gameController;

    @FXML
    private void createNewSaveButtonAction(ActionEvent evt) {
        try {
            String saveName = validateSaveName(saveNameTextField.getText());
            int[] usedIds = Storage.getAllUsedIds();
            int saveId = 1;
            try {
                saveId = Arrays.stream(usedIds).max().getAsInt() + 1;
            } catch (NoSuchElementException e) {
                //If no previous saves exist
                System.out.println("Creating first save");
            }
            
            Storage newSaveStorage = new Storage(saveId);
            try {
                newSaveStorage.writeSave("NAME", saveName);
                addStartSaveButton(saveName, saveId);
            } catch (IOException e) {
                System.err.println("Saving error");
            }
        } catch (IllegalArgumentException e) {
            errorText.setText(e.getMessage());
            errorText.setVisible(true);
        }
    }

    @FXML
    void initialize(){
        //Get all saves and create buttons
        int[] usedIds = Storage.getAllUsedIds();
        for (int id : usedIds){
            Storage idStorage = new Storage(id);
            try {
                addStartSaveButton(validateSaveName(idStorage.readSave("NAME")), id);
            } catch (IllegalArgumentException e) {
                System.err.println("Save with id " + id + " has an invalid name, and is maybe corrupted");
            }
            
        }
    }

    private void addStartSaveButton(String text, int saveId) {
        // Vanskelig å lage dype kloner av JavaFX-elementer, så det er bedre å lage et
        // nytt for hver knapp enn å definere i SceneBuilder og prøve å klone
        Button startSaveButton = new Button(text);
        startSaveButton.setMaxWidth(Double.MAX_VALUE);
        startSaveButton.setPrefHeight(50);
        startSaveButton.setStyle("-fx-background-color:#000000");
        startSaveButton.setTextFill(Color.WHITE);
        startSaveButton.setId(String.valueOf(saveId));
        startSaveButton.setOnAction(evt -> openGameScene(evt));
        saveSelectVBox.getChildren().add(startSaveButton);
    }

    private String validateSaveName(String saveName) {
        Pattern saveNamePattern = Pattern.compile("^(?=.*[A-Za-z])(\\w| |-)*$");
        if (!saveNamePattern.matcher(saveName).find()) {
            throw new IllegalArgumentException(
                    "Save name must contain at loast one letter a-z, space, underscore and dash");
        }
        return saveName;
    }

    //Exits launcher to game
    private void openGameScene(ActionEvent actionEvent){
        int id = Integer.parseInt(((Button) actionEvent.getSource()).getId());
		Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(gameScene);
        gameController.initializeGame(id);
    }

    public void setGameScene(Scene gameScene){
        this.gameScene = gameScene;
    }

    public void setGameController(GameController gameController){
        this.gameController = gameController;
    }

}
