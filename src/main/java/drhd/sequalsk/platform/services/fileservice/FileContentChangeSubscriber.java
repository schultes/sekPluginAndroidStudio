package drhd.sequalsk.platform.services.fileservice;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.Topic;

/**
 * Subscriber for content changes of the current file in the editor.
 */
public interface FileContentChangeSubscriber {

    Topic<FileContentChangeSubscriber> TOPIC = Topic.create("FILE_CONTENT_CHANGE_TOPIC", FileContentChangeSubscriber.class);

    /**
     * Called immediately when the content of the selected kotlin file in the editor has changed. The purpose of this
     * method is NOT to trigger transpiler requests. that's what {@link FileSelectionSubscriber#onFileSelectionDelayExpired}}
     * is for.
     *
     * @param virtualFile the virtual file that has changed
     */
    default void onKotlinFileContentChanged(VirtualFile virtualFile) {}

    /**
     * Only called if the automatic refresh setting is active.
     *
     * Whenever the selected kotlin file in the editor has changed, this method gets called as soon as the delay timer
     * has expired.
     *
     * @param virtualFile the virtual file that has changed
     */
    default void onContentChangeDelayExpired(VirtualFile virtualFile) {}

}
