import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSolutionDao implements SolutionDao {

    /**
     * The path to the data directory, preferably with unix slashes for portability.
     * For example: -D{@value #DATA_DIR_SYSTEM_PROPERTY}=sources/data/
     */
    public static final String DATA_DIR_SYSTEM_PROPERTY = "org.optaplanner.examples.dataDir";

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    protected String dirName;
    protected File dataDir;

    public AbstractSolutionDao(String dirName) {
        this.dirName = dirName;
        String dataDirPath = System.getProperty(DATA_DIR_SYSTEM_PROPERTY, "data/");
        dataDir = new File(dataDirPath, dirName);
        if (!dataDir.exists()) {
            throw new IllegalStateException("The directory dataDir (" + dataDir.getAbsolutePath()
                    + ") does not exist.\n" +
                    " Either the working directory should be set to the directory that contains the data directory" +
                    " (which is not the data directory itself), or the system property "
                    + DATA_DIR_SYSTEM_PROPERTY + " should be set properly.\n" +
                    " The data directory is different in a git clone (optaplanner/optaplanner-examples/data)" +
                    " and in a release zip (examples/sources/data).\n" +
                    " In an IDE (IntelliJ, Eclipse, NetBeans), open the \"Run configuration\""
                    + " to change \"Working directory\" (or add the system property in \"VM options\").");
        }
    }

    public String getDirName() {
        return dirName;
    }

    public File getDataDir() {
        return dataDir;
    }

}
