package drhd.sequalsk.platform.services.toolwindow.tabs.result;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import drhd.sequalsk.platform.services.toolwindow.ResultChangeListener;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import javax.swing.*;
import java.awt.*;

/**
 * This class creates and manages the toolbar at the top of the {@link ResultTab}, which contains a label to display the
 * translated filename and actions to analyze the {@link TranspilerResult}.
 */
public class ResultTabToolbarManager implements ResultChangeListener {

    /** Panel that contains the ActionToolbar and the label for the filename. */
    private final JPanel toolbarPanel;

    /** The {@link DefaultActionGroup} that contains all actions to analyze the result. */
    private final ResultTabActionGroup resultTabActionGroup;

    /** The label to show the name of the transpiled file */
    private final JLabel labelCurrentFile = new JLabel("");

    /** {@link ResultTabToolbarManager} */
    public ResultTabToolbarManager(ToolWindowModel model) {
        model.addListener(this);
        ActionManager actionManager = ActionManager.getInstance();
        resultTabActionGroup = new ResultTabActionGroup(model);

        ActionToolbar actionToolbar = actionManager.createActionToolbar("result action group", resultTabActionGroup, true);

        toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ((FlowLayout) toolbarPanel.getLayout()).setVgap(0);
        ((FlowLayout) toolbarPanel.getLayout()).setHgap(0);
        toolbarPanel.add(actionToolbar.getComponent());
        toolbarPanel.add(labelCurrentFile);
    }

    public JComponent getComponent() {
        return toolbarPanel;
    }

    @Override
    public void onResultChanged(TranspilerResult result) {
        resultTabActionGroup.updateActionVisibilities();
        labelCurrentFile.setText(result.getMainFile().getOriginalFile().getName());
    }

    @Override
    public void onResultCleared() {
        labelCurrentFile.setText("");
        resultTabActionGroup.removeAll();
    }

}
