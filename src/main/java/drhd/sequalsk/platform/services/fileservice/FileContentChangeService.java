package drhd.sequalsk.platform.services.fileservice;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.utils.debugging.DebugLogger;
import drhd.sequalsk.utils.PluginVirtualFileUtils;
import org.jetbrains.annotations.NotNull;

/**
 * This service installs a {@link DocumentListener} on the currently selected file of the editor, if the file is a
 * kotlin file.
 * <p>
 * The document listener publishes two messages in case of a content change (adding and/or removing content):
 * <ul>
 *  <li>A KotlinFileChangedEvent is published immediately ({@link FileContentChangeSubscriber#onKotlinFileContentChanged}) </li>
 *  <li>A timer is started upon its finish a KotlinFileChangeDelayExpired event is dispatched ({@link FileContentChangeService#onFileSelectionDelayExpired}) </li>
 * </ul>
 */
public class FileContentChangeService extends FileService implements FileSelectionSubscriber {

    /**
     * The document that is active in the editor or null if no (kotlin) file is selected.
     */
    private Document currentDocument;

    /**
     * The listener of the document that reacts to content changes.
     */
    private DocumentListener currentDocumentListener;

    @Override
    protected void onServiceInitialized() {
        project.getMessageBus().connect().subscribe(FileSelectionSubscriber.TOPIC, this);
    }

    @Override
    protected long getDelayTime() {
        return (long) (PluginSettingsHelper.AutoRefreshSettings.fileContentChangeDelay() * 1000);
    }

    @Override
    public void onChangeDelayExpired() {
        DebugLogger.info(this, "Dispatching KotlinFileChangeDelayExpired event...");
        project.getMessageBus().syncPublisher(FileContentChangeSubscriber.TOPIC).onContentChangeDelayExpired(currentEditorFile);
    }

    //
    // File Selection
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFileSelected(VirtualFile virtualFile) {
        if(PluginVirtualFileUtils.isKotlinFile(virtualFile))
            onKotlinFileSelected(virtualFile);
        else
            onOtherFileTypeSelected(virtualFile);
    }

    /** Called whenever a kotlin file has been selected in the editor. */
    private void onKotlinFileSelected(VirtualFile virtualFile) {
        DebugLogger.info(this, virtualFile.getName() + " is now selected, updating DocumentListener");
        removeDocumentListener();
        this.currentEditorFile = virtualFile;
        installDocumentListener();
    }

    /** Called whenever a non-kotlin file has been selected in the editor (includes the case of no file beeing selected anymore) */
    private void onOtherFileTypeSelected(VirtualFile virtualFile) {
        if (virtualFile != null)
            DebugLogger.info(this, virtualFile.getName() + " is now selected, removing DocumentListener");
        else
            DebugLogger.info(this, "No file selected, removing Document Listener");

        removeDocumentListener();
        delayTimer.cancel();
        this.currentEditorFile = null;
    }

    //
    // Document listening
    //

    /**
     * Called from the {@link #currentDocumentListener} whenever the content of the listened document has changed.
     */
    private void onDocumentChanged() {
        DebugLogger.info(this, currentEditorFile.getName() + " changed, dispatching KotlinFileChanged event...");
        project.getMessageBus().syncPublisher(FileContentChangeSubscriber.TOPIC).onKotlinFileContentChanged(currentEditorFile);
        delayTimer.restart();
    }

    /**
     * Creates a new document listener for the current document in the editor.
     */
    private void installDocumentListener() {
        currentDocumentListener = this.createDocumentListener();
        currentDocument = PluginVirtualFileUtils.getDocument(this.currentEditorFile);
        currentDocument.addDocumentListener(currentDocumentListener);
        DebugLogger.info(this, "Added document listener to file " + this.currentEditorFile.getName());
    }

    /**
     * Removes the document listener from the current kotlin document.
     */
    private void removeDocumentListener() {
        if (currentEditorFile != null) {
            currentDocument.removeDocumentListener(currentDocumentListener);
            this.currentDocumentListener = null;
            DebugLogger.info(this, "Removed document listener from " + this.currentEditorFile.getName());
        }
    }

    private DocumentListener createDocumentListener() {
        return new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                onDocumentChanged();
            }
        };
    }
}