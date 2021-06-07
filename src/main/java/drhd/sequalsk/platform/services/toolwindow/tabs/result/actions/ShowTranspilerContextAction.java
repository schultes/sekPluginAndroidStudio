package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.utils.PluginIcons;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.utils.OutputUtils;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Action to show the {@link TranspilerContext} of the latest {@link TranspilerRequest} that is currently displayed in the tool window.
 */
public class ShowTranspilerContextAction extends ToolWindowAction {

    public ShowTranspilerContextAction(ToolWindowModel toolWindowModel) {
        super("Show Transpiler Context", PluginIcons.References, toolWindowModel);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TranspilerContext context = toolWindowModel.getCurrentResult().getTranspilerContext();
        String contextPrintOutput = OutputUtils.printContext(context);

        /* build and show dialog */
        SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Referenced Files");
        dialogBuilder.addEditor(new SimpleEditorBuilder(contextPrintOutput).highlightingOff());
        dialogBuilder.build().show();
    }
}
