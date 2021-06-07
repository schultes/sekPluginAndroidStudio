package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.utils.uibuilder.SimpleDiffViewerBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Action to compare the original file with the transpiled content in a diff-view. Only visible for two-way-results.
 */
public class ShowDiffAction extends ToolWindowAction {

    public ShowDiffAction(ToolWindowModel viewModel) {
        super("Compare original content with transpiled content", AllIcons.Actions.Diff, viewModel);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TranspilerRequestFile originalMainFile = toolWindowModel.getCurrentResult().getTranspilerContext().getMainFile();
        String originalText = originalMainFile.getContent();
        String transpiledText = toolWindowModel.getCurrentResult().getMainFile().getTranspiledContent();

        new SimpleDiffViewerBuilder(e.getProject())
                .left(originalMainFile.getPath(), originalText)
                .right(originalMainFile.getName() + " (transpiled)", transpiledText)
                .show();
    }

}
