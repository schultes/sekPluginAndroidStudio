package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.utils.PluginIcons;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Action that opens a dialog with an editor that contains the transpiled code of all combined files.
 */
public class ShowResultAllAction extends ToolWindowAction {

    public ShowResultAllAction(ToolWindowModel viewModel) {
        super("Show Transpiled Code (All Files)", PluginIcons.ChangedFilesAllGreen, viewModel);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TranspilerResult currentResult = toolWindowModel.getCurrentResult();
        String transpilerResponse = currentResult.getTranspilerResponse();

        SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Transpiled Code (All Files)");
        dialogBuilder.addEditor(new SimpleEditorBuilder(transpilerResponse));
        dialogBuilder.buildAndShow();
    }

}
