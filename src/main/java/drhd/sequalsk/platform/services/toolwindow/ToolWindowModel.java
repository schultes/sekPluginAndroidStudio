package drhd.sequalsk.platform.services.toolwindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Shared model across all components of the tool window.
 */
public class ToolWindowModel {

    /**
     * Project that the tool window is part of.
     */
    protected final Project project;

    /**
     * The transpiler result that is currently shown in the tool window.
     */
    private TranspilerResult currentResult;

    /**
     * Components that are informed when the result to display changes.
     */
    private final List<ResultChangeListener> resultChangeListenerList;

    public ToolWindowModel(Project project) {
        this.project = project;
        resultChangeListenerList = new ArrayList<>();
    }

    /**
     * Add a listener that will be informed whenever the result to display changes.
     */
    public void addListener(ResultChangeListener listener) {
        resultChangeListenerList.add(listener);
    }

    /**
     * Sets the result that must be displayed in the tool window.
     */
    public void setCurrentResult(final TranspilerResult currentResult) {
        this.currentResult = currentResult;

        Runnable runnable = () -> resultChangeListenerList.forEach(listener -> listener.onResultChanged(currentResult));
        ApplicationManager.getApplication().invokeLater(runnable);
    }

    /**
     * Removes the result from the tool window.
     */
    public void clearResult() {
        this.currentResult = null;
        resultChangeListenerList.forEach(ResultChangeListener::onResultCleared);
    }

    /**
     * Returns the transpiler result that must be displayed in the tool window.
     */
    public TranspilerResult getCurrentResult() {
        return currentResult;
    }

    /** {@link #project} */
    public Project getProject() {
        return project;
    }
}
