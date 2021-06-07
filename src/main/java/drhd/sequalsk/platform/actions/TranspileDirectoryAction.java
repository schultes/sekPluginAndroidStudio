package drhd.sequalsk.platform.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.transpiler.directory.TranspileDirectoryDialog;
import org.jetbrains.annotations.NotNull;

/**
 * Action to manually transpile a directory.
 */
public class TranspileDirectoryAction extends TranspilerAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new TranspileDirectoryDialog(e.getProject()).show();
    }

}
