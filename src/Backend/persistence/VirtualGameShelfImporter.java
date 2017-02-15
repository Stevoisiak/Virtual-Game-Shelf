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


public class VGSImporter extends AbstractTxtSolutionImporter {

    private static final String INPUT_FILE_SUFFIX = "txt";
    private static final String SPLIT_REGEX = "[\\ \\t]+";

    public static void main(String[] args) {
        new VGSImporter().convertAll();
    }

    public VGSImporter() {
        super(new VGSDao());
    }

    @Override
    public String getInputFileSuffix() {
        return INPUT_FILE_SUFFIX;
    }

    public TxtInputBuilder createTxtInputBuilder() {
        return new VGSInputBuilder();
    }

    public static class VGSInputBuilder extends TxtInputBuilder {

        public Solution readSolution() throws IOException {
            GameList gameList = new GameList();
            gameList.setId(0L);
            // Explanation:
            readConstantLine("gameName gameSystem hoursPlayed Finish");
            // Games: 12
            int gameListSize = readIntegerValue("Games:");
            
            Map<String, Game> gameMap = readGameList(
                    gameList, gameListSize);
            
            
            readEmptyLine();
            readConstantLine("END\\.");

        //readGameList
        private Map<String, Game> readGameList(
                GameList gameList, int gameListSize) throws IOException {
            Map<String, Game> gameMap = new HashMap<String, Game>(gameListSize);
            List<Game> gameList = new ArrayList<Game>(gameListSize);
            readEmptyLine();
            readConstantLine("Game:");
            for (int i = 0; i < gameListSize; i++) {
                Game game = new Game();
                game.setId((long) i);
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySpacesOrTabs(line,5); //Line tokens will change based on how much we are taking in
                game.setName(lineTokens[0]);
				game.setSystem(lineTokens[1]);
				game.setHours(lineTokens[2]);
				game.setFinish(lineTokens[3]);
				game.setDLC(lineTokens[4]);
				
				/* for loop for achievements TODO
				 *
				*/
				
                gameList.add(game);
                gameMap.put(game.getName(), game);
            }
            gameList.setGameList(gameList);
            return gameMap;
        }
	}
}