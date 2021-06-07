package drhd.sequalsk.platform.services.toolwindow.tabs.result;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.impl.EditorImpl;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.platform.services.toolwindow.tabs.ToolWindowTab;
import drhd.sequalsk.platform.services.toolwindow.tabs.result.actions.ToolWindowAction;
import drhd.sequalsk.transpiler.validation.view.TranspilerResultInspectionList;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import javax.swing.*;
import java.awt.*;

/**
 * The tab of the ToolWindow that contains ... <ul>
 *      <li>... an editor for the latest transpiler result</li>
 *      <li>... a panel to display warnings about the latest transpiler result</li>
 *      <li>... an action toolbar with {@link ToolWindowAction}s</li>
 * </ul>
 */
public class ResultTab extends ToolWindowTab {

    /** The editor that contains the translated code. */
    private EditorImpl editor;

    /** The manager that manages the visibility of the actions in the toolbar depending on used request type. */
    private ResultTabToolbarManager toolbarManager;

    /** The list that displays the results of the automatic analysis of the transpiler result.  */
    private TranspilerResultInspectionList resultInspectionList;

    public ResultTab(ToolWindowModel viewModel) {
        super(viewModel);
    }

    /** Method to override the content of the code editor. */
    public void setCustomEditorContent(String content) {
        Runnable runnable = () -> ApplicationManager.getApplication().runWriteAction(() -> {
            editor.getDocument().setText(content);
        });
        ApplicationManager.getApplication().invokeLater(runnable);
    }

    @Override
    protected String getTitle() {
        return "Result";
    }

    @Override
    protected JComponent createMainLayout() {
        toolbarManager = new ResultTabToolbarManager(model);
        resultInspectionList = new TranspilerResultInspectionList();

        editor = new SimpleEditorBuilder("// hello world!").readOnly().highlightKotlin().build();

        JPanel mainLayout = new JPanel(new BorderLayout());
        mainLayout.add(toolbarManager.getComponent(), BorderLayout.NORTH);
        mainLayout.add(editor.getComponent(), BorderLayout.CENTER);

        mainLayout.add(resultInspectionList, BorderLayout.SOUTH);

        return mainLayout;
    }

    @Override
    public void onResultChanged(TranspilerResult result) {
        setCustomEditorContent(result.getMainFile().getTranspiledContent());
        resultInspectionList.updateWarningItems(result);
    }

    @Override
    public void onResultCleared() {
        resultInspectionList.removeAll();
        setCustomEditorContent("");
    }

    /**
     * Releases the editor to avoid memory leaks.
     */
    public void disposeEditor() {
        EditorFactory.getInstance().releaseEditor(editor);
    }

}
