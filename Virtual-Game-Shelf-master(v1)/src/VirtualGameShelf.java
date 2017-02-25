import javafx.animation.*;
import javafx.application.*;
import javafx.beans.value.*;
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

        // add application icon
        //mainStage.getIcons().add( new Image("icons/") );

        // all the items that appear in the window are (extend) Nodes.
        // Nodes are stored in a tree-like data structure called the scene graph.
        BorderPane root = new BorderPane();

        // the Scene contains the content within the Stage/window.
        // optional: specify size. otherwise, automatically calculated.
        Scene mainScene = new Scene(root, 400, 200);

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

        MenuItem manuelAdd = new MenuItem("Manually Add New Game");
        manuelAdd.setOnAction(e -> System.out.println("This feature is not yet available."));
        
        MenuItem autoAdd = new MenuItem("Add New Game Via Steam");
        autoAdd.setOnAction(e -> System.out.println("This feature is not yet available."));

        menuButton.getItems().addAll(manuelAdd, autoAdd);
        
        // rotates the image 45 degrees when the menu button is "active" 
        // then rotates the image 45 degrees when the menu button is not "active"
        menuButton.showingProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                    if( newValue) {
                        RotateTransition rotation = new RotateTransition(Duration.seconds(0.5), menuButton);
                        rotation.setByAngle(45);
                        rotation.play();
                    } else {
                        RotateTransition rotation = new RotateTransition(Duration.seconds(0.5), menuButton);
                        rotation.setByAngle(45);
                        rotation.play();
                    }

                }
            });

        return menuButton;
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
