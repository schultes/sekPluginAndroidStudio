package drhd.sequalsk.platform.services.settings.project;

import com.intellij.openapi.options.Configurable;
import drhd.sequalsk.platform.services.settings.project.views.ProjectSettingsPanel;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

/**
 * Provides controller functionality for project settings.
 */
public class ProjectSettingsConfigurable implements Configurable {

    private ProjectSettingsPanel settingsPanel;

    @Override
    public String getDisplayName() {
        return "SequalsK Settings (Project)";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsPanel = new ProjectSettingsPanel();
        return settingsPanel.createPanel();
    }

    @Override
    public boolean isModified() {
        ProjectSettingsService settingsState = ProjectSettingsService.getInstance();

        boolean modified = false;

        // transpiler settings
        modified |= !settingsPanel.transpilerSettings().getRequestMode().equals(settingsState.getRequestMode());
        modified |= !settingsPanel.transpilerSettings().getFileContextMode().equals(settingsState.getFileContextMode());
        modified |= !settingsPanel.transpilerSettings().getCacheFiles().equals(settingsState.getCacheFiles());


        // automatic transpiler settings
        modified |= !settingsPanel.autoRefreshSettings().enabled().equals(settingsState.getAutoRefreshEnabled());
        modified |= !settingsPanel.autoRefreshSettings().onFileSelectionEnabled().equals(settingsState.getAutoRefreshOnFileSelection());
        modified |= !settingsPanel.autoRefreshSettings().onContentChangeEnabled().equals(settingsState.getAutoRefreshOnFileContentChange());
        modified |= !settingsPanel.autoRefreshSettings().getDelayTimeFileSelection().equals(settingsState.getAutoRefreshDelayFileSelection());
        modified |= !settingsPanel.autoRefreshSettings().getDelayTimeFileContentChange().equals(settingsState.getAutoRefreshDelayFileContentChange());

        // user interface settings
        modified |= !settingsPanel.userInterfaceSettings().showActionBar().equals(settingsState.getShowFloatingActionBar());
        modified |= !settingsPanel.userInterfaceSettings().showEditorPopupMenuActions().equals(settingsState.getShowEditorPopupActions());
        modified |= !settingsPanel.userInterfaceSettings().showLineMarkerInfos().equals(settingsState.getShowLineMarkerInfos());
        modified |= !settingsPanel.userInterfaceSettings().showInspections().equals(settingsState.getShowInspections());

        // directories
        modified |= !settingsPanel.projectTranspilerSettings().getOutputDirectory().equals(settingsState.getOutputDirectory());
        modified |= !settingsPanel.projectTranspilerSettings().getProjectRootDirectory().equals(settingsState.getProjectRootDirectory());

        return modified;
    }

    @Override
    public void apply() {
        ProjectSettingsService settings = ProjectSettingsService.getInstance();

        // transpiler settings
        settings.setRequestMode(settingsPanel.transpilerSettings().getRequestMode());
        settings.setFileContextMode(settingsPanel.transpilerSettings().getFileContextMode());
        settings.setCacheFiles(settingsPanel.transpilerSettings().getCacheFiles());

        // automatic transpiler settings
        settings.setAutoRefreshEnabled(settingsPanel.autoRefreshSettings().enabled());
        settings.setAutoRefreshOnFileSelection(settingsPanel.autoRefreshSettings().onFileSelectionEnabled());
        settings.setAutoRefreshOnFileContentChange(settingsPanel.autoRefreshSettings().onContentChangeEnabled());
        settings.setAutoRefreshDelayFileSelection(settingsPanel.autoRefreshSettings().getDelayTimeFileSelection());
        settings.setAutoRefreshDelayFileContentChange(settingsPanel.autoRefreshSettings().getDelayTimeFileContentChange());

        // user interface settings
        settings.setShowFloatingActionBar(settingsPanel.userInterfaceSettings().showActionBar());
        settings.setShowEditorPopupActions(settingsPanel.userInterfaceSettings().showEditorPopupMenuActions());
        settings.setShowLineMarkerInfos(settingsPanel.userInterfaceSettings().showLineMarkerInfos());
        settings.setShowInspections(settingsPanel.userInterfaceSettings().showInspections());

        // directories
        settings.setOutputDirectory(settingsPanel.projectTranspilerSettings().getOutputDirectory());
        settings.setProjectRootDirectory(settingsPanel.projectTranspilerSettings().getProjectRootDirectory());
    }

    @Override
    public void reset() {
        ProjectSettingsService settings = ProjectSettingsService.getInstance();

        // transpiler settings
        settingsPanel.transpilerSettings().setRequestMode(settings.getRequestMode());
        settingsPanel.transpilerSettings().setFileContextMode(settings.getFileContextMode());
        settingsPanel.transpilerSettings().setCacheFiles(settings.getCacheFiles());

        // automatic transpiler settings
        settingsPanel.autoRefreshSettings().setEnabled(settings.getAutoRefreshEnabled());
        settingsPanel.autoRefreshSettings().setOnFileSelectionEnabled(settings.getAutoRefreshOnFileSelection());
        settingsPanel.autoRefreshSettings().setOnContentChangeEnabled(settings.getAutoRefreshOnFileContentChange());
        settingsPanel.autoRefreshSettings().setDelayTimeFileSelection(settings.getAutoRefreshDelayFileSelection());
        settingsPanel.autoRefreshSettings().setDelayTimeFileContentChange(settings.getAutoRefreshDelayFileContentChange());

        // user interface settings
        settingsPanel.userInterfaceSettings().setShowActionBar(settings.getShowFloatingActionBar());
        settingsPanel.userInterfaceSettings().setShowEditorPopupActions(settings.getShowEditorPopupActions());
        settingsPanel.userInterfaceSettings().setShowInspections(settings.getShowInspections());
        settingsPanel.userInterfaceSettings().setShowLineMarkerInfos(settings.getShowLineMarkerInfos());

        // directories
        settingsPanel.projectTranspilerSettings().setOutputDirectory(settings.getOutputDirectory());
        settingsPanel.projectTranspilerSettings().setProjectRootDirectory(settings.getProjectRootDirectory());
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }
}
