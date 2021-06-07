package drhd.sequalsk.platform.services.settings;

import drhd.sequalsk.platform.services.settings.application.ApplicationSettingsService;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.platform.services.settings.project.ProjectSettingsService;
import drhd.sequalsk.transpiler.sequalskclient.SequalskClient;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerConfiguration;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;

/**
 * This is a utility class that provides convenient access to project and application level settings.
 */
public final class PluginSettingsHelper {

    private PluginSettingsHelper() {
    }

    public static class TranspilerSettings {

        /* RequestMode */
        public static TranspilerRequestMode requestMode() {
            return ProjectSettingsService.getInstance().getRequestMode();
        }

        public static void requestMode(TranspilerRequestMode requestMode) {
            ProjectSettingsService.getInstance().setRequestMode(requestMode);
        }

        /* Cache */
        public static Boolean cacheFiles() {
            return ProjectSettingsService.getInstance().getCacheFiles();
        }

        public static void cacheFiles(boolean b) {
            ProjectSettingsService.getInstance().setCacheFiles(b);
        }

        /* FileContextMode */

        public static FileContextMode fileContextMode() {
            return ProjectSettingsService.getInstance().getFileContextMode();
        }

        public static void fileContextMode(FileContextMode mode) {
            ProjectSettingsService.getInstance().setFileContextMode(mode);
        }

    }

    public static class OnlineTranspilerSettings {
        public static String host() {
            return ApplicationSettingsService.getInstance().getHost();
        }

        public static String requestPath() {
            return ApplicationSettingsService.getInstance().getRequestPath();
        }

        public static String requestHeaders() {
            return ApplicationSettingsService.getInstance().getRequestHeaders();
        }

        public static Integer timeoutInSeconds() {
            return ApplicationSettingsService.getInstance().getTimeout();
        }
    }

    public static class AutoRefreshSettings {

        /* activated */

        public static Boolean enabled() {
            return ProjectSettingsService.getInstance().getAutoRefreshEnabled();
        }

        public static void enabled(boolean value) {
            ProjectSettingsService.getInstance().setAutoRefreshEnabled(value);
        }

        /* on file selection */

        public static Boolean enabledOnFileSelection() {
            return enabled() && ProjectSettingsService.getInstance().getAutoRefreshOnFileSelection();
        }

        public static void enabledOnFileSelection(boolean value) {
            ProjectSettingsService.getInstance().setAutoRefreshOnFileSelection(value);
        }

        /* on file content change */

        public static Boolean enabledOnFileContentChange() {
            return enabled() && ProjectSettingsService.getInstance().getAutoRefreshOnFileContentChange();
        }

        public static void enabledOnFileContentChange(boolean value) {
            ProjectSettingsService.getInstance().setAutoRefreshOnFileContentChange(value);
        }

        /* file selection delay */

        public static Double fileSelectionDelay() {
            return ProjectSettingsService.getInstance().getAutoRefreshDelayFileSelection();
        }

        public static void fileSelectionDelay(double value) {
            ProjectSettingsService.getInstance().setAutoRefreshDelayFileSelection(value);
        }

        /* file content change delay */

        public static Double fileContentChangeDelay() {
            return ProjectSettingsService.getInstance().getAutoRefreshDelayFileContentChange();
        }

        public static void fileContentChangeDelay(double value) {
            ProjectSettingsService.getInstance().setAutoRefreshDelayFileContentChange(value);
        }
    }

    public static class UserInterfaceSettings {

        public static Boolean showActionBar() {
            return ProjectSettingsService.getInstance().getShowFloatingActionBar();
        }

        public static void showActionBar(boolean value) {
            ProjectSettingsService.getInstance().setShowFloatingActionBar(value);
        }

        public static Boolean showEditorPopupActions() {
            return ProjectSettingsService.getInstance().getShowEditorPopupActions();
        }

        public static void showEditorPopupActions(boolean value) {
            ProjectSettingsService.getInstance().setShowEditorPopupActions(value);
        }

        public static boolean showLineMarkerInfos() {
            return ProjectSettingsService.getInstance().getShowLineMarkerInfos();
        }

        public static void showLineMarkerInfos(boolean value) {
            ProjectSettingsService.getInstance().setShowLineMarkerInfos(value);
        }

        public static boolean showInspections() {
            return ProjectSettingsService.getInstance().getShowInspections();
        }

        public static void showInspections(boolean value) {
            ProjectSettingsService.getInstance().setShowInspections(value);
        }
    }

    public static class DirectoryTranspilerSettings {
        public static String outputDirectory() {
            return ProjectSettingsService.getInstance().getOutputDirectory();
        }

        public static void outputDirectory(String directory) {
            ProjectSettingsService.getInstance().setOutputDirectory(directory);
        }

        public static String projectRootDirectory() {
            return ProjectSettingsService.getInstance().getProjectRootDirectory();
        }

        public static void projectRootDirectory(String directory) {
            ProjectSettingsService.getInstance().setProjectRootDirectory(directory);
        }
    }

    /**
     * Generates a {@link TranspilerConfiguration} for the {@link SequalskClient} bases on the current plugin
     * configurations.
     */
    public static TranspilerConfiguration asTranspilerConfig() {
        return new TranspilerConfiguration(
                OnlineTranspilerSettings.host(),
                OnlineTranspilerSettings.requestPath(),
                OnlineTranspilerSettings.timeoutInSeconds(),
                OnlineTranspilerSettings.requestHeaders()
        );
    }
}
