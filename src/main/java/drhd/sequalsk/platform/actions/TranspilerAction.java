package drhd.sequalsk.platform.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowService;
import drhd.sequalsk.transpiler.sequalskclient.SequalskClient;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.views.TranspilerExceptionHelper;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for all actions used to transpile code manually.
 * Manages the visibility of the action in the editor's floating action bar and editor context menu.
 * Contains methods for using the transpiler.
 */
abstract public class TranspilerAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);

        boolean isContextToolbarPlace = e.getPlace().equals("ContextToolbar");
        boolean showInContextToolbar = PluginSettingsHelper.UserInterfaceSettings.showActionBar();
        boolean isEditorPopupMenuPlace = e.getPlace().equals("EditorPopup");
        boolean showInEditorPopupMenu = PluginSettingsHelper.UserInterfaceSettings.showEditorPopupActions();

        if (isContextToolbarPlace) {
            e.getPresentation().setVisible(showInContextToolbar);
        }

        if (isEditorPopupMenuPlace) {
            e.getPresentation().setVisible(showInEditorPopupMenu);
        }
    }


    /**
     * Creates a new {@link SequalskClient}.
     */
    protected SequalskClient getTranspiler() {
        return new SequalskClient(PluginSettingsHelper.asTranspilerConfig());
    }

    /**
     * Transpiles the given request and sends the result to the tool window.
     */
    protected void transpile(Project project, TranspilerRequest request) {
        CompletableFuture<TranspilerResult> future = getTranspiler().transpileAsync(request);

        future.thenAccept(transpilerResult -> {
            ToolWindowService toolWindowService = project.getService(ToolWindowService.class);
            toolWindowService.updateContent(transpilerResult);
        });

        future.exceptionally(throwable -> {
            TranspilerExceptionHelper.showNotification(request, throwable);
            return null;
        });
    }

}
