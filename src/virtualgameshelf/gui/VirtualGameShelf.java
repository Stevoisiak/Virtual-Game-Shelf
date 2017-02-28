package virtualgameshelf.gui;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class VirtualGameShelf extends Application {
    public static void main(String[] args) {
        // Automatic VM reset
        try {
            // http://stackoverflow.com/a/32597281
            // Bugfix: Prevents JavaFX ComboBox freezing on Windows 10 touchscreen computers
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
        //mainStage.getIcons().add( new Image("resources/icons/") );

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root, 400, 600);
        mainStage.setScene(mainScene);

        // add stylesheet
        mainScene.getStylesheets().add("resources/stylesheet.css");

        // custom code below --------------------------------------------

        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // used to add games to the library
        MenuButton addGameButton = createAddGameButton();
        root.setMargin(addGameButton, new Insets(16));
        root.setBottom(addGameButton);
        root.setAlignment(addGameButton, Pos.CENTER_RIGHT);

        mainStage.show();
    }

    public MenuBar createMenuBar() {
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

    public MenuButton createAddGameButton() {
        MenuButton addGameButton = new MenuButton(null, new ImageView("resources/icons/add.png"));
        addGameButton.setPopupSide(Side.TOP);

        MenuItem manualAdd = new MenuItem("Manually Add New Game");
        manualAdd.setOnAction(e -> {
            NewGameWindow newGameWindow = new NewGameWindow();
            newGameWindow.show();
        });

        MenuItem autoAdd = new MenuItem("Add New Game Via Steam");
        autoAdd.setOnAction(e -> System.out.println("This feature is not yet available."));

        addGameButton.getItems().addAll(manualAdd, autoAdd);

        // rotates the image 45 degrees when the menu button is "active"
        addGameButton.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    addGameButton.setRotate(addGameButton.getRotate() + 45);
                } else {
                    addGameButton.setRotate(addGameButton.getRotate() + 45);
                }
            }
        });

        return addGameButton;
    }
}
