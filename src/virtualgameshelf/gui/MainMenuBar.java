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
import java.util.Optional;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import virtualgameshelf.backend.domain.Game;
import virtualgameshelf.backend.domain.GameList;
import virtualgameshelf.backend.persistance.SteamCommunityGameImporter;

public class MainMenuBar extends MenuBar {
    /** Default folder for saving user data and settings */
    private static final String userDataDirName = "user/";
    private final Stage mainStage;
    private final FileChooser fileChooser;

    public MainMenuBar(Stage mainStage) {
        this.mainStage = mainStage;
        fileChooser = new FileChooser();

        Menu menuFile = new Menu("File");
        this.getMenus().add(menuFile);

        MenuItem menuItemNew = new MenuItem("New");
        menuItemNew.setOnAction(e -> onNew());
        menuItemNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        menuFile.getItems().add(menuItemNew);

        MenuItem menuItemOpen = new MenuItem("Open...");
        menuItemOpen.setOnAction(e -> onOpen());
        menuItemOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        menuFile.getItems().add(menuItemOpen);

        MenuItem menuItemSave = new MenuItem("Save");
        menuItemSave.setOnAction(e -> onSave());
        menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        menuFile.getItems().add(menuItemSave);

        MenuItem menuItemSaveAs = new MenuItem("Save As...");
        menuItemSaveAs.setOnAction(e -> onSaveAs());
        menuFile.getItems().add(menuItemSaveAs);

        menuFile.getItems().add(new SeparatorMenuItem());

        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(e -> Platform.exit());
        // menuItemExit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        menuFile.getItems().add(menuItemExit);

        Menu menuHelp = new Menu("Help");
        this.getMenus().add(menuHelp);

        MenuItem menuViewOnGithub = new MenuItem("View on GitHub");
        menuViewOnGithub.setOnAction(e -> onViewGitHub());
        menuHelp.getItems().add(menuViewOnGithub);

        MenuItem menuItemAbout = new MenuItem("About Game Shelf");
        menuItemAbout.setOnAction(e -> onAboutGameShelf());
        menuHelp.getItems().add(menuItemAbout);

        Menu menuDebug = new Menu("Debug");
        this.getMenus().add(menuDebug);

        MenuItem menuItemPrintGameList = new MenuItem("Print current game list");
        menuItemPrintGameList.setOnAction(e -> onPrintCurrentGameList());
        menuDebug.getItems().add(menuItemPrintGameList);

        MenuItem menuItemOpenSystemList = new MenuItem("Import/Export system_list.csv");
        menuItemOpenSystemList.setOnAction(e -> onImportExportSystemList());
        menuDebug.getItems().add(menuItemOpenSystemList);

        MenuItem menuItemTestImportSteamLibraryXML = new MenuItem("Test importing Steam library (.XML)");
        menuItemTestImportSteamLibraryXML.setOnAction(e -> onTestImportSteamLibraryXML());
        menuDebug.getItems().add(menuItemTestImportSteamLibraryXML);
    }

    /** Clears current gameList. */
    public void onNew() {
        GameShelf.setGameList(new GameList());
    }

