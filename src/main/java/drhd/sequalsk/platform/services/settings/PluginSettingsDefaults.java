package drhd.sequalsk.platform.services.settings;

import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;

/**
 * Class that contains a default value for every setting that is configurable in the plugin settings.
 */
public final class PluginSettingsDefaults {

    // transpiler settings
    public static final TranspilerRequestMode requestMode = TranspilerRequestMode.ONE_WAY;
    public static final FileContextMode fileContextMode = FileContextMode.REFERENCES_ACCURATE;

    // online mode settings
    public static final String host = "https://transpile.iem.thm.de/";
    public static final String requestPath = "sek";
    public static final String requestHeaders = "Accept: */*";
    public static final Integer timeout = 10;

    // automatic transpiler settings
    public static final Boolean autoRefreshEnabled = true;
    public static final Boolean autoRefreshOnFileSelection = true;
    public static final Boolean autoRefreshOnFileContentChange = true;
    public static final Double autoRefreshDelayFileSelection = 1d;
    public static final Double autoRefreshDelayFileContentChange = 1d;
    public static final Double autoRefreshDelayMinValue = 0.5;
    public static final Double autoRefreshDelayMaxValue = 10.0;

    // directories
    public static final String outputDirectory = "";
    public static final String projectRootDirectory = "";

    // user interface settings
    public static final Boolean showFloatingActionBar = true;
    public static final Boolean showEditorPopupActions = true;
    public static final Boolean showInspections = true;
    public static final Boolean showLineMarkerInfos = true;

    // cache
    public static final Boolean cacheFiles = true;
}
