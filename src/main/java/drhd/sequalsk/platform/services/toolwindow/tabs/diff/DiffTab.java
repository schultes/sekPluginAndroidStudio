package drhd.sequalsk.platform.services.toolwindow.tabs.diff;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.DiffRequestFactory;
import com.intellij.diff.DiffRequestPanel;
import com.intellij.diff.requests.ContentDiffRequest;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.testFramework.LightVirtualFile;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowService;
import drhd.sequalsk.platform.services.toolwindow.tabs.ToolWindowTab;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import javax.swing.*;

/**
 * Tab to integrate the diff-view into the tool window.
 */
public class DiffTab extends ToolWindowTab {

    /**
     * The panel that contains the diff view. Gets updated everytime when a new transpiler result is added to the {@link ToolWindowService})
     */
    private DiffRequestPanel diffRequestPanel;

    /** {@link DiffTab} */
    public DiffTab(ToolWindowModel viewModel) {
        super(viewModel);
    }

    @Override
    protected String getTitle() {
        return "Compare";
    }

    @Override
    protected JComponent createMainLayout() {
        DialogBuilder dialogBuilder = new DialogBuilder(model.getProject());
        diffRequestPanel = DiffManager.getInstance().createRequestPanel(model.getProject(), dialogBuilder, dialogBuilder.getWindow());
        return diffRequestPanel.getComponent();
    }

    @Override
    public void onResultChanged(TranspilerResult result) {
        DiffContentFactory contentFactory = DiffContentFactory.getInstance();
        DiffRequestFactory requestFactory = DiffRequestFactory.getInstance();

        TranspilerRequestFile originalMainFile = result.getTranspilerContext().getMainFile();
        String originalText = originalMainFile.getContent();
        String transpiledText = result.getMainFile().getTranspiledContent();

        LightVirtualFile left = new LightVirtualFile("Left.kt", originalText);
        LightVirtualFile right = new LightVirtualFile("Right.kt", transpiledText);

        ContentDiffRequest request = requestFactory.createFromFiles(model.getProject(), left, right);
        diffRequestPanel.setRequest(request);
    }

    @Override
    public void onResultCleared() {
        diffRequestPanel.setRequest(null);
    }

}
