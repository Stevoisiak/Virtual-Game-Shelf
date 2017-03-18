package virtualgameshelf.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import virtualgameshelf.backend.domain.Game;

public class MainMenuBar extends MenuBar {
    public MainMenuBar() {
        Menu menuFile = new Menu("File");
        this.getMenus().add(menuFile);

        MenuItem menuItemNew = new MenuItem("New");
        // menuItemNew.setOnAction(e -> onNew());
        menuItemNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        menuFile.getItems().add(menuItemNew);

        MenuItem menuItemOpen = new MenuItem("Open");
        // menuItemOpen.setOnAction(e -> onOpen());
        menuItemOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        menuFile.getItems().add(menuItemOpen);

        MenuItem menuItemSave = new MenuItem("Save");
        // menuItemSave.setOnAction(e -> onSave());
        menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        menuFile.getItems().add(menuItemSave);

        menuFile.getItems().add(new SeparatorMenuItem());

        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(e -> Platform.exit());
        // menuItemExit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        menuFile.getItems().add(menuItemExit);

        Menu menuHelp = new Menu("Help");
        this.getMenus().add(menuHelp);

        MenuItem menuViewOnGithub = new MenuItem("View on GitHub");
        menuViewOnGithub.setOnAction(e -> {
            try {
                URI githubURI = new URI("https://github.com/Stevoisiak/Virtual-Game-Shelf");
                Desktop.getDesktop().browse(githubURI);
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
            }
        });
        menuHelp.getItems().add(menuViewOnGithub);

        MenuItem menuItemAbout = new MenuItem("About Game Shelf");
        menuItemAbout.setOnAction(e -> {
            AboutGameShelfWindow aboutWindow = new AboutGameShelfWindow();
            aboutWindow.showAndWait();
        });
        menuHelp.getItems().add(menuItemAbout);

        Menu menuDebug = new Menu("Debug");
        this.getMenus().add(menuDebug);

        MenuItem menuItemPrintGameList = new MenuItem("Print user game list");
        menuItemPrintGameList.setOnAction(e -> {
            List <Game> listOfGames = VirtualGameShelf.gameList.getGame();
            // TODO: Create method for GameList.print()
            System.out.println("Game List:");
            if (listOfGames != null && !listOfGames.isEmpty()) {
                for (Game g : listOfGames) {
                    System.out.print("\t");
                    System.out.println(g.getName()    + ", " +
                            g.getSystem()             + ", " +
                            g.getCompletion()         + ", " +
                            g.getHours() + " hour(s)" + ", " +
                            g.getRating() + " star(s)");
                }
            } else {
                System.out.println("\t<empty game list>");
            }
        });
        menuDebug.getItems().add(menuItemPrintGameList);
    }
}
