package drhd.sequalsk.platform.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import drhd.sequalsk.transpiler.utils.TranspilerRequestBuilder;
import drhd.sequalsk.transpiler.context.TranspilerContextGenerator;
import drhd.sequalsk.transpiler.context.utils.TextSelection;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import org.jetbrains.annotations.NotNull;

/**
 * Action to transpile the current selection of the editor (two-way).
 */
public class TranspileSelectionTwoWayAction extends TranspileSelectionAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (editor == null) return;

        TextSelection textSelection = TextSelection.fromEditor(editor);
        TranspilerContext transpilerContext = TranspilerContextGenerator.buildSelectionContext(e.getProject(), textSelection);
        TranspilerRequest request = new TranspilerRequestBuilder(transpilerContext).twoWay().build();

        transpile(e.getProject(), request);
    }

}
