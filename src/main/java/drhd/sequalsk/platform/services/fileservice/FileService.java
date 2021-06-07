package drhd.sequalsk.platform.services.fileservice;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.platform.services.PluginBaseService;
import drhd.sequalsk.utils.debugging.DebugLogger;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for the services that are responsible to handle file content changes and selection changes in the
 * code editor.
 */
abstract public class FileService extends PluginBaseService implements DelayTimer.DelayTimerListener {

    /**
     * The virtual file that is currently active in the code editor. Null if it is not a kotlin file.
     */
    protected VirtualFile currentEditorFile;

    /**
     * The timer that delays the dispatching of the change/selection event.
     */
    protected DelayTimer delayTimer;

    /**
     * Initialize the project service.
     */
    public void initService(@NotNull Project project) {
        this.project = project;
        this.createDelayTimer();
        onServiceInitialized();
        DebugLogger.info(this, "Initialized " + this.getClass().getSimpleName());
    }

    /**
     * Creates the {@link #delayTimer}
     */
    protected void createDelayTimer() {
        this.delayTimer = new DelayTimer(
                this,
                this::getDelayTime,
                getClass().getSimpleName() + " timer"
        );
    }

    /** Provides the delay time in milliseconds */
    abstract protected long getDelayTime();

    /**
     * Called as soon the service has been initialized. Used to further initialize the service, e.g. to subscribe to the
     * message bus.
     */
    abstract protected void onServiceInitialized();

}
