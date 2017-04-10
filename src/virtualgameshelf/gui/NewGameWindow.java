package virtualgameshelf.gui;

import org.controlsfx.control.textfield.TextFields;

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
    private TextField hoursField;
    private ToggleGroup starGroup;
    private ComboBox<String> systemChooser;
    private ComboBox<String> completionChooser;
    private Button addButton;
    private Game newGame;

    /** Create a new window for adding games. */
    public NewGameWindow() {
        this(new Game());
    }

    /**
     * Create a new window for adding games. Fields are pre-filled with info
     * from game. Useful when editing games.
     *
     * @param game
     *            Used to fill fields with game info.
     */
    public NewGameWindow(Game game) {
        // tell stage it is meant to pop-up (Modal)
        this.initModality(Modality.APPLICATION_MODAL);

        this.setTitle("Add New Game");
        // add application icon
        // newStage.getIcons().add( new Image("icons/") );

        GridPane root = new GridPane();
        root.setPadding(new Insets(16));
        root.setHgap(10);
        root.setVgap(10);
        root.setAlignment(Pos.CENTER);
        // root.setGridLinesVisible(true);

        Scene popupScene = new Scene(root, 800, 300);
        this.setScene(popupScene);

        // add stylesheet
        popupScene.getStylesheets().add("stylesheet.css");

        // custom code below --------------------------------------------

        Label nameLabel = new Label("Game Name:");
        nameField = new TextField();

        // selectable list of game systems
        Label systemLabel = new Label("Game System:");
        HBox systemRow = new HBox();
        systemRow.setSpacing(16);
        systemRow.setAlignment( Pos.CENTER_LEFT );
        systemChooser = new ComboBox<>();
        for (String value : VirtualGameShelf.systemNameMap.values()) {
            systemChooser.getItems().add(value); // populate system list
        }
        systemChooser.setPromptText("Choose a System");
        systemChooser.setEditable(true);
        TextFields.bindAutoCompletion(systemChooser.getEditor(), systemChooser.getItems());
        systemRow.getChildren().add(systemChooser);

        Label completionLabel = new Label("Game Completion:");
        completionChooser = new ComboBox<>();
        completionChooser.getItems().addAll("Unfinished", "Beaten", "Completed", "Null", "Mastered", "Unplayed");

        completionChooser.setPromptText("Choose a Level of Completion");

        Label hoursLabel = new Label("Hours Played:");
        hoursField = new TextField();
        // http://stackoverflow.com/a/30796829
        // force the field to be numeric only
        hoursField.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                hoursField.setText(newValue.replaceAll("[^\\d]", ""));
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

        if (game.getName() != null) {
            // Fill every field with game info if editing old game
            nameField.setText(game.getName());
            systemChooser.setValue(game.getSystem());
            completionChooser.setValue(game.getCompletion());
            hoursField.setText("" + game.getHours());
            for (RadioButton rb : starButtons) {
                if ((int)rb.getUserData() == game.getRating()) {
                    rb.setSelected(true);
                }
            }
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

    /**
     * Works like {@link #showAndWait()}, but returns Game object.
     *
     * @return game object created by the user.
     */
    public Game showAndAddGame() {
        this.showAndWait();
        // Waits until window is closed before returning newGame
        return newGame;
    }

    /**
     * Create and display an alert to the user.
     *
     * @param message
     *            string to display to user.
     */
    private void displayAlert(String message) {
        Alert infoAlert = new Alert(AlertType.WARNING);
        infoAlert.setTitle(null);
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(message);
        infoAlert.showAndWait();
    }

    /**
     * Retrieves info from text fields to create new Game object.
     * Once it has been created, the current window is closed.
     */
    private void onClickAddGame() {
        newGame = new Game();

        // Retrieve game name
        if (nameField.getText() != null && !nameField.getText().trim().isEmpty()) {
            newGame.setName(nameField.getText());
        } else {
            // Name is missing; stop parsing game data
            displayAlert("Please specify the game name.");
            return;
        }

        // Retrieve game system
        if (systemChooser.getValue() != null && !systemChooser.getValue().trim().isEmpty() ) {
            String system = systemChooser.getValue().trim();
            system = VirtualGameShelf.getSystemShortName(system);
            newGame.setSystem(system);
        } else {
            newGame.setSystem("Other");
        }

        // Retrieve game completion
        if  (completionChooser.getValue() != null) {
            newGame.setCompletion(completionChooser.getValue());
        } else {
            newGame.setCompletion("Unfinished");
        }

        // Retrieve game hours
        if (hoursField.getText() != null && !hoursField.getText().trim().isEmpty()) {
            newGame.setHours(Integer.parseInt(hoursField.getText()));
        } else {
            newGame.setHours(0);
        }

        // Retrieve game rating
        if (starGroup.getSelectedToggle() != null) {
            newGame.setRating((int) starGroup.getSelectedToggle().getUserData());
        } else {
            newGame.setRating(0);
        }

        //newGame.print();

        // Close window after successful game addition (http://stackoverflow.com/a/25038465)
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}
