package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.utils.PluginIcons;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Action that opens a dialog with an editor that contains the code of all combined files that were sent to the transpiler.
 */
public class ShowRequestAllAction extends ToolWindowAction {

    public ShowRequestAllAction(ToolWindowModel viewModel) {
        super("Show Request Code (All Files)", PluginIcons.ChangedFilesAllBlue, viewModel);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TranspilerResult currentResult = toolWindowModel.getCurrentResult();
        String requestContent = currentResult.getRequest().getContext().asSingleContent();
        SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Original Code (All Files)");
        dialogBuilder.addEditor(new SimpleEditorBuilder(requestContent));
        dialogBuilder.buildAndShow();
    }

}
