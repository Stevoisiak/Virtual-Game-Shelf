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

    public BridgesSessionsImporter() {
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
            GameList list = new GameList();
            list.setId(0L);
            // Games: 12
            int gameListSize = readIntegerValue("Games:");
            
		
            Map<String, Game> gameMap = readGameList(
                    list, gameListSize);
            
            
            readEmptyLine();
            readConstantLine("END\\.");
            createGameList(list, gameListSize);
            /* Work below here! */

            return list;
        }
        
	
        //readStudentList
        private Map<String, Game> readGameList(
                GameList list, int gameListSize) throws IOException {
            Map<String, Game> gameMap = new HashMap<String, Game>(gameListSize);
            List<Game> gameList = new ArrayList<Game>(gameListSize);
            readEmptyLine();
            readConstantLine("Game:");
            for (int i = 0; i < gameListSize; i++) {
                Game game = new Game();
                Game.setId((long) i);
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySemicolonSeparatedValue(line,5);
                game.setName(lineTokens[0]);
                game.setSystem(lineTokens[1]);
                game.setHours(Integer.parseInt(lineTokens[2]));
                game.setCompletion(lineTokens[3]);
                game.setRating(Integer.parseInt(lineTokens[4]));
                
                gameList.add(game);
                gameMap.put(game.getCode(), game);
            }
            list.setGameList(gameList);
            return gameMap;
        }
    }
}
