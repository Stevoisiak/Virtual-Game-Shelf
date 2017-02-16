package src;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VirtualGameShelf extends Application {
    public static void main(String[] args) {
        // Automatic VM reset, thanks to Joseph Rachmuth.
        try {
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

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root, 400, 200);
        mainStage.setScene(mainScene);

        MenuBar menuBar = generateMenuBar();
        root.setTop(menuBar);

        mainStage.show();
    }

    public MenuBar generateMenuBar()
    {
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
}
