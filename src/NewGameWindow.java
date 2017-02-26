import java.util.ArrayList;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.stage.*;

public class NewGameWindow extends Stage {
    private TextField systemField;

    public NewGameWindow() {
        //tell stage it is meant to pop-up (Modal)
        this.initModality(Modality.APPLICATION_MODAL);

        this.setTitle("Add New Game");
        // add application icon
        //newStage.getIcons().add( new Image("icons/") );

        GridPane root = new GridPane();
        root.setPadding( new Insets(16) );
        root.setHgap(10);
        root.setVgap(10);
        root.setAlignment( Pos.CENTER );
        //root.setGridLinesVisible(true);

        Scene popupScene = new Scene(root, 800, 200);
        this.setScene(popupScene);

        // add stylesheet
        popupScene.getStylesheets().add("assets/stylesheet.css");

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
        systemChooser.valueProperty().addListener(new ChangeListener<String>()
            {
                public void changed(ObservableValue ov, String oldValue, String newValue)
                {
                    if (newValue == "Add New System") {
                        systemField = new TextField("");
                        systemField.setPromptText("Enter a System");

                        root.add(systemField, 2, 1);
                    }
                    else {
                        root.getChildren().remove(systemField);
                    }
                }
            }
        );

        Label completionLabel = new Label("Game Completion:");
        ComboBox<String> completionChooser = new ComboBox<String>();
        completionChooser.getItems().addAll("Unfinished", "Beaten", "Completed", "Null", "Mastered", "Unplayed");

        completionChooser.setPromptText("Choose a Level of Completion");
        // checks which item is selected in the ComboBox
        completionChooser.valueProperty().addListener(new ChangeListener<String>()
            {
                public void changed(ObservableValue ov, String oldValue, String newValue)
                {
                    //TODO
                }
            }
        );

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
        starRow.setAlignment( Pos.CENTER );

        // radio for star rating
        ToggleGroup group = new ToggleGroup();
        RadioButton s1 = new RadioButton("1 Star");
        s1.setUserData("1 Star");
        s1.setToggleGroup(group);
        s1.requestFocus();

        RadioButton s2 = new RadioButton("2 Star");
        s2.setUserData("2 Star");
        s2.setToggleGroup(group);

        RadioButton s3 = new RadioButton("3 Star");
        s3.setUserData("3 Star");
        s3.setToggleGroup(group);

        RadioButton s4 = new RadioButton("4 Star");
        s4.setUserData("4 Star");
        s4.setToggleGroup(group);

        RadioButton s5 = new RadioButton("5 Star");
        s5.setUserData("5 Star");
        s5.setToggleGroup(group);

        starRow.getChildren().addAll(s1, s2, s3, s4, s5);

        Button addButton = new Button("Add");
        addButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    System.out.println("Game Name: " + nameField.getText());

                    if (systemChooser.getValue() == "Add New System") {
                        System.out.println("Game System: <Choice>" + systemChooser.getValue() + "<Choice> "
                            + "<Adding>" + systemField.getText() + "<Adding>");
                    }
                    else {
                        System.out.println("Game System: " + systemChooser.getValue());
                    }

                    System.out.println("Game Completion: " + completionChooser.getValue());

                    System.out.println("Hours Played: " + hoursField.getText());

                    if (s1.isSelected()) {
                        System.out.println("Rating: 1 Star");
                    }
                    else if (s2.isSelected()) {
                        System.out.println("Rating: 2 Star");
                    }
                    else if (s3.isSelected()) {
                        System.out.println("Rating: 3 Star");
                    }
                    else if (s4.isSelected()) {
                        System.out.println("Rating: 4 Star");
                    }
                    else if (s5.isSelected()) {
                        System.out.println("Rating: 5 Star");
                    }
                }
            }
        );
        root.setHalignment( addButton, HPos.CENTER );

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
