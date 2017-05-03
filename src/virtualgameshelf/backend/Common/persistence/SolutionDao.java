import java.io.File;



public interface SolutionDao {

    String getDirName();

    File getDataDir();

    String getFileExtension();

    Solution readSolution(File inputSolutionFile);

    void writeSolution(Solution solution, File outputSolutionFile);

}
