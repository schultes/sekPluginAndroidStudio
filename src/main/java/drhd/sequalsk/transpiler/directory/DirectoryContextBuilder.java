package drhd.sequalsk.transpiler.directory;

import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.context.TranspilerContextGenerator;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.utils.PluginVirtualFileUtils;
import javax.swing.*;

/**
 * Step two to transpile the directory. Generates the transpiler context for the input directory.
 */
public class DirectoryContextBuilder extends DirectoryTranspilerProcessor {

    public DirectoryContextBuilder(TranspileDirectoryDialog transpileDirectoryDialog) {
        super(transpileDirectoryDialog);
    }

    /**
     * The context that is generated during this step. Passed to the next step.
     */
    private TranspilerContext generatedContext;

    @Override
    protected DirectoryTranspilerProcessor getNextProcessor() {
        return new DirectoryTranspiler(dialog, generatedContext);
    }

    @Override
    protected Integer getStepOrder() {
        return 2;
    }

    @Override
    protected String getStepTitle() {
        return "Generate Project Transpiler Context";
    }

    @Override
    protected String getStepDescription() {
        return "Generating context";
    }

    @Override
    protected JLabel getProgressLabel() {
        return dialog.getLabelStepTwoProgress();
    }

    @Override
    protected void executeStep() {
        VirtualFile inputDir = PluginVirtualFileUtils.getVirtualFile(dialog.getInputDirectory());
        if (inputDir == null || !inputDir.isValid() || !inputDir.isWritable() || !inputDir.isDirectory()) {
            finishedFailure("Input directory is invalid");
            return;
        }
        generatedContext = TranspilerContextGenerator.buildDirectoryContext(dialog.getProject(), inputDir);
        finishedSuccess();
    }

}
