package drhd.sequalsk.platform.services.fileservice;

import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.utils.debugging.DebugLogger;
import org.jetbrains.annotations.NotNull;

/**
 * This service is responsible to listen to newly selected files in the code editor. Whenever a new file was selected 
 * this services publishes two messages: 
 * {@link FileSelectionSubscriber#onFileSelected}
 * {@link FileSelectionSubscriber#onFileSelectionDelayExpired}
 */
public class FileSelectionService extends FileService implements FileEditorManagerListener {

    @Override
    protected void onServiceInitialized() {
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);
    }

    @Override
    protected long getDelayTime() {
        return (long) (PluginSettingsHelper.AutoRefreshSettings.fileSelectionDelay() * 1000);
    }

    @Override
    public void onChangeDelayExpired() {
        DebugLogger.info(this, "Dispatching KotlinFileSelectionDelayExpired event...");
        project.getMessageBus()
                .syncPublisher(FileSelectionSubscriber.TOPIC)
                .onFileSelectionDelayExpired(this.currentEditorFile);
    }

    /**
     * Called from the {@link FileEditorManagerListener} whenever a new file was selected in the code editor.
     */
    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        delayTimer.cancel();
        this.currentEditorFile = event.getNewFile();
        printFileSelectedDebugStatement();

        DebugLogger.info(this, "Dispatching FileSelected event ...");
        project.getMessageBus()
                .syncPublisher(FileSelectionSubscriber.TOPIC)
                .onFileSelected(currentEditorFile);
        delayTimer.restart();
    }

    private void printFileSelectedDebugStatement() {
        String debugMessage = "Selected file changed: ";
        if (currentEditorFile == null) DebugLogger.info(this, debugMessage + "No file selected");
        else DebugLogger.info(this, debugMessage + currentEditorFile.getName());
    }
}
