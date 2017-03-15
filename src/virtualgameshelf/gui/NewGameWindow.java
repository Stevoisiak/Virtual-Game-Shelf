package virtualgameshelf.gui;

import java.util.ArrayList;
import java.util.Optional;

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
    private boolean displayingAdd;

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

        HBox systemRow = new HBox();
        //systemRow.setPadding( new Insets(16) );
        systemRow.setSpacing(16);
        systemRow.setAlignment( Pos.CENTER_LEFT );

        Scene popupScene = new Scene(root, 800, 300);
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
        ComboBox<String> systemChooser = new ComboBox<>();
        systemChooser.getItems().addAll(systemList);

        systemChooser.setPromptText("Choose a System");
        // checks which item is selected in the ComboBox
        systemChooser.valueProperty().addListener(new ChangeListener<String>() {
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
        ComboBox<String> completionChooser = new ComboBox<>();
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
                boolean nameFound = false;

                // Retrieve and set game info
                // Retrieving game name
                do {
                    if (nameField.getText().trim().equals("")) {
                        String value = createTextAlert("Game Name:");
                        newGame.setName(value);
                    }
                    else {
                        newGame.setName(nameField.getText());
                    }

                    if (!newGame.getName().trim().equals("")) {
                        nameFound = true;
                    }
                } while (nameFound == false);

             // Retrieving game system
                if  (systemChooser.getValue() == "Add New System") {
                    if (systemField.getText().trim().equals("")) {
                        newGame.setSystem("Other");
                    }
                    else {
                        newGame.setSystem(systemField.getText());
                    }
                }
                else {
                    if (systemChooser.getValue() == null) {
                        newGame.setSystem("Other");
                    }
                    else {
                        newGame.setSystem(systemChooser.getValue());
                    }
                }

             // Retrieving game completion
                if  (completionChooser.getValue() == null) {
                    newGame.setCompletion("Unfinished");
                }
                else {
                    newGame.setCompletion(completionChooser.getValue());
                }

                if (hoursField.getText() != null || hoursField.getText().trim().isEmpty())
                    newGame.setHours(0);
                else
                    newGame.setHours(Integer.parseInt(hoursField.getText()));

                if (starGroup.getSelectedToggle() != null)
                    newGame.setRating((int) starGroup.getSelectedToggle().getUserData());
                else
                    newGame.setRating(0);

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
            }
        });
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

    public String createTextAlert(String prompt) {

        String value = "default";

     // dialog for getting string input
        TextInputDialog textDialog = new TextInputDialog();
        // used to set the title of the window
        textDialog.setTitle("Missing Entry");
        // I don't want header text
        textDialog.setHeaderText(null);
        // what is written in the window
        textDialog.setContentText(prompt);

        // Traditional way to get the response value.
        Optional<String> textResult = textDialog.showAndWait();
        if ( textResult.isPresent() ) {
            System.out.println("You entered: " + textResult.get());
            //saves value for later
            value = textResult.get();
        }
        else {
            System.out.println("You closed the dialog.");
        }

        return value;

    }
}
