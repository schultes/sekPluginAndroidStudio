package drhd.sequalsk.utils.uibuilder;

import com.intellij.diff.*;
import com.intellij.diff.requests.ContentDiffRequest;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import drhd.sequalsk.utils.PluginVirtualFileUtils;

/**
 * Class to provide a simple way to create a diff viewer.
 */
public class SimpleDiffViewerBuilder {

    private final Project project;

    private String titleLeft;
    private String contentLeft;

    private String titleRight;
    private String contentRight;

    /**
     * Creates a new EditorBuilder without content. Default values: line numbers visible, editable, highlighted language
     * is java, ide default font size
     */
    public SimpleDiffViewerBuilder(Project project) {
        this.project = project;
    }

    /**
     * Set content for the left pane of the diff viewer.
     */
    public SimpleDiffViewerBuilder left(String title, String content) {
        this.titleLeft = title;
        this.contentLeft = content;
        return this;
    }

    /**
     * Set content for the left pane of the diff viewer.
     */
    public SimpleDiffViewerBuilder left(VirtualFile virtualFile) {
        this.titleLeft = virtualFile.getName();
        this.contentLeft = PluginVirtualFileUtils.getText(virtualFile).toString();
        return this;
    }

    /**
     * Set content for the right pane of the diff viewer.
     */
    public SimpleDiffViewerBuilder right(String title, String content) {
        this.titleRight = title;
        this.contentRight = content;
        return this;
    }

    /**
     * Set content for the right pane of the diff viewer.
     */
    public SimpleDiffViewerBuilder right(VirtualFile virtualFile) {
        this.titleRight = virtualFile.getName();
        this.contentRight = PluginVirtualFileUtils.getText(virtualFile).toString();
        return this;
    }

    /**
     * Show the diff viewer based on the configured settings
     */
    public void show() {
        DiffContentFactory contentFactory = DiffContentFactory.getInstance();
        DiffRequestFactory requestFactory = DiffRequestFactory.getInstance();

        LightVirtualFile left = new LightVirtualFile(titleLeft, contentLeft);
        LightVirtualFile right = new LightVirtualFile(titleRight, contentRight);

        ContentDiffRequest request = requestFactory.createFromFiles(project, left, right);
        DiffManager.getInstance().showDiff(project, request);
    }

}
