package drhd.sequalsk.platform.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.utils.TranspilerRequestBuilder;
import drhd.sequalsk.transpiler.context.TranspilerContextGenerator;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import org.jetbrains.annotations.NotNull;

/**
 * Action to transpile the current file of the editor (two-way).
 */
public class TranspileFileTwoWayAction extends TranspilerAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        TranspilerContext transpilerContext = TranspilerContextGenerator.buildFileContext(e.getProject(), virtualFile);
        TranspilerRequest request = new TranspilerRequestBuilder(transpilerContext).twoWay().build();

        transpile(e.getProject(), request);
    }

}