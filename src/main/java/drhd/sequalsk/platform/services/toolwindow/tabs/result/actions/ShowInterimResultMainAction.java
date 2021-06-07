package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.utils.PluginIcons;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerTwoWayResult;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Action to show the interim result of a two-way transpiler request. Shows the content of the main file only.
 * Only visible for two-way-results.
 */
public class ShowInterimResultMainAction extends ToolWindowAction {

    public ShowInterimResultMainAction(ToolWindowModel viewModel) {
        super("Show Interim Result (Main File)", PluginIcons.SwiftLang, viewModel);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TranspilerTwoWayResult transpilerTwoWayResult = (TranspilerTwoWayResult) toolWindowModel.getCurrentResult();
        String transpilerResponse = transpilerTwoWayResult.getInterimResult().getMainFile().getTranspiledContent();
        SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Interim Result (Main File)");
        dialogBuilder.addEditor(new SimpleEditorBuilder(transpilerResponse));
        dialogBuilder.buildAndShow();
    }

}
