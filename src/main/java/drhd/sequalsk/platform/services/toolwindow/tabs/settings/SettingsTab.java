package drhd.sequalsk.platform.services.toolwindow.tabs.settings;

import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.platform.services.toolwindow.tabs.ToolWindowTab;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import javax.swing.*;

/**
 * The tab of the ToolWindow that provides access to the project settings.
 */
public class SettingsTab extends ToolWindowTab {

    @Override
    protected String getTitle() {
        return "Settings";
    }

    @Override
    protected JComponent createMainLayout() {
        return new SettingsTabPanel(model.getProject()).getMainPanel();
    }

    public SettingsTab(ToolWindowModel model) {
        super(model);
    }

    @Override
    public void onResultChanged(TranspilerResult result) {
        // nothing to update here
    }

    @Override
    public void onResultCleared() {
        // nothing to update here
    }
}
