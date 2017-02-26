import javafx.animation.*;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;
import java.util.*;

public class VirtualGameShelf extends Application {
    public static void main(String[] args) {
        try {
            // http://stackoverflow.com/questions/31786980/javafx-windows-10-combobox-error
            // Prevents ComboBox from freezing a JavaFX program on a Windows 10 computer
            System.setProperty("glass.accessible.force", "false");
            launch(args);
            System.exit(0);
        }
        catch (Exception error) {
            error.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setTitle("Virtual Game Shelf");

        // add application icon
        //mainStage.getIcons().add( new Image("icons/") );

        // all the items that appear in the window are (extend) Nodes.
        // Nodes are stored in a tree-like data structure called the scene graph.
        BorderPane root = new BorderPane();

        // the Scene contains the content within the Stage/window.
        // optional: specify size. otherwise, automatically calculated.
        Scene mainScene = new Scene(root, 400, 600);

        // attach the scene to the stage
        mainStage.setScene(mainScene);

        // the following line loads a stylesheet file; incorrect syntax will generate a parse warning
        mainScene.getStylesheets().add("assets/stylesheet.css");

        // custom code below --------------------------------------------

        MenuBar menuBar = generateMenuBar();
        root.setTop(menuBar);

        // used to add games to the library
        MenuButton menuButton = generateMenuButton();
        root.setMargin(menuButton, new Insets(16));
        root.setBottom(menuButton);
        root.setAlignment( menuButton, Pos.CENTER_RIGHT );

        mainStage.show();
    }

    public MenuButton generateMenuButton() {
        MenuButton menuButton = new MenuButton(null, new ImageView("icons/add.png"));
        menuButton.setPopupSide(Side.TOP);

        MenuItem manualAdd = new MenuItem("Manually Add New Game");
        manualAdd.setOnAction(e -> manualAdd());

        MenuItem autoAdd = new MenuItem("Add New Game Via Steam");
        autoAdd.setOnAction(e -> System.out.println("This feature is not yet available."));

        menuButton.getItems().addAll(manualAdd, autoAdd);

        // rotates the image 45 degrees when the menu button is "active"
        // then rotates the image 45 degrees when the menu button is not "active"
        menuButton.showingProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        menuButton.setRotate(menuButton.getRotate() + 45);
                    } else {
                        menuButton.setRotate(menuButton.getRotate() + 45);
                    }
                }
            });

        return menuButton;
    }

    public MenuBar generateMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        menuBar.getMenus().add(menuFile);

        MenuItem menuItemNew = new MenuItem("New");
        //menuItemNew.setOnAction(e -> onNew());
        menuItemNew.setAccelerator( new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN) );
        menuFile.getItems().add(menuItemNew);

        MenuItem menuItemOpen = new MenuItem("Open");
        //menuItemOpen.setOnAction(e -> onOpen());
        menuItemOpen.setAccelerator( new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN) );
        menuFile.getItems().add(menuItemOpen);

        MenuItem menuItemSave = new MenuItem("Save");
        //menuItemSave.setOnAction(e -> onSave());
        menuItemSave.setAccelerator( new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN) );
        menuFile.getItems().add(menuItemSave);

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        menuFile.getItems().add(separatorMenuItem);

        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(e -> Platform.exit());
        //menuItemExit.setAccelerator( new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN) );
        menuFile.getItems().add(menuItemExit);

        return menuBar;
    }

    TextField systemField;

    public void manualAdd() {
        Stage newStage = new Stage();

        //tell stage it is meannt to pop-up (Modal)
        newStage.initModality(Modality.APPLICATION_MODAL);

        newStage.setTitle("Add New Game");
        // add application icon
        //newStage.getIcons().add( new Image("icons/") );

        // all the items that appear in the window are (extend) Nodes.
        // Nodes are stored in a tree-like data structure called the scene graph.
        GridPane root2 = new GridPane();
        root2.setPadding( new Insets(16) );
        root2.setHgap(10);
        root2.setVgap(10);
        root2.setAlignment( Pos.CENTER );
        //root2.setGridLinesVisible(true);

        // the Scene contains the content within the Stage/window.
        // optional: specify size. otherwise, automatically calculated.
        Scene popupScene = new Scene(root2, 800, 200);

        // attach the scene to the stage
        newStage.setScene(popupScene);

        // the following line loads a stylesheet file; incorrect syntax will generate a parse warning
        popupScene.getStylesheets().add("assets/stylesheet.css");

        // custom code below --------------------------------------------
        // create an array list
        ArrayList<String> systemList = new ArrayList<>();
        // add elements to the array list
        systemList.add("Add New System");
        systemList.add("PSP");

        Label nameLabel = new Label(" Game Name: ");
        TextField nameField = new TextField("");

        Label systemLabel = new Label(" Game System: ");
        ComboBox<String> systemChooser = new ComboBox<String>();
        systemChooser.getItems().addAll(systemList);

        systemChooser.setPromptText("Choose a System");
        // checks which item is selected in the ComboBox
        systemChooser.valueProperty().addListener(new ChangeListener<String>()
            {
                public void changed( ObservableValue ov, String oldValue, String newValue)
                {
                    if (newValue == "Add New System") {
                        systemField = new TextField("");
                        systemField.setPromptText("Enter a System");

                        root2.add(systemField, 2, 1);
                    }
                    else {
                        root2.getChildren().remove(systemField);
                    }
                }
            }
        );

        Label completionLabel = new Label(" Game Completion: ");
        ComboBox<String> completionChooser = new ComboBox<String>();
        completionChooser.getItems().addAll("Unfinished", "Beaten", "Completed", "Null", "Mastered", "Unplayed");

        completionChooser.setPromptText("Choose a Level of Completion");
        // checks which item is selected in the ComboBox
        completionChooser.valueProperty().addListener(new ChangeListener<String>()
            {
                public void changed( ObservableValue ov, String oldValue, String newValue)
                {
                    //TODO
                }
            }
        );

        Label hoursLabel = new Label(" Hours Played: ");
        TextField hoursField = new TextField("");
        //<LINK> http://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        // force the field to be numeric only
        hoursField.textProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        hoursField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });

        Label ratingLabel = new Label(" Rating: ");
        // HBox
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
        root2.setHalignment( addButton, HPos.CENTER );

        root2.add(nameLabel, 0, 0);
        root2.add(nameField, 1, 0);
        root2.add(systemLabel, 0, 1);
        root2.add(systemChooser, 1, 1);
        root2.add(completionLabel, 0, 2);
        root2.add(completionChooser, 1, 2);
        root2.add(hoursLabel, 0, 3);
        root2.add(hoursField, 1, 3);
        root2.add(ratingLabel, 0, 4);
        root2.add(starRow, 1, 4);
        root2.add(addButton, 1, 5);

        newStage.show();
    }
}