    /** Display prompt to load gameList from file. */
    public void onOpen() {
        // TODO: Auto-load last saved gameList on startup
        // TODO: Create global methods for saving/loading gameList (Dedicated FileIO class?)
        // TODO: Open fileChooser in last opened directory
        List<Game> loadedGameList = new ArrayList<>();
        boolean loadSuccess = false;

        configureFileChooser(this.fileChooser);
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(mainStage);

        if (file != null) {
            if (file.canRead()) {
                try {
                    CSVReader reader = new CSVReader(new FileReader(file), ',', CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
                    // String[] header = Game.getColumnHeaders();

                    // read line by line
                    String[] record = null;
                    while ((record = reader.readNext()) != null) {
                        // TODO: Error handling for string-to-int conversion & empty fields
                        // TODO: Read columns by field name, regardless of order
                        Game game = new Game();
                        game.setName(record[0]);
                        game.setSystem(record[1]);
                        game.setHours(Integer.parseInt(record[2]));
                        game.setCompletion(record[3]);
                        game.setRating(Integer.parseInt(record[4]));
                        loadedGameList.add(game);
                    }
                    loadSuccess = true;
                    reader.close();
                } catch (FileNotFoundException er) {
                    er.printStackTrace();
                } catch (IOException er) {
                    er.printStackTrace();
                } catch (NumberFormatException er) {
                    er.printStackTrace();
                }
            } else {
                System.err.println("Cannot open " + file.getAbsolutePath() + ". File may be read-only or in use by another program.");
            }
        }

        // Replace gameList
        if (loadSuccess) {
            GameShelf.setGameList(new GameList((ArrayList<Game>) loadedGameList));
        }
    }

    /**
     * Save gameList to /user data folder.
     * Saves to ../[app-dir]/user/game_list.csv
     */
    public void onSave() {
        // TODO: Save to last place gameList file was loaded from or saved to.
        // TODO: Error handling for "File in use" when overwriting a file.

        // initial setup
        String appPath = GameShelf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String gameListFileName = "game_list.csv";

        File userDataDirectory = new File(appPath + userDataDirName);
        File outFile = new File(appPath + userDataDirName + gameListFileName);

        // create user directory if it doesn't exist
        if (!userDataDirectory.exists()) {
            userDataDirectory.mkdir();
        }

        // Save to file
        if (outFile != null) {
            try {
                saveGameListToFile(outFile);
            } catch (FileNotFoundException er) {
                er.printStackTrace(); // Can occur if directories do not exist
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    /** Display prompt to save gameList to a file. */
    public void onSaveAs() {
        // TODO: Error handling for "File in use" when overwriting a file

        configureFileChooser(this.fileChooser);
        fileChooser.setTitle("Save As");
        File outFile = fileChooser.showSaveDialog(mainStage);

        // Save to file
        if (outFile != null) {
            try {
                saveGameListToFile(outFile);
            } catch (FileNotFoundException er) {
                er.printStackTrace(); // Can occur if directories do not exist
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    /** View project source code on GitHub. */
    public static void onViewGitHub() {
        try {
            URI githubURI = new URI("https://github.com/Stevoisiak/Virtual-Game-Shelf");
            Desktop.getDesktop().browse(githubURI);
        } catch (URISyntaxException | IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Display info about Game Shelf. */
    public static void onAboutGameShelf() {
        AboutGameShelfWindow aboutWindow = new AboutGameShelfWindow();
        aboutWindow.showAndWait();
    }

    /** Debug method to print all games from the current game list. */
    public static void onPrintCurrentGameList() {
        ArrayList<Game> gameList = GameShelf.gameList.getGameList();
        System.out.println("Game List:");
        if (gameList != null && !gameList.isEmpty()) {
            for (Game g : gameList) {
                System.out.print("\t");
                System.out.println(g.toString());
            }
        } else {
            System.out.println("\t<empty game list>");
        }
    }

    /** Debug method for importing/exporting system list from .csv file. */
    public static void onImportExportSystemList() {
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
    }

    /** Test importing games into gamelist from Steam library (.xml file). */
    public static void onTestImportSteamLibraryXML() {
        // TODO: ID field should be empty by default in final code.
        //       Leaving 'Stevoisiak' as default ID for testing.

        // TODO: Accept multiple formats for Steam ID.
        //       (ie: gabelogannewell, 76561197960287930, STEAM_0:0:11101,
        //            [U:1:22202], http://steamcommunity.com/id/gabelogannewell/,
        //            http://steamcommunity.com/profiles/76561197960287930/,
        //            http://steamcommunity.com/profiles/[U:1:22202]/)
        //       Convert types with (https://github.com/xPaw/SteamID.php)?
        TextInputDialog steamIdDialog = new TextInputDialog("Stevoisiak");
        steamIdDialog.setTitle("Import library from Steam");
        steamIdDialog.setHeaderText(null);
        steamIdDialog.setContentText("Please enter your steam ID.");

        Optional<String> steamID = steamIdDialog.showAndWait();
        if (steamID.isPresent()) {
            SteamCommunityGameImporter importer = new SteamCommunityGameImporter();
            importer.steamCommunityAddGames(steamID.get());
        }
    }

    /**
     * Saves current gameList to a file.
     *
     * @param outFile
     *            gameList will be written to this file
     */
    private static void saveGameListToFile(File outFile) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(outFile), ',');
        String[] header = Game.getColumnHeaders();
        csvWriter.writeNext(header);
        for (Game g : GameShelf.gameList.getGameList()) {
            csvWriter.writeNext(g.toStringArray());
        }
        csvWriter.close();
        System.out.println("Saved to " + outFile.getPath());
    }

    /**
     * Configure FileChooser window for saving/opening GameList from file.
     *
     * @param fileChooser
     *            FileChooser you want modified.
     */
    private static void configureFileChooser(FileChooser fileChooser) {
        // set initial directory
        String appPath = GameShelf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File userDataDirectory = new File(appPath + userDataDirName);
        File userDirectory = new File(System.getProperty("user.dir"));

        if (userDataDirectory.canRead()) {
            fileChooser.setInitialDirectory(userDataDirectory);
        } else if (userDirectory.canRead()) {
            fileChooser.setInitialDirectory(userDirectory);
        } else {
            fileChooser.setInitialDirectory(new File("C:/"));
        }

        // set selectable file types
        FileChooser.ExtensionFilter filterCSV = new FileChooser.ExtensionFilter("CSV", "*.csv");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(filterCSV);

        // set default file name
        if (fileChooser.getInitialFileName() == null) {
            fileChooser.setInitialFileName("game_list");
        }
    }
}
