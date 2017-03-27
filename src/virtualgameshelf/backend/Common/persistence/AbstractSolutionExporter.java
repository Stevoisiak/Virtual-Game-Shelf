import java.io.File;
import java.util.Arrays;

//LoggingMain in app
//ProblemFileComparator business

public abstract class AbstractSolutionExporter extends LoggingMain {

    private static final String DEFAULT_INPUT_FILE_SUFFIX = "xml";
    protected SolutionDao solutionDao;

    public AbstractSolutionExporter(SolutionDao solutionDao) {
        this.solutionDao = solutionDao;
    }

    public AbstractSolutionExporter(boolean withoutDao) {
        if (!withoutDao) {
            throw new IllegalArgumentException("The parameter withoutDao (" + withoutDao + ") must be true.");
        }
        solutionDao = null;
    }

    protected File getInputDir() {
        return new File(solutionDao.getDataDir(), "solved");
    }

    protected File getOutputDir() {
        return new File(solutionDao.getDataDir(), "export");
    }

    protected String getInputFileSuffix() {
        return DEFAULT_INPUT_FILE_SUFFIX;
    }

    public abstract String getOutputFileSuffix();

    public void convertAll() {
        File inputDir = getInputDir();
        if (!inputDir.exists()) {
            throw new IllegalStateException("The directory inputDir (" + inputDir.getAbsolutePath()
                    + ") does not exist.");
        }
        File outputDir = getOutputDir();
        outputDir.mkdirs();
        File[] inputFiles = inputDir.listFiles();
        Arrays.sort(inputFiles, new ProblemFileComparator());
        for (File inputFile : inputFiles) {
            String inputFileName = inputFile.getName();
            if (inputFileName.endsWith("." + getInputFileSuffix())) {
                Solution solution = solutionDao.readSolution(inputFile);
                String outputFileName = inputFileName.substring(0,
                        inputFileName.length() - getInputFileSuffix().length())
                        + getOutputFileSuffix();
                File outputFile = new File(outputDir, outputFileName);
                writeSolution(solution, outputFile);
            }
        }
    }

    public abstract void writeSolution(Solution solution, File outputFile);

    public static abstract class OutputBuilder extends LoggingMain {

    }

}
