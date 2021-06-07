package drhd.sequalsk.transpiler.directory;

import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.utils.TranspilerRequestBuilder;
import drhd.sequalsk.transpiler.sequalskclient.SequalskClient;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerException;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import javax.swing.*;

/**
 * Step three to transpile the directory. Generates and executes a TranspilerRequest from the previously generated context.
 */
public class DirectoryTranspiler extends DirectoryTranspilerProcessor {

    /** The context that was generated by step two. */
    private final TranspilerContext transpilerContext;

    /** The transpiler result that is generated by this step. Passed to the next step. */
    private TranspilerResult transpilerResult;

    public DirectoryTranspiler(TranspileDirectoryDialog transpileDirectoryDialog, TranspilerContext transpilerContext) {
        super(transpileDirectoryDialog);
        this.transpilerContext = transpilerContext;
    }

    @Override
    protected DirectoryTranspilerProcessor getNextProcessor() {
        return new SwiftFileCreator(dialog, transpilerResult);
    }

    @Override
    protected Integer getStepOrder() {
        return 3;
    }

    @Override
    protected String getStepTitle() {
        return "Transpile Project Files";
    }

    @Override
    protected String getStepDescription() {
        return "transpiling " + transpilerContext.getAllFiles().size() +" files";
    }

    @Override
    protected JLabel getProgressLabel() {
        return dialog.getLabelStepThreeProgress();
    }

    @Override
    protected void executeStep() {
        try {
            SequalskClient transpiler = new SequalskClient(PluginSettingsHelper.asTranspilerConfig());
            TranspilerRequest request = new TranspilerRequestBuilder(transpilerContext).build();
            transpilerResult = transpiler.transpile(request);
            finishedSuccess();
        } catch (TranspilerException e) {
            finishedFailure(e);
        }
    }

}
