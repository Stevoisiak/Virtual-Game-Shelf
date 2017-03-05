package virtualgameshelf.gui;

import java.util.ArrayList;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.stage.*;
import virtualgameshelf.backend.domain.Game;

public class NewGameWindow extends Stage {
    private TextField systemField;

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

        Scene popupScene = new Scene(root, 800, 200);
        this.setScene(popupScene);

        // add stylesheet
        popupScene.getStylesheets().add("resources/stylesheet.css");

        // custom code below --------------------------------------------

        // selectable list of game systems
        ArrayList<String> systemList = new ArrayList<>();
        systemList.add("Add New System");
        systemList.add("PSP");

        Label nameLabel = new Label("Game Name:");
        TextField nameField = new TextField("");

        Label systemLabel = new Label("Game System:");
        ComboBox<String> systemChooser = new ComboBox<String>();
        systemChooser.getItems().addAll(systemList);

        systemChooser.setPromptText("Choose a System");
        // checks which item is selected in the ComboBox
        systemChooser.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                if (newValue == "Add New System") {
                    systemField = new TextField("");
                    systemField.setPromptText("Enter a System");

                    root.add(systemField, 2, 1);
                } else {
                    root.getChildren().remove(systemField);
                }
            }
        });

        Label completionLabel = new Label("Game Completion:");
        ComboBox<String> completionChooser = new ComboBox<String>();
        completionChooser.getItems().addAll("Unfinished", "Beaten", "Completed", "Null", "Mastered", "Unplayed");

        completionChooser.setPromptText("Choose a Level of Completion");
        // checks which item is selected in the ComboBox
        completionChooser.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String oldValue, String newValue) {
                // TODO
            }
        });

        Label hoursLabel = new Label("Hours Played:");
        TextField hoursField = new TextField("");
        // http://stackoverflow.com/a/30796829
        // force the field to be numeric only
        hoursField.textProperty().addListener(new ChangeListener<String>() {
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
        ToggleGroup starGroup = new ToggleGroup();
        RadioButton starButtons[] = new RadioButton[5];
        for (int i = 0; i < starButtons.length; i++) {
            starButtons[i] = new RadioButton(i+1 + " Star");
            starButtons[i].setUserData(i+1);
            starButtons[i].setToggleGroup(starGroup);
            starRow.getChildren().add(starButtons[i]);
        }

        // Create entry with entered game data
        Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Game newGame = new Game();
                boolean customConsole = false;

                // Retrieve and set game info
                newGame.setName(nameField.getText());

                if (systemChooser.getValue() == "Add New System") {
                    customConsole = true;
                    newGame.setSystem(systemField.getText());
                } else {
                    newGame.setSystem(systemChooser.getValue());
                }

                newGame.setFinish(completionChooser.getValue());

                if (hoursField.getText() != null || hoursField.getText().trim().isEmpty())
                    newGame.setHours(0);
                else
                    newGame.setHours(Integer.parseInt(hoursField.getText()));

                newGame.setFinish(completionChooser.getValue());

                if (starGroup.getSelectedToggle() != null)
                    newGame.setRating((int) starGroup.getSelectedToggle().getUserData());
                else
                    newGame.setRating(0);

                // Print game info
                System.out.println("Game Name: " + newGame.getName());
                System.out.println("Game System: " + newGame.getSystem());
                System.out.println("Game Completion: " + newGame.getFinish());
                System.out.println("Hours Played: " + newGame.getHours());
                System.out.println("Rating: " + newGame.getRating() + " Star");
            }
        });
        root.setHalignment(addButton, HPos.CENTER);

        root.add(nameLabel, 0, 0);
        root.add(nameField, 1, 0);
        root.add(systemLabel, 0, 1);
        root.add(systemChooser, 1, 1);
        root.add(completionLabel, 0, 2);
        root.add(completionChooser, 1, 2);
        root.add(hoursLabel, 0, 3);
        root.add(hoursField, 1, 3);
        root.add(ratingLabel, 0, 4);
        root.add(starRow, 1, 4);
        root.add(addButton, 1, 5);
    }
}
