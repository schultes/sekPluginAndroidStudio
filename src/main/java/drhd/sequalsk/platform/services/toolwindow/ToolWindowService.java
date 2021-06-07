package drhd.sequalsk.platform.services.toolwindow;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.ContentManager;
import drhd.sequalsk.platform.services.feedback.TranspilerFeedbackSubscriber;
import drhd.sequalsk.platform.services.PluginBaseService;
import drhd.sequalsk.platform.services.fileservice.FileSelectionSubscriber;
import drhd.sequalsk.platform.services.toolwindow.tabs.diff.DiffTab;
import drhd.sequalsk.platform.services.toolwindow.tabs.settings.SettingsTab;
import drhd.sequalsk.platform.services.toolwindow.tabs.result.ResultTab;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import org.jetbrains.annotations.NotNull;

/**
 * Controller of the tool window and parent of all tabs/components that are part of it.
 */
public class ToolWindowService extends PluginBaseService implements Disposable, FileSelectionSubscriber {

    private ResultTab resultTab;
    private DiffTab diffTab;
    private SettingsTab settingsTab;
    private ToolWindowModel model;

    @Override
    protected void initializeService() {
        this.model = new ToolWindowModel(project);
        resultTab = new ResultTab(model);
        diffTab = new DiffTab(model);
        settingsTab = new SettingsTab(model);
        project.getMessageBus().connect().subscribe(FileSelectionSubscriber.TOPIC, this);
    }

    /**
     * Sets the tool window that is managed by this service.
     */
    public void setToolWindow(@NotNull ToolWindow toolWindow) {
        /* add tabs to layout */
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(resultTab.getTabContent());
        contentManager.addContent(diffTab.getTabContent());
        contentManager.addContent(settingsTab.getTabContent());
    }

    @Override
    public void dispose() {
        resultTab.disposeEditor();
    }

    public void updateContent(TranspilerResult result) {
        if(result == null) model.clearResult();
        else model.setCurrentResult(result);

        project.getMessageBus().syncPublisher(TranspilerFeedbackSubscriber.TOPIC).onResultReceived(result);
    }

    /**
     * Removes the current result from the tool window
     */
    public void clear() {
        model.clearResult();
    }

    /**
     * Set a custom text to the editor of the {@link ResultTab}
     */
    public void setCustomEditorContent(String content) {
        resultTab.setCustomEditorContent(content);
    }

    @Override
    public void onFileSelected(VirtualFile virtualFile) {
        if(virtualFile == null) {
            clear();
        }
        else if(model.getCurrentResult() != null) {
            String selectedFilePath = virtualFile.getPath();
            String shownFilePath = model.getCurrentResult().getMainFile().getOriginalFile().getPath();
            if(!selectedFilePath.equals(shownFilePath)) clear();
        }
    }
}
