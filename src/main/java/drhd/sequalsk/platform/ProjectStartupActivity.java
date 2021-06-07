package drhd.sequalsk.platform;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import drhd.sequalsk.platform.services.feedback.TranspilerFeedbackEditorService;
import drhd.sequalsk.platform.services.feedback.TranspilerFeedbackService;
import drhd.sequalsk.platform.services.fileservice.FileContentChangeService;
import drhd.sequalsk.platform.services.fileservice.FileSelectionService;
import drhd.sequalsk.platform.services.transpiler.TranspilerServiceCache;
import drhd.sequalsk.platform.services.transpiler.TranspilerService;
import org.jetbrains.annotations.NotNull;

/**
 * Initializes all project level services of the plugin after the project has been loaded.
 */
public class ProjectStartupActivity implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {

        FileSelectionService editorListener = project.getService(FileSelectionService.class);
        editorListener.initService(project);

        FileContentChangeService listenerService = project.getService(FileContentChangeService.class);
        listenerService.initService(project);

        TranspilerService transpilerService = project.getService(TranspilerService.class);
        transpilerService.initializeService(project);

        TranspilerServiceCache transpilerCacheService = project.getService(TranspilerServiceCache.class);
        transpilerCacheService.initializeService(project);

        TranspilerFeedbackService feedbackService = project.getService(TranspilerFeedbackService.class);
        feedbackService.initializeService(project);

        TranspilerFeedbackEditorService feedbackEditorService = project.getService(TranspilerFeedbackEditorService.class);
        feedbackEditorService.initializeService(project);

    }

}
