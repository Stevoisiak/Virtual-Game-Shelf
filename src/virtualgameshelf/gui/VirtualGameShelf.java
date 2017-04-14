package virtualgameshelf.gui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.controlsfx.control.CheckTreeView;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.opencsv.CSVReader;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    /** User's complete list of games. Static to allow for global access. */
    public static GameList gameList;
    /** Used to look up full names of consoles. ("PS4" - "PlayStation 4") */
    protected static BiMap<String, String> systemNameMap;
    /** Visual display of {@link #gameList}. */
    private static VBox gameListVBox;

    // used when deleting games
    static ArrayList<String> selectedGamesString = new ArrayList<>();
    static Button deleteButton;

    // used when editing games
    static Button editButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        gameList = new GameList();
        mainStage.setTitle("Virtual Game Shelf");

        // add application icon
        // mainStage.getIcons().add(new Image("icons/"));

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root, 400, 600);
        mainStage.setScene(mainScene);

        // used to add a scroll bar to the page
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
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
        gameListVBox.getStyleClass().add("gameVBox");
        scroll.setContent(gameListVBox);
        refreshGameList();

        // used to display the delete button and add game button
        HBox footer = new HBox();
        footer.getStyleClass().add("footer");

        deleteButton = createDeleteButton();
        deleteButton.setAlignment(Pos.CENTER_LEFT);
        deleteButton.setDisable(true);

        editButton = createEditButton();
        editButton.setAlignment(Pos.CENTER_LEFT);
        editButton.setDisable(true);

        // used to add games to the library
        MenuButton addGameButton = createAddGameButton();
        addGameButton.setAlignment(Pos.CENTER_RIGHT);

        footer.getChildren().addAll(deleteButton, editButton, addGameButton);
        root.setBottom(footer);

        mainStage.show();
    }

    // creates button for deleting games
    public Button createDeleteButton() {
        Button deleteButton = new Button("Delete Game(s)");

        deleteButton.setOnAction(e -> displayDeleteGameAlert());

        return deleteButton;
    }

 // creates button for editing games
    public Button createEditButton() {
        Button editButton = new Button("Edit Game");

        editButton.setOnAction(e -> displayEditGameAlert());

        return editButton;
    }

    // creates alert for deleting games
    public static void displayDeleteGameAlert() {
        int index = -1;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected games?");

        ButtonType deleteGame = new ButtonType("Delete Game(s)");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteGame, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == deleteGame){
            for (String g : selectedGamesString) {
                index = getGameIndex(g);
                gameList.getGameList().remove(index);
            }

            refreshGameList();
            deleteButton.setDisable(true);
        }
        else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    // creates alert for editing games
    public static void displayEditGameAlert() {
        int index = getGameIndex(selectedGamesString.get(0));
        ArrayList<Game> tempGameList = (ArrayList<Game>) gameList.getGameList().clone();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("" + tempGameList.get(index).getName());
        alert.setHeaderText(null);
        alert.setContentText("Would you like to edit the game " + tempGameList.get(index).getName() + "?");

        ButtonType editGame = new ButtonType("Edit Game");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(editGame, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == editGame) {
            NewGameWindow newGameWindow = new NewGameWindow(tempGameList.get(index) );
            Game newGame = newGameWindow.showAndAddGame();
            if (newGame != null) {
                // Add title to game list
                gameList.getGameList().remove(index);
                gameList.addGame(newGame);
                refreshGameList();
                editButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        }
        else {
            // ... user chose CANCEL or closed the dialog
        }
    }

 // Takes String and returns its location in the game list as an int
    public static int getGameIndex(String selectedGame) {
        int index = -1;

        for (Game g : gameList.getGameList()) {
            if (selectedGame.equals(g.gameString())) {
                for (int i = 0; i < gameList.getGameList().size(); i++) {
                    if (g.gameString().equals(gameList.getGameList().get(i).gameString())) {
                        index = i;
                    }
                }
            }
        }

        return index;
    }

    /**
     * Creates button for adding new games.
     * <p>
     * Includes options for manually adding games or importing from Steam.
     *
     * @return button for adding new games.
     */
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
                refreshGameList();
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

    /**
     * Refresh the displayed list of games.
     * Should be called whenever gameList has been modified.
     */
    public static void refreshGameList() {
        CheckBoxTreeItem<String> rootNode = new CheckBoxTreeItem<>("Consoles", new ImageView("icons/gamepad.png"));
        rootNode.setExpanded(true);

        for (Game g : gameList.getGameList()) {
            CheckBoxTreeItem<String> gameLeaf = new CheckBoxTreeItem<>(g.getName() + "\n" + g.getSystem() +
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
                CheckBoxTreeItem<String> depNode = new CheckBoxTreeItem<>(displayName, new ImageView("icons/vintage.png"));
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(gameLeaf);
            }
        }

        CheckTreeView<String> checkTreeView = new CheckTreeView<>(rootNode);

     // and listen to the relevant events (e.g. when the checked items change).
        checkTreeView.getCheckModel().getCheckedItems().addListener((ListChangeListener<TreeItem<String>>) c -> {
             ObservableList<TreeItem<String>> selectedGames = checkTreeView.getCheckModel().getCheckedItems();

             if (selectedGames.size() > 0) {
                 selectedGamesString.clear();

                 if (selectedGames.size() == 1) {
                     editButton.setDisable(false);
                     deleteButton.setDisable(false);

                     selectedGamesString.add(selectedGames.get(0).getValue());
                 }
                 else {
                     deleteButton.setDisable(false);

                     for (TreeItem<String> s : selectedGames) {
                         String singleGame = s.getValue();

                         // Ensures 'delete game' prompt only shows for games
                         if (s.isLeaf() && s.getParent() != null) {

                             selectedGamesString.add(singleGame);
                         }
                     }
                 }
                 if (selectedGames.size() > 1) {
                     editButton.setDisable(true);
                 }
             }
             else {
                 deleteButton.setDisable(true);
                 editButton.setDisable(true);
             }
         });

        // Clear and redraw game list
        gameListVBox.getChildren().clear();
        gameListVBox.getChildren().add(checkTreeView);
    }

    /** Initialize {@link #systemNameMap} for looking up console names. */
    private void initializeSystemNameMap() {
        systemNameMap = HashBiMap.create();
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
     * Returns game system's full display name.
     * <p>
     * For example, calling this method with the argument "PS4" will return
     * "PlayStation 4".
     *
     * @param system
     *            abbreviated system name.
     * @return full system display name.
     */
    public static String getSystemDisplayName(String systemShortName) {
        if (systemNameMap.containsKey(systemShortName)) {
            return systemNameMap.get(systemShortName);
        } else {
            return systemShortName;
        }
    }

    /**
     * Returns game system's abbreviated name.
     * <p>
     * For example, calling this method with the argument "PlayStation 4" will return
     * "PS4".
     *
     * @param system
     *            full system display name.
     * @return abbreviated system name.
     */
    public static String getSystemShortName(String systemLongName) {
        if (systemNameMap.containsValue(systemLongName)) {
            BiMap<String, String> invSystemNameMap = systemNameMap.inverse();
            return invSystemNameMap.get(systemLongName);
        } else {
            return systemLongName;
        }
    }

    public static void setGameList(GameList newGameList) {
        gameList = newGameList;
        Collections.sort(gameList.getGameList());
        refreshGameList();
    }
}
