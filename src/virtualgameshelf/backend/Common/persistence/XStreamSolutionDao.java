import java.io.File;

public abstract class XStreamSolutionDao extends AbstractSolutionDao {

    protected XStreamSolutionFileIO xStreamSolutionFileIO;

    public XStreamSolutionDao(String dirName, Class... xStreamAnnotations) {
        super(dirName);
        xStreamSolutionFileIO = new XStreamSolutionFileIO(xStreamAnnotations);
    }

    public String getFileExtension() {
        return xStreamSolutionFileIO.getOutputFileExtension();
    }

    public Solution readSolution(File inputSolutionFile) {
        Solution solution = xStreamSolutionFileIO.read(inputSolutionFile);
        logger.info("Opened: {}", inputSolutionFile);
        return solution;
    }

    public void writeSolution(Solution solution, File outputSolutionFile) {
        xStreamSolutionFileIO.write(solution, outputSolutionFile);
        logger.info("Saved: {}", outputSolutionFile);
    }

}
