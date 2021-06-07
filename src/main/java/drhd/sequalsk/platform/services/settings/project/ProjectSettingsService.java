package drhd.sequalsk.platform.services.settings.project;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import drhd.sequalsk.platform.services.settings.PluginSettingsDefaults;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * From Jetbrains Documentation: Supports storing the settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent settings are stored.
 */
@State(
        name = "settings.sequalsk.ProjectSettingsState",
        storages = {@Storage("SequalskProjectSettings.xml")}
)
public class ProjectSettingsService implements PersistentStateComponent<ProjectSettingsService> {

    // transpiler settings
    private TranspilerRequestMode requestMode = PluginSettingsDefaults.requestMode;
    private FileContextMode fileContextMode = PluginSettingsDefaults.fileContextMode;
    private Boolean cacheFiles = PluginSettingsDefaults.cacheFiles;

    // auto refresh settings
    private Boolean autoRefreshEnabled = PluginSettingsDefaults.autoRefreshEnabled;
    private Boolean autoRefreshOnFileSelection = PluginSettingsDefaults.autoRefreshOnFileSelection;
    private Boolean autoRefreshOnFileContentChange = PluginSettingsDefaults.autoRefreshOnFileContentChange;
    private Double autoRefreshDelayFileSelection = PluginSettingsDefaults.autoRefreshDelayFileSelection;
    private Double autoRefreshDelayFileContentChange = PluginSettingsDefaults.autoRefreshDelayFileContentChange;

    // directories
    private String outputDirectory = PluginSettingsDefaults.outputDirectory;
    private String projectRootDirectory = PluginSettingsDefaults.projectRootDirectory;

    // user interface options
    private Boolean showFloatingActionBar = PluginSettingsDefaults.showFloatingActionBar;
    private Boolean showEditorPopupActions = PluginSettingsDefaults.showEditorPopupActions;
    private Boolean showInspections = PluginSettingsDefaults.showInspections;
    private Boolean showLineMarkerInfos = PluginSettingsDefaults.showLineMarkerInfos;

    public static ProjectSettingsService getInstance() {
        return ServiceManager.getService(ProjectSettingsService.class);
    }

    @Nullable
    @Override
    public ProjectSettingsService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectSettingsService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public TranspilerRequestMode getRequestMode() {
        return requestMode;
    }

    public void setRequestMode(TranspilerRequestMode requestMode) {
        this.requestMode = requestMode;
    }

    public Boolean getAutoRefreshEnabled() {
        return autoRefreshEnabled;
    }

    public void setAutoRefreshEnabled(Boolean autoRefreshEnabled) {
        this.autoRefreshEnabled = autoRefreshEnabled;
    }

    public Boolean getShowFloatingActionBar() {
        return showFloatingActionBar;
    }

    public void setShowFloatingActionBar(Boolean showFloatingActionBar) {
        this.showFloatingActionBar = showFloatingActionBar;
    }

    public Boolean getShowEditorPopupActions() {
        return showEditorPopupActions;
    }

    public void setShowEditorPopupActions(Boolean showEditorPopupActions) {
        this.showEditorPopupActions = showEditorPopupActions;
    }

    public Boolean getAutoRefreshOnFileSelection() {
        return autoRefreshOnFileSelection;
    }

    public void setAutoRefreshOnFileSelection(Boolean autoRefreshOnFileSelection) {
        this.autoRefreshOnFileSelection = autoRefreshOnFileSelection;
    }

    public Boolean getAutoRefreshOnFileContentChange() {
        return autoRefreshOnFileContentChange;
    }

    public void setAutoRefreshOnFileContentChange(Boolean autoRefreshOnFileContentChange) {
        this.autoRefreshOnFileContentChange = autoRefreshOnFileContentChange;
    }

    public Double getAutoRefreshDelayFileSelection() {
        return autoRefreshDelayFileSelection;
    }

    public void setAutoRefreshDelayFileSelection(Double autoRefreshDelayFileSelection) {
        this.autoRefreshDelayFileSelection = autoRefreshDelayFileSelection;
    }

    public Double getAutoRefreshDelayFileContentChange() {
        return autoRefreshDelayFileContentChange;
    }

    public void setAutoRefreshDelayFileContentChange(Double autoRefreshDelayFileContentChange) {
        this.autoRefreshDelayFileContentChange = autoRefreshDelayFileContentChange;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Boolean getCacheFiles() {
        return cacheFiles;
    }

    public void setCacheFiles(Boolean cacheFiles) {
        this.cacheFiles = cacheFiles;
    }

    public Boolean getShowInspections() {
        return showInspections;
    }

    public void setShowInspections(Boolean showInspections) {
        this.showInspections = showInspections;
    }

    public Boolean getShowLineMarkerInfos() {
        return showLineMarkerInfos;
    }

    public void setShowLineMarkerInfos(Boolean showLineMarkerInfos) {
        this.showLineMarkerInfos = showLineMarkerInfos;
    }

    public FileContextMode getFileContextMode() {
        return fileContextMode;
    }

    public void setFileContextMode(FileContextMode fileContextMode) {
        this.fileContextMode = fileContextMode;
    }

    public String getProjectRootDirectory() {
        return projectRootDirectory;
    }

    public void setProjectRootDirectory(String projectRootDirectory) {
        this.projectRootDirectory = projectRootDirectory;
    }
}