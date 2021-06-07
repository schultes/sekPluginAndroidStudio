package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.utils.PluginIcons;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerTwoWayResult;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Action to show the interim result of a two-way request. Shows the content of all files (main file & additional files).
 * Only visible for two-way-results.
 */
public class ShowInterimResultAllAction extends ToolWindowAction {

    public ShowInterimResultAllAction(ToolWindowModel viewModel) {
        super("Show Interim Result (All Files)", PluginIcons.SwiftLangs, viewModel);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TranspilerTwoWayResult transpilerTwoWayResult = (TranspilerTwoWayResult) toolWindowModel.getCurrentResult();
        String transpilerResponse = transpilerTwoWayResult.getInterimResult().getTranspilerResponse();

        SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Interim Result (All Files)");
        dialogBuilder.addEditor(new SimpleEditorBuilder(transpilerResponse));
        dialogBuilder.buildAndShow();
    }

}
