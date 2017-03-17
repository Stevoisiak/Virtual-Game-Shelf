package virtualgameshelf.gui;

import java.util.ArrayList;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.stage.*;
import virtualgameshelf.backend.domain.Game;

public class NewGameWindow extends Stage {
    private TextField nameField;
    private TextField systemField;
    private TextField hoursField;
    private ToggleGroup starGroup;
    private ComboBox<String> systemChooser;
    private ComboBox<String> completionChooser;
    private Button addButton;
    private Game newGame;

    public NewGameWindow() {
        // tell stage it is meant to pop-up (Modal)
        this.initModality(Modality.APPLICATION_MODAL);

        this.setTitle("Add New Game");
        // add application icon
        // newStage.getIcons().add( new Image("resources/icons/") );

        GridPane root = new GridPane();
        root.setPadding(new Insets(16));
        root.setHgap(10);
        root.setVgap(10);
        root.setAlignment(Pos.CENTER);
        // root.setGridLinesVisible(true);

        Scene popupScene = new Scene(root, 800, 300);
        this.setScene(popupScene);

        // add stylesheet
        popupScene.getStylesheets().add("resources/stylesheet.css");

        // custom code below --------------------------------------------

        // selectable list of game systems
        HBox systemRow = new HBox();
        systemRow.setSpacing(16);
        systemRow.setAlignment( Pos.CENTER_LEFT );

        ArrayList<String> systemList = new ArrayList<>();
        systemList.add("Add New System");
        systemList.add("PSP");

        Label nameLabel = new Label("Game Name:");
        nameField = new TextField("");

        Label systemLabel = new Label("Game System:");
        systemChooser = new ComboBox<>();
        systemChooser.getItems().addAll(systemList);

        systemChooser.setPromptText("Choose a System");
        // checks which item is selected in the ComboBox
        systemChooser.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                if (newValue == "Add New System") {
                    systemField = new TextField("");
                    systemField.setPromptText("Enter a System");

                    systemRow.getChildren().add(systemField);
                } else {
                    systemRow.getChildren().remove(systemField);
                }
            }
        });
        systemRow.getChildren().add(systemChooser);

        Label completionLabel = new Label("Game Completion:");
        completionChooser = new ComboBox<>();
        completionChooser.getItems().addAll("Unfinished", "Beaten", "Completed", "Null", "Mastered", "Unplayed");

        completionChooser.setPromptText("Choose a Level of Completion");

        Label hoursLabel = new Label("Hours Played:");
        hoursField = new TextField("");
        // http://stackoverflow.com/a/30796829
        // force the field to be numeric only
        hoursField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    hoursField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Label ratingLabel = new Label("Rating:");
        HBox starRow = new HBox();
        starRow.setSpacing(5);
        starRow.setAlignment(Pos.CENTER);

        // radio for 1-5 star rating
        starGroup = new ToggleGroup();
        RadioButton starButtons[] = new RadioButton[5];
        for (int i = 0; i < starButtons.length; i++) {
            starButtons[i] = new RadioButton(i+1 + " Star");
            starButtons[i].setUserData(i+1);
            starButtons[i].setToggleGroup(starGroup);
            starRow.getChildren().add(starButtons[i]);
        }

        // Create entry with entered game data
        addButton = new Button("Add");
        addButton.setOnAction(e -> onClickAddGame());
        root.setHalignment(addButton, HPos.CENTER);

        root.add(nameLabel, 0, 0);
        root.add(nameField, 1, 0);
        root.add(systemLabel, 0, 1);
        root.add(systemRow, 1, 1);
        root.add(completionLabel, 0, 2);
        root.add(completionChooser, 1, 2);
        root.add(hoursLabel, 0, 3);
        root.add(hoursField, 1, 3);
        root.add(ratingLabel, 0, 4);
        root.add(starRow, 1, 4);
        root.add(addButton, 1, 5);
    }

    public Game showAndAddGame() {
        this.showAndWait();
        // Waits until window is closed before returning newGame
        return newGame;
    }

    /** Takes in a String to create and display an alert window */
    private void displayAlert(String message) {
        Alert infoAlert = new Alert(AlertType.WARNING);
        infoAlert.setTitle(null);
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(message);
        infoAlert.showAndWait();
    }

    private void onClickAddGame() {
        newGame = new Game();

        // Retrieve game name
        if (nameField.getText() != null && !nameField.getText().trim().equals("")) {
            newGame.setName(nameField.getText());
        } else {
            // Name is missing; stop parsing game data
            displayAlert("Please specify the game name.");
            return;
        }

        // Retrieve game system
        if (systemChooser.getValue() == "Add New System") {
            if (systemField.getText().trim().equals("")) {
                newGame.setSystem("Other");
            } else {
                newGame.setSystem(systemField.getText());
            }
        } else {
            if (systemChooser.getValue() == null) {
                newGame.setSystem("Other");
            } else {
                newGame.setSystem(systemChooser.getValue());
            }
        }

        // Retrieve game completion
        if  (completionChooser.getValue() == null) {
            newGame.setCompletion("Unfinished");
        } else {
            newGame.setCompletion(completionChooser.getValue());
        }

        // Retrieve game hours
        if (hoursField.getText() == null || hoursField.getText().trim().isEmpty()) {
            newGame.setHours(0);
        } else {
            newGame.setHours(Integer.parseInt(hoursField.getText()));
        }

        // Retrieve game rating
        if (starGroup.getSelectedToggle() != null) {
            newGame.setRating((int) starGroup.getSelectedToggle().getUserData());
        } else {
            newGame.setRating(0);
        }

        // Print game info
        System.out.println("Game Name: " + newGame.getName());
        System.out.println("Game System: " + newGame.getSystem());
        System.out.println("Game Completion: " + newGame.getCompletion());
        System.out.println("Hours Played: " + newGame.getHours());
        if (newGame.getRating() == 1) {
            System.out.println("Rating: " + newGame.getRating() + " star");
        } else {
            System.out.println("Rating: " + newGame.getRating() + " stars");
        }

        // Close window after successful game addition (http://stackoverflow.com/a/25038465)
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}
