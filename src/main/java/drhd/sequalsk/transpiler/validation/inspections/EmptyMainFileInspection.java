package drhd.sequalsk.transpiler.validation.inspections;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.transpiler.validation.TranspilerResultInspection;
import drhd.sequalsk.transpiler.validation.TranspilerInspectionAction;
import drhd.sequalsk.transpiler.sequalskclient.utils.OutputUtils;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * If the transpiler returns an empty result for the main file of the request.
 */
public class EmptyMainFileInspection extends TranspilerResultInspection {

    @Override
    public String getTitle() {
        return "Main file does not contain content";
    }

    @Override
    public String getDescription() {
        return "The transpiler did not return any content for the main file";
    }

    @Override
    public TranspilerResultInspector generateResultInspector() {
        return result -> {
            String content = result.getMainFile().getTranspiledContent();
            boolean mainFileIsEmpty = (content == null || content.equals("") || content.equals("-"));
            return !mainFileIsEmpty;
        };
    }

    @Override
    protected TranspilerResultInspection.Severity getSeverity() {
        return Severity.ERROR;
    }

    @Override
    public List<AnAction> getActions() {
        TranspilerInspectionAction detailsAction = new TranspilerInspectionAction("Show Details", AllIcons.General.ShowInfos) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Warning: " + getTitle() + "\n");
                stringBuilder.append("The transpiler did not return any content for the main file.\n");

                stringBuilder.append("\n\n### Transpiler Context ###\n\n");
                stringBuilder.append(OutputUtils.printContext(transpilerResult.getTranspilerContext()));

                SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Warning Details");
                dialogBuilder.addEditor(new SimpleEditorBuilder(stringBuilder.toString()).highlightingOff().hideLineNumbers());
                dialogBuilder.buildAndShow();
            }
        };

        return Collections.singletonList(detailsAction);
    }
}
