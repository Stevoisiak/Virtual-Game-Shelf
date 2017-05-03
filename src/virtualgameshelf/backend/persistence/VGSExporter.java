//add the imports necessary and package for upload


import java.io.IOException;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.examples.common.persistence.AbstractTxtSolutionExporter;

public class VGSExporter extends AbstractTxtSolutionExporter {

    private static final String OUTPUT_FILE_SUFFIX = "txt";

    public static void main(String[] args) {
        new VGSExporter().convertAll();
    }

    public BridgesSessionsExporter() {
        super(new VGSDao());
    }

    @Override
    public String getOutputFileSuffix() {
        return OUTPUT_FILE_SUFFIX;
    }

    public TxtOutputBuilder createTxtOutputBuilder() {
        return new VGSOutputBuilder();
    }

    public static class VGSOutputBuilder extends TxtOutputBuilder {

        private GameList gameList;

        public void setSolution(GameList list) {
            gameList = (GameList) list;
        }

        public void writeSolution() throws IOException {
			bufferedWriter.write("Games: " + gameList.getTotal());
			bufferedWriter.newLine();
			bufferedWriter.newLine();
			bufferedWriter.write("Game:");
			bufferedWriter.newLine();
            for (GameList gameList : gameList.getGameList()) {
                bufferedWriter.write(
                		  gameList.getGame().getName()
                		+ ";" + gameList.getGame().getSystem()
                        + ";" + gameList.getGame().getHours()
                        + ";" + gameList.getGame().getCompletion()
                        + ";" + gameList.getGame().getRating()
                );
                bufferedWriter.newLine();
            }
        }
    }

}
