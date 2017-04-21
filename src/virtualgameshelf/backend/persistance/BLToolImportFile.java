package virtualgameshelf.backend.persistance;

import virtualgameshelf.backend.domain.Game;
import virtualgameshelf.gui.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

// Steam to BackLoggery
// java -jar bltool.jar --from steam --to backloggery --steam-name $STEAM_NAME --bl-name $BL_NAME --bl-pass $BL_PASS

// Steam to File
// java -jar bltool.jar --from steam --to text  --output SteamData.csv --steam-name $STEAM_NAME

// How to Invoke Clojure Code from Java
// https://clojurefun.wordpress.com/2012/12/24/invoking-clojure-code-from-java/

public class BLToolImportFile {
    public void blToolAddGames(File file) {
        if (file != null) {
            if (file.canRead()) {
                try {
                    CSVReader reader = new CSVReader(new FileReader(file), ',', CSVParser.DEFAULT_QUOTE_CHARACTER, 1);

                    // read line by line
                    String[] record = null;
                    while ((record = reader.readNext()) != null) {

                        Game game = new Game();
                        game.setName(record[1]);
                        game.setSystem(record[2]);
                        game.setHours(0);
                        game.setCompletion(record[3]);
                        game.setRating(0);
                        VirtualGameShelf.gameList.addGame(game);
                    }
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

    }
}
