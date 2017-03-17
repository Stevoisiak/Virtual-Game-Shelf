package virtualgameshelf.gui;

import java.util.ArrayList;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import virtualgameshelf.backend.domain.Game;
import virtualgameshelf.backend.domain.GameList;

public class VirtualGameShelf extends Application {

    private GameList gameList = new GameList();
    private ArrayList<Game> listOfGames = new ArrayList<>();

    public static void main(String[] args) {
        // Automatic VM reset
        try {
            // Bugfix: Prevents JavaFX ComboBox freezing on Windows 10
            // touchscreen computers (http://stackoverflow.com/a/32597281)
            System.setProperty("glass.accessible.force", "false");
            launch(args);
            System.exit(0);
        } catch (Exception error) {
            error.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setTitle("Virtual Game Shelf");

        // add application icon
        // mainStage.getIcons().add(new Image("resources/icons/"));

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root, 400, 600);
        mainStage.setScene(mainScene);

        // add stylesheet
        mainScene.getStylesheets().add("resources/stylesheet.css");

        // top menu bar
        MainMenuBar menuBar = new MainMenuBar();
        root.setTop(menuBar);

        // custom code below ---------------------------------------

        // used to add games to the library
        MenuButton addGameButton = createAddGameButton();
        root.setMargin(addGameButton, new Insets(16));
        root.setBottom(addGameButton);
        root.setAlignment(addGameButton, Pos.CENTER_RIGHT);

        mainStage.show();
    }

    public MenuButton createAddGameButton() {
        MenuButton addGameButton = new MenuButton(null, new ImageView("resources/icons/add.png"));
        addGameButton.setPopupSide(Side.TOP);

        MenuItem manualAdd = new MenuItem("Manually Add New Game");
        manualAdd.setOnAction(e -> {
            NewGameWindow newGameWindow = new NewGameWindow();
            //newGameWindow.show();
            Game newGame = newGameWindow.showAndAddGame();
            if (newGame != null) {
                gameList.addGame(newGame.getName(), newGame.getSystem(), newGame.getHours(), newGame.getCompletion(), newGame.getRating());
            }

            listOfGames = (ArrayList<Game>) gameList.getGame();

            System.out.println("/n");
            for (Game g : listOfGames) {
                System.out.println( g.getName());
            }
            System.out.println("/n");
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
