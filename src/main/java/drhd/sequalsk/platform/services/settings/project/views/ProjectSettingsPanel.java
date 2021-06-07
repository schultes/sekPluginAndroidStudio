package drhd.sequalsk.platform.services.settings.project.views;

import drhd.sequalsk.platform.services.settings.project.views.embedded.AutoRefreshSettingsPanel;
import drhd.sequalsk.platform.services.settings.project.views.embedded.UserInterfaceSettingsPanel;
import drhd.sequalsk.platform.services.settings.project.views.embedded.ProjectTranspilerSettingsPanel;
import drhd.sequalsk.utils.uibuilder.ViewUtils;
import drhd.sequalsk.platform.services.settings.project.views.embedded.TranspilerSettingsPanel;
import javax.swing.*;

/**
 * This is the form that contains all views to configure the plugin settings on project level.
 * This panel is used in the settings (preferences) window of the IDE as well as in the plugin tool window settings tab.
 */
public class ProjectSettingsPanel {

    private JPanel mainPanel;

    private TranspilerSettingsPanel transpilerSettingsPanel;
    private AutoRefreshSettingsPanel autoRefreshSettingsPanel;
    private ProjectTranspilerSettingsPanel projectTranspilerSettingsPanel;
    private UserInterfaceSettingsPanel userInterfaceSettingsPanel;
    private JPanel rootPanel;

    public JPanel createPanel() {
        autoRefreshSettingsPanel.initialize();
        transpilerSettingsPanel.initialize();
        projectTranspilerSettingsPanel.initialize();

        ViewUtils.setTitleSeparator(transpilerSettingsPanel.getMainPanel(), "Transpiler");
        ViewUtils.setTitleSeparator(autoRefreshSettingsPanel.getMainPanel(), "Automatic Transpiler");
        ViewUtils.setTitleSeparator(projectTranspilerSettingsPanel.getMainPanel(), "Project Transpiler");
        ViewUtils.setTitleSeparator(userInterfaceSettingsPanel.getMainPanel(), "User Interface");

        return mainPanel;
    }

    public TranspilerSettingsPanel transpilerSettings() {
        return transpilerSettingsPanel;
    }

    public AutoRefreshSettingsPanel autoRefreshSettings() {
        return autoRefreshSettingsPanel;
    }

    public ProjectTranspilerSettingsPanel projectTranspilerSettings() {
        return projectTranspilerSettingsPanel;
    }

    public UserInterfaceSettingsPanel userInterfaceSettings() {
        return userInterfaceSettingsPanel;
    }
}
