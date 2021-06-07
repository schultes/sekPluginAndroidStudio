package drhd.sequalsk.platform.services.toolwindow;

import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;

/**
 * Listener that reacts whenever the displayed {@link TranspilerResult} in the {@link ToolWindowService} has changed.
 */
public interface ResultChangeListener {

    /**
     * Called when the {@link TranspilerResult} in the {@link ToolWindowService} has changed or been replaced.
     */
    void onResultChanged(TranspilerResult result);

    /**
     * Called when the {@link TranspilerResult} in the {@link ToolWindowService} was removed (not replaced).
     */
    void onResultCleared();

}
