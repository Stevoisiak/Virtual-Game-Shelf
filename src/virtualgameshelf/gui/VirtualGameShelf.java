package virtualgameshelf.gui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
import virtualgameshelf.backend.fileIO.CSVReader;

public class VirtualGameShelf extends Application {
    /** User's complete list of games. Static to allow for global access */
    protected static GameList gameList = new GameList();
    /** Used to look up full names of consoles. ("PS4" -> "PlayStation 4") */
    protected static Map<String, String> systemNameMap;
    /** Consoles taken from gameList */
    ArrayList<String> shrunkenConsoleList = new ArrayList<>();

    VBox gameConsoleList;

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

        // used to add a scroll bar to the page
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        // add stylesheet
        mainScene.getStylesheets().add("resources/stylesheet.css");

        // top menu bar
        MainMenuBar menuBar = new MainMenuBar();
        root.setTop(menuBar);

        // custom code below ---------------------------------------
        initializeSystemNameMap();

        // used to display games in library
        gameConsoleList = new VBox();
        gameConsoleList.setPadding( new Insets(16) );
        gameConsoleList.setSpacing(16);
        gameConsoleList.setAlignment( Pos.CENTER );
        scroll.setContent(gameConsoleList);

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
            Game newGame = newGameWindow.showAndAddGame();
            if (newGame != null) {
                // Add title to game list
                gameList.addGame(newGame.getName(), newGame.getSystem(), newGame.getHours(), newGame.getCompletion(), newGame.getRating());
                makeShrunkenList(newGame.getSystem());

                // used to display games in gameList
                gameConsoleList.getChildren().clear();

                TreeView<VBox> treeView = new TreeView<>();

                treeView = displayGameConsoles();

                gameConsoleList.getChildren().add(treeView);
            }
        });

        MenuItem autoAdd = new MenuItem("Add New Game Via Steam");
        autoAdd.setOnAction(e -> System.out.println("This feature is not yet available."));

        addGameButton.getItems().addAll(manualAdd, autoAdd);

        // rotates the image 45 degrees when the menu button is "active"
        addGameButton.showingProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                addGameButton.setRotate(addGameButton.getRotate() + 45);
            } else {
                addGameButton.setRotate(addGameButton.getRotate() + 45);
            }
        });

        return addGameButton;
    }

    // used to make a list of all the consoles without repeats
    public void makeShrunkenList(String console) {
        boolean found = false;

        for (String c : shrunkenConsoleList) {
            if (console.equals(c)) {
                found = true;
            }
        }

        if (!found) {
            shrunkenConsoleList.add(console);
        }
    }

    // used to display the list of games
    public TreeView displayGameConsoles() {
        ArrayList<Game> listOfGames = new ArrayList<>();
        listOfGames = (ArrayList<Game>) gameList.getGame();

        TreeItem<String> rootNode = new TreeItem<>("Consoles", new ImageView("resources/icons/gamepad.png"));
        rootNode.setExpanded(true);

        for (Game g : listOfGames) {
            TreeItem<String> gameLeaf = new TreeItem<>(g.getName() + "\n" + g.getSystem() +
                    "\n" + g.getCompletion() + "\n" + g.getHours() + " hours played \n" + g.getRating() + " star(s)");
            boolean found = false;

            for (TreeItem<String> depNode : rootNode.getChildren()) {
                if (depNode.getValue().contentEquals(g.getSystem())) {
                    depNode.getChildren().add(gameLeaf);
                    found = true;
                    break;
                }
            }

            if (!found){
                TreeItem<String> depNode = new TreeItem<>(g.getSystem(), new ImageView("resources/icons/vintage.png"));
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(gameLeaf);
            }
        }

        TreeView<String> treeView = new TreeView<>(rootNode);

        return treeView;
    }

    // used to display the list of games (other method)
    public TreeView displayGameConsoles2() {
        ArrayList<Game> listOfGames = new ArrayList<>();
        listOfGames = (ArrayList<Game>) gameList.getGame();

        VBox title = new VBox();
        //title.setPadding( new Insets(16) );
        //title.setSpacing(16);
        //title.setAlignment( Pos.CENTER );

        Label consoleLabel = new Label("Consoles");
        title.getChildren().add(consoleLabel);

        Label nameLabel = new Label();
        Label systemLabel = new Label();
        Label completionLabel = new Label();
        Label hoursLabel = new Label();
        Label ratingLabel = new Label();

        TreeItem<VBox> rootNode = new TreeItem<>(title, new ImageView("resources/icons/gamepad.png"));
        rootNode.setExpanded(true);

        for (Game g : listOfGames) {

            VBox gameInfo = new VBox();
            //gameInfo.setPadding( new Insets(16) );
            //gameInfo.setSpacing(16);
            //gameInfo.setAlignment( Pos.CENTER_RIGHT );

            nameLabel.setText(g.getName());
            systemLabel.setText(g.getSystem());
            completionLabel.setText(g.getCompletion());
            hoursLabel.setText(g.getHours() + " Hours Played");
            ratingLabel.setText(g.getRating() + " Star(s)");

            gameInfo.getChildren().addAll(nameLabel, systemLabel, completionLabel, hoursLabel, ratingLabel);

            TreeItem<VBox> gameLeaf = new TreeItem<>(gameInfo);
            boolean found = false;

            for (TreeItem<VBox> depNode : rootNode.getChildren()) {
                System.out.println(depNode.getValue().getChildren().contains(g.getSystem()));
                if (depNode.getValue().getChildren().contains(g.getSystem())) {
                    depNode.getChildren().add(gameLeaf);
                    found = true;
                    break;
                }
            }

            if (!found){
                VBox system = new VBox();
                //system.setPadding( new Insets(16) );
                //system.setSpacing(16);
                //system.setAlignment( Pos.CENTER );

                Label systemTitleLabel = new Label(g.getSystem() + "");
                system.getChildren().add(systemTitleLabel);

                TreeItem<VBox> depNode = new TreeItem<>(system, new ImageView("resources/icons/vintage.png"));
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(gameLeaf);
            }
        }

        TreeView<VBox> treeView = new TreeView<>(rootNode);

        return treeView;
    }

    /** Initialize hashmap to lookup console names. (e.g.: "PS4" -> "PlayStation 4") */
    private void initializeSystemNameMap() {
        systemNameMap = new LinkedHashMap<>();
        ArrayList<String[]> systemList = CSVReader.readFromFile("src/resources/system_list.csv", ",");
        for (String[] s : systemList) {
            String name = s[0];
            String displayName = s[1];
            if (name != null && !name.equals("name")
                    && displayName != null && !displayName.equals("displayName")) {
                systemNameMap.put(name, displayName);
            }
        }
    }

    /** if available, return system's full display name */
    public static String getSystemDisplayName(String system) {
        if (systemNameMap.containsKey(system)) {
            system = systemNameMap.get(system);
        }
        return system;
    }
}
