package drhd.sequalsk.platform.services.toolwindow.tabs.settings;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.project.Project;
import drhd.sequalsk.platform.services.transpiler.TranspilerServiceCache;
import drhd.sequalsk.platform.services.settings.project.views.ProjectSettingsPanel;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.utils.uibuilder.SimpleNotificationBuilder;
import drhd.sequalsk.utils.uibuilder.ViewUtils;
import javax.swing.*;
import static javax.swing.BorderFactory.createEmptyBorder;

public class SettingsTabPanel {

    private final Project project;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private ProjectSettingsPanel projectSettingsPanel;
    private JPanel updateSettingsButtonContainer;
    private JButton buttonUpdateSettings;
    private JButton buttonClearCache;

    public SettingsTabPanel(Project project) {
        this.project = project;

        ViewUtils.setTitleSeparator(updateSettingsButtonContainer, "Actions");
        projectSettingsPanel.createPanel();
        applyCurrentSettings();

        buttonUpdateSettings.addActionListener(e -> {
            updateSettings();
            new SimpleNotificationBuilder("Updated SequalsK-Settings").buildAndShow();
        });
        buttonClearCache.addActionListener(e -> {
            project.getService(TranspilerServiceCache.class).clear();
            new SimpleNotificationBuilder("Cleared Transpiler Cache").buildAndShow();
        });

        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(createEmptyBorder());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Refreshes the controls of the project settings panel to match the current plugin settings.
     */
    protected void applyCurrentSettings() {
        // transpiler settings
        projectSettingsPanel.transpilerSettings().setRequestMode(PluginSettingsHelper.TranspilerSettings.requestMode());
        projectSettingsPanel.transpilerSettings().setFileContextMode(PluginSettingsHelper.TranspilerSettings.fileContextMode());
        projectSettingsPanel.transpilerSettings().setCacheFiles(PluginSettingsHelper.TranspilerSettings.cacheFiles());

        // auto refresh settings
        projectSettingsPanel.autoRefreshSettings().setEnabled(PluginSettingsHelper.AutoRefreshSettings.enabled());
        projectSettingsPanel.autoRefreshSettings().setOnFileSelectionEnabled(PluginSettingsHelper.AutoRefreshSettings.enabledOnFileSelection());
        projectSettingsPanel.autoRefreshSettings().setOnContentChangeEnabled(PluginSettingsHelper.AutoRefreshSettings.enabledOnFileContentChange());
        projectSettingsPanel.autoRefreshSettings().setDelayTimeFileSelection(PluginSettingsHelper.AutoRefreshSettings.fileSelectionDelay());
        projectSettingsPanel.autoRefreshSettings().setDelayTimeFileContentChange(PluginSettingsHelper.AutoRefreshSettings.fileContentChangeDelay());

        // user interface settings
        projectSettingsPanel.userInterfaceSettings().setShowActionBar(PluginSettingsHelper.UserInterfaceSettings.showActionBar());
        projectSettingsPanel.userInterfaceSettings().setShowEditorPopupActions(PluginSettingsHelper.UserInterfaceSettings.showEditorPopupActions());
        projectSettingsPanel.userInterfaceSettings().setShowLineMarkerInfos(PluginSettingsHelper.UserInterfaceSettings.showLineMarkerInfos());
        projectSettingsPanel.userInterfaceSettings().setShowInspections(PluginSettingsHelper.UserInterfaceSettings.showInspections());

        // directories
        projectSettingsPanel.projectTranspilerSettings().setOutputDirectory(PluginSettingsHelper.DirectoryTranspilerSettings.outputDirectory());
        projectSettingsPanel.projectTranspilerSettings().setProjectRootDirectory(PluginSettingsHelper.DirectoryTranspilerSettings.projectRootDirectory());
    }

    /**
     * Reads all controls of the panel to update the plugin settings.
     */
    protected void updateSettings() {
        // transpiler settings
        PluginSettingsHelper.TranspilerSettings.requestMode(projectSettingsPanel.transpilerSettings().getRequestMode());
        PluginSettingsHelper.TranspilerSettings.fileContextMode(projectSettingsPanel.transpilerSettings().getFileContextMode());
        PluginSettingsHelper.TranspilerSettings.cacheFiles(projectSettingsPanel.transpilerSettings().getCacheFiles());

        // auto refresh settings
        PluginSettingsHelper.AutoRefreshSettings.enabled(projectSettingsPanel.autoRefreshSettings().enabled());
        PluginSettingsHelper.AutoRefreshSettings.enabledOnFileSelection(projectSettingsPanel.autoRefreshSettings().onFileSelectionEnabled());
        PluginSettingsHelper.AutoRefreshSettings.enabledOnFileContentChange(projectSettingsPanel.autoRefreshSettings().onContentChangeEnabled());
        PluginSettingsHelper.AutoRefreshSettings.fileSelectionDelay(projectSettingsPanel.autoRefreshSettings().getDelayTimeFileSelection());
        PluginSettingsHelper.AutoRefreshSettings.fileContentChangeDelay(projectSettingsPanel.autoRefreshSettings().getDelayTimeFileContentChange());

        // user interface settings
        PluginSettingsHelper.UserInterfaceSettings.showActionBar(projectSettingsPanel.userInterfaceSettings().showActionBar());
        PluginSettingsHelper.UserInterfaceSettings.showEditorPopupActions(projectSettingsPanel.userInterfaceSettings().showEditorPopupMenuActions());
        PluginSettingsHelper.UserInterfaceSettings.showLineMarkerInfos(projectSettingsPanel.userInterfaceSettings().showLineMarkerInfos());
        PluginSettingsHelper.UserInterfaceSettings.showInspections(projectSettingsPanel.userInterfaceSettings().showInspections());

        // directories
        PluginSettingsHelper.DirectoryTranspilerSettings.outputDirectory(projectSettingsPanel.projectTranspilerSettings().getOutputDirectory());
        PluginSettingsHelper.DirectoryTranspilerSettings.projectRootDirectory(projectSettingsPanel.projectTranspilerSettings().getProjectRootDirectory());

        DaemonCodeAnalyzer.getInstance(project).restart();
        project.getService(TranspilerServiceCache.class).clear();
    }

}
