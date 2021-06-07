package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.views.TranspilerRequestPanel;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Action to show the details of the latest {@link TranspilerRequest} in a {@link TranspilerRequestPanel}.
 */
public class ShowTranspilerRequestAction extends ToolWindowAction {

    public ShowTranspilerRequestAction(ToolWindowModel viewModel) {
        super("Show Transpiler Request Details", AllIcons.General.ShowInfos, viewModel);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TranspilerResult currentResult = toolWindowModel.getCurrentResult();
        SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Request Details");
        dialogBuilder.addComponent(new TranspilerRequestPanel(currentResult.getRequest()).getMainPanel());
        dialogBuilder.buildAndShow();
    }

}
