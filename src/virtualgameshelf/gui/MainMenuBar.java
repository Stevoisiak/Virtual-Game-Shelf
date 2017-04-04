package virtualgameshelf.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import virtualgameshelf.backend.domain.Game;
import virtualgameshelf.backend.domain.GameList;

public class MainMenuBar extends MenuBar {
    public MainMenuBar() {
        Menu menuFile = new Menu("File");
        this.getMenus().add(menuFile);

        MenuItem menuItemNew = new MenuItem("New");
        menuItemNew.setOnAction(e -> onNew());
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
            ArrayList<Game> listOfGames = VirtualGameShelf.gameList.getGameList();
            // TODO: Create method for GameList.print()
            System.out.println("Game List:");
            if (listOfGames != null && !listOfGames.isEmpty()) {
                for (Game g : listOfGames) {
                    System.out.print("\t");
                    System.out.println(g.toString());
                }
            } else {
                System.out.println("\t<empty game list>");
            }
        });
        menuDebug.getItems().add(menuItemPrintGameList);

        MenuItem menuItemOpenSystemList = new MenuItem("Load/Save system_list.csv");
        menuItemOpenSystemList.setOnAction(e -> {
            String inputFilePath = "resources/system_list.csv";
            String outputFilePath = "bin/system_list_output.csv";

            // Read arrayList from file
            System.out.println("Importing from " + inputFilePath);
            List<String[]> arrayList = null;
            try {
                CSVReader reader = new CSVReader(new FileReader(inputFilePath));
                arrayList = reader.readAll();
                reader.close();
            } catch (FileNotFoundException er) {
                er.printStackTrace();
            } catch (IOException er) {
                er.printStackTrace();
            }

            // Output arrayList to screen
            for (String[] s : arrayList) {
               System.out.println(Arrays.toString(s));
            }

            // Output arrayList to file
            System.out.println("Exporting to " + outputFilePath);
            try {
                CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath));
                writer.writeAll(arrayList);
                writer.close();
                Desktop.getDesktop().open(new File(outputFilePath));
            } catch (IOException er) {
                er.printStackTrace();
            }
        });
        menuDebug.getItems().add(menuItemOpenSystemList);
    }

    /** Starts a new gameList */
    public void onNew() {
	VirtualGameShelf.gameList = new GameList();
	VirtualGameShelf.displayGameConsoles();
    }
}
