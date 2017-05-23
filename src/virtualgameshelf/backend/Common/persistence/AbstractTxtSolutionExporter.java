import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.optaplanner.core.api.domain.solution.Solution;

public abstract class AbstractTxtSolutionExporter extends AbstractSolutionExporter {

    protected static final String DEFAULT_OUTPUT_FILE_SUFFIX = "txt";

    protected AbstractTxtSolutionExporter(SolutionDao solutionDao) {
        super(solutionDao);
    }

    protected AbstractTxtSolutionExporter(boolean withoutDao) {
        super(withoutDao);
    }

    public String getOutputFileSuffix() {
        return DEFAULT_OUTPUT_FILE_SUFFIX;
    }

    public abstract TxtOutputBuilder createTxtOutputBuilder();

    public void writeSolution(Solution solution, File outputFile) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            TxtOutputBuilder txtOutputBuilder = createTxtOutputBuilder();
            txtOutputBuilder.setBufferedWriter(bufferedWriter);
            txtOutputBuilder.setSolution(solution);
            txtOutputBuilder.writeSolution();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not write the file (" + outputFile.getName() + ").", e);
        } finally {
            IOUtils.closeQuietly(bufferedWriter);
        }
        logger.info("Exported: {}", outputFile);
    }

    public static abstract class TxtOutputBuilder extends OutputBuilder {

        protected BufferedWriter bufferedWriter;

        public void setBufferedWriter(BufferedWriter bufferedWriter) {
            this.bufferedWriter = bufferedWriter;
        }

        public abstract void setSolution(Solution solution);

        public abstract void writeSolution() throws IOException;

        // ************************************************************************
        // Helper methods
        // ************************************************************************

    }

}
