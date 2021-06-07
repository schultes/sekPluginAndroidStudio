package drhd.sequalsk.platform.services.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Initializes the {@link ToolWindowService}.
 */
public class SequalskToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolWindowService toolWindowService = project.getService(ToolWindowService.class);
        toolWindowService.initializeService(project);
        toolWindowService.setToolWindow(toolWindow);
    }
}
