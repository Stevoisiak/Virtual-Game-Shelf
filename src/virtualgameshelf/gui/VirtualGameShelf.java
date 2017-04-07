package virtualgameshelf.gui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.opencsv.CSVReader;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import virtualgameshelf.backend.domain.Game;
import virtualgameshelf.backend.domain.GameList;

public class VirtualGameShelf extends Application {
    /** User's complete list of games. Static to allow for global access */
    public static GameList gameList = new GameList();
    /** Used to look up full names of consoles. ("PS4" -> "PlayStation 4") */
    protected static Map<String, String> systemNameMap;
    /** Visual display of gameList */
    private static VBox gameListVBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setTitle("Virtual Game Shelf");

        // add application icon
        // mainStage.getIcons().add(new Image("icons/"));

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root, 400, 600);
        mainStage.setScene(mainScene);

        // used to add a scroll bar to the page
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        // add stylesheet
        mainScene.getStylesheets().add("stylesheet.css");

        // top menu bar
        MainMenuBar menuBar = new MainMenuBar(mainStage);
        root.setTop(menuBar);

        // custom code below ---------------------------------------
        initializeSystemNameMap();

        // used to display games in library
        gameListVBox = new VBox();
        gameListVBox.setPadding( new Insets(16) );
        gameListVBox.setSpacing(16);
        gameListVBox.setAlignment( Pos.CENTER );
        scroll.setContent(gameListVBox);

        // used to add games to the library
        MenuButton addGameButton = createAddGameButton();
        root.setMargin(addGameButton, new Insets(16));
        root.setBottom(addGameButton);
        root.setAlignment(addGameButton, Pos.CENTER_RIGHT);

        mainStage.show();
    }

    public MenuButton createAddGameButton() {
        MenuButton addGameButton = new MenuButton(null, new ImageView("icons/add.png"));
        addGameButton.setPopupSide(Side.TOP);

        MenuItem manualAdd = new MenuItem("Manually Add New Game");
        manualAdd.setOnAction(e -> {
            NewGameWindow newGameWindow = new NewGameWindow();
            Game newGame = newGameWindow.showAndAddGame();
            if (newGame != null) {
                // Add title to game list
                gameList.addGame(newGame);
                displayGameConsoles();
            }
        });

        MenuItem autoAdd = new MenuItem("Add New Game Via Steam");
        autoAdd.setOnAction(e -> System.out.println("This feature is not yet available."));

        addGameButton.getItems().addAll(manualAdd, autoAdd);

        // rotates the image 45 degrees when the menu button is "active"
        addGameButton.showingProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                addGameButton.setRotate(45.0);
            } else {
                addGameButton.setRotate(0.0);
            }
        });

        return addGameButton;
    }

    /** Used to display the list of games */
    public static void displayGameConsoles() {
        TreeItem<String> rootNode = new TreeItem<>("Consoles", new ImageView("icons/gamepad.png"));
        rootNode.setExpanded(true);

        for (Game g : gameList.getGameList()) {
            TreeItem<String> gameLeaf = new TreeItem<>(g.getName() + "\n" + g.getSystem() +
                    "\n" + g.getCompletion() + "\n" + g.getHours() + " hours played \n" + g.getRating() + " star(s)");
            boolean found = false;

            String displayName = getSystemDisplayName(g.getSystem());

            for (TreeItem<String> depNode : rootNode.getChildren()) {
                if (depNode.getValue().contentEquals(displayName)) {
                    depNode.getChildren().add(gameLeaf);
                    found = true;
                    break;
                }
            }

            if (!found) {
                TreeItem<String> depNode = new TreeItem<>(displayName, new ImageView("icons/vintage.png"));
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(gameLeaf);
            }
        }

        TreeView<String> treeView = new TreeView<>(rootNode);

        treeView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<TreeItem<String>>) (observable, oldValue, newValue) -> {
            TreeItem<String> selectedItem = newValue;

            // Ensures 'edit game' prompt only shows for games
            if (selectedItem.isLeaf() && selectedItem.getParent() != null) {
                displayEditGameAlert(selectedItem);
            }
        });

        // Clear and redraw game list
        gameListVBox.getChildren().clear();
        gameListVBox.getChildren().add(treeView);
    }

    /** Initialize hashmap to lookup console names. (e.g.: "PS4" -> "PlayStation 4") */
    private void initializeSystemNameMap() {
        systemNameMap = new LinkedHashMap<>();
        List<String[]> systemList = null;

        // Read in systemList from file
        try {
            CSVReader reader = new CSVReader(new FileReader("resources/system_list.csv"));
            systemList = reader.readAll();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Populate systemName hash map
        if (systemList != null) {
            for (String[] s : systemList) {
                String name = s[0];
                String displayName = s[1];
                if (name != null && !name.equals("name")
                        && displayName != null && !displayName.equals("displayName")) {
                    systemNameMap.put(name, displayName);
                }
            }
        }
    }

    /**
     * Returns game system's full display name
     * @param  system Abbreviated system name. [ie: PS4]
     * @return        Full system display name. [ie: PlayStation 4].
     */
    public static String getSystemDisplayName(String system) {
        if (systemNameMap.containsKey(system)) {
            return systemNameMap.get(system);
        } else {
            return system;
        }
    }

    /** Display option to edit or delete a game */
    public static void displayEditGameAlert(TreeItem<String> selectedItem) {
        int index = -1;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null); // TODO: Use game title for header
        alert.setContentText("Would you like to:");

        ButtonType deleteGame = new ButtonType("Delete Game");
        ButtonType editGame = new ButtonType("Edit Game");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteGame, editGame, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == deleteGame){
            index = getGameIndex(selectedItem);
            gameList.getGameList().remove(index);
            displayGameConsoles();
        }
        else if (result.get() == editGame) {
            index = getGameIndex(selectedItem);
            ArrayList<Game> tempGameList = (ArrayList<Game>) gameList.getGameList().clone();

            NewGameWindow newGameWindow = new NewGameWindow(tempGameList.get(index) );
            Game newGame = newGameWindow.showAndAddGame();
            if (newGame != null) {
                // Add title to game list
                gameList.getGameList().remove(index);
                gameList.addGame(newGame);
                displayGameConsoles();
            }
        }
        else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    /** Takes TreeItem and returns its location in GameList as an int */
    public static int getGameIndex(TreeItem<String> selectedItem) {
        int index = -1;

        for (Game g : gameList.getGameList()) {
            if (selectedItem.getValue().equals(g.gameString())) {
                for (int i = 0; i < gameList.getGameList().size(); i++) {
                    if (g.gameString().equals(gameList.getGameList().get(i).gameString())) {
                        index = i;
                    }
                }
            }
        }

        return index;
    }

    public static void setGameList(GameList newGameList) {
        gameList = newGameList;
        Collections.sort(gameList.getGameList());
        displayGameConsoles();
    }
}
