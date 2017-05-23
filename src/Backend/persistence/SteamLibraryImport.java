//package ;

import java.io.IOException;
import java.lang.Boolean;
import java.lang.Integer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * this is for importing stuff like this: http://pastebin.com/MKasxda5
 * it is subject to change
*/
public class VGSSteamImporter extends AbstractTxtSolutionImporter {

    private static final String INPUT_FILE_SUFFIX = "csv";
    private static final String SPLIT_REGEX = "[\\ \\t]+";

    public static void main(String[] args) {
        new VGSSteamImporter().convertAll();
    }

    public VGSSteamImporter() {
        super(new VGSDao());
    }

    @Override
    public String getInputFileSuffix() {
        return INPUT_FILE_SUFFIX;
    }

    public TxtInputBuilder createTxtInputBuilder() {
        return new VGSSteamBuilder();
    }

    public static class VGSSteamBuilder extends TxtInputBuilder {

        public Solution readSolution() throws IOException {
            GameList gameList = new GameList();
            gameList.setId(0L);
            //"Name","Playtime","Store"
            readConstantLine("Name","Playtime","Store");
            // TODO: work here to find the total lines of games to fill in gameListSize
            int gameListSize = 12 - 1; //Will have minus one for final line, maybe -2 for first line as well
            
            Map<String, Game> gameMap = readGameList(
                    gameList, gameListSize);
            
            readConstantLine("\"\"");

        //readGameList
        private Map<String, Game> readGameList(
                GameList gameList, int gameListSize) throws IOException {
            Map<String, Game> gameMap = new HashMap<String, Game>(gameListSize);
            List<Game> gameList = new ArrayList<Game>(gameListSize);
            for (int i = 0; i < gameListSize; i++) {
                Game game = new Game();
                game.setId((long) i);
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySpacesOrTabs(line,1); //Line tokens will change based on how much we are taking in
                game.setName(lineTokens[0]);
				game.setHours(lineTokens[1]);
				game.setSteamStore(lineTokens[2]); //Excess and unneeded but stored just in case
				
                gameList.add(game);
                gameMap.put(game.getName(), game);
            }
            gameList.setGameList(gameList);
            return gameMap;
        }
	}
}