package drhd.sequalsk.platform.services.transpiler.utils;

import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.platform.services.transpiler.TranspilerService;
import drhd.sequalsk.utils.debugging.DebugLogger;
import drhd.sequalsk.utils.PluginVirtualFileUtils;

/**
 * Helper class for {@link TranspilerService}. Contains some utility methods that check the current plugin settings.
 */
public class TranspilerServiceUtils {

    /**
     * Checks if a selected file should be read from cache immediately.
     */
    public static boolean readResultOnFileSelection(VirtualFile selectedFile) {
        /* check filetype */
        if(!PluginVirtualFileUtils.isKotlinFile(selectedFile)) {
            DebugLogger.info(TranspilerServiceUtils.class, "Ignoring file selection (file is no kotlin file)");
            return false;
        }

        /* check if auto refresh is enabled */
        if (!PluginSettingsHelper.AutoRefreshSettings.enabledOnFileSelection()) {
            DebugLogger.info(TranspilerServiceUtils.class, "Ignoring file selection (auto refresh inactive for file selection)");
            return false;
        }

        return true;
    }

    /**
     * Checks if a selected file should be transpiled after the selection delay has expired.
     */
    public static boolean transpileFileOnSelection(VirtualFile virtualFile) {
        if(!PluginVirtualFileUtils.isKotlinFile(virtualFile)) {
            DebugLogger.info(TranspilerServiceUtils.class, "Not transpiling selected file (file is no kotlin file)");
            return false;
        }

        /* check if auto refresh is enabled */
        if (!PluginSettingsHelper.AutoRefreshSettings.enabledOnFileSelection()) {
            DebugLogger.info(TranspilerServiceUtils.class, "Not transpiling selected file (auto refresh inactive for file selection)");
            return false;
        }

        return true;
    }

    /**
     * Checks if a file should be transpiled after its content has changed.
     */
    public static boolean transpileFileOnContentChange() {
        if (!PluginSettingsHelper.AutoRefreshSettings.enabledOnFileContentChange()) {
            DebugLogger.info(TranspilerServiceUtils.class, "Ignoring content change delay expiration (auto refresh inactive for content changes)");
            return false;
        }

        return true;
    }

}
