package drhd.sequalsk.transpiler.directory;

import org.apache.commons.io.FileUtils;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Implements the first step to transpile a directory. Validates the specified output directory and removes all content in that directory.
 */
public class OutputDirectoryCleaner extends DirectoryTranspilerProcessor {

    public OutputDirectoryCleaner(TranspileDirectoryDialog transpileDirectoryDialog) {
        super(transpileDirectoryDialog);
    }

    @Override
    protected DirectoryTranspilerProcessor getNextProcessor() {
        return new DirectoryContextBuilder(dialog);
    }

    @Override
    protected Integer getStepOrder() {
        return 1;
    }

    @Override
    protected String getStepTitle() {
        return "Clear Output Directory";
    }

    @Override
    protected String getStepDescription() {
        return "Deleting content of output directory";
    }

    @Override
    protected JLabel getProgressLabel() {
        return dialog.getLabelStepOneProgress();
    }

    @Override
    protected void executeStep() {

        String outputDir = dialog.getOutputDirectory();
        if(outputDir == null || outputDir.equals("")) {
            finishedFailure("No output directory specified");
            return;
        }

        boolean directoryExists = Files.exists(Paths.get(dialog.getOutputDirectory()));
        if (!directoryExists) {
            finishedSuccess("done (directory does not exist)!");
            return;
        }

        boolean isDirectory = Files.isDirectory(Paths.get(dialog.getOutputDirectory()));

        if (!isDirectory) {
            finishedFailure("could not clean directory - path is not a directory!");
            return;
        }

        try {
            FileUtils.cleanDirectory(asFile(dialog.getOutputDirectory()));
            finishedSuccess();
        } catch (IllegalArgumentException | IOException e) {
            finishedFailure(e);
        }
    }

}
