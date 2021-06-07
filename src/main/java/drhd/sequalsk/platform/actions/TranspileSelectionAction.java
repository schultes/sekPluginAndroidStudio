package drhd.sequalsk.platform.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import drhd.sequalsk.transpiler.context.utils.TextSelection;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all actions that are used to transpile a text selection.
 *
 * Overrides the visibility management of the {@link TranspilerAction} so the action is only visible if any text is
 * selected in the editor.
 */
abstract public class TranspileSelectionAction extends TranspilerAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        if(!e.getPresentation().isVisible()) return;

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if(editor == null) return;
        TextSelection textSelection = TextSelection.fromEditor(editor);

        boolean textIsNull = textSelection.getSelectedText() == null;
        boolean textIsEmpty = true;
        if(!textIsNull) textIsEmpty = textSelection.getSelectedText().equals("");

        e.getPresentation().setVisible(!textIsNull && !textIsEmpty);
    }

}
