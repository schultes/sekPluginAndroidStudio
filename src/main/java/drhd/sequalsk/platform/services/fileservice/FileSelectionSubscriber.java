package drhd.sequalsk.platform.services.fileservice;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.Topic;

/**
 * Subscriber for selected files in the code editor.
 */
public interface FileSelectionSubscriber {

    Topic<FileSelectionSubscriber> TOPIC = Topic.create("FILE_SELECTION_TOPIC", FileSelectionSubscriber.class);

    /**
     * Called immediately when a file was selected in the editor. The virtual file is null if no file is selected
     * anymore.
     *
     * @param virtualFile the virtual file that is now selected in the editor or null if no file is selected.
     */
    default void onFileSelected(VirtualFile virtualFile) {}

    /**
     * Only called if the auto refresh setting is active for file selections.
     *
     * Whenever the selected file changed, this method gets called as soon as the delay timer expired.
     *
     * @param virtualFile the virtual file that has changed
     */
    default void onFileSelectionDelayExpired(VirtualFile virtualFile) {}
}
