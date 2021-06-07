package drhd.sequalsk.transpiler.validation.inspections;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;
import drhd.sequalsk.transpiler.validation.TranspilerResultInspection;
import drhd.sequalsk.transpiler.validation.TranspilerInspectionAction;
import drhd.sequalsk.transpiler.validation.utils.TranspiledFileComparator;
import drhd.sequalsk.transpiler.sequalskclient.utils.OutputUtils;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleDiffViewerBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;


/**
 * Displayed if the transpiled content of the main file does not match the content of the original main file.
 * Only shown for two-way results.
 */
public class UnequalMainFileInspection extends TranspilerResultInspection {

    @Override
    public String getTitle() {
        return "Main file not equal to original content";
    }

    @Override
    public String getDescription() {
        return "The content of the two-way-transpiled main file does not match the original content";
    }

    @Override
    public TranspilerResultInspector generateResultInspector() {
        return new TranspilerResultInspector() {
            @Override
            public boolean isValidResult(TranspilerResult result) {

                if (result.getRequest().getRequestMode() == TranspilerRequestMode.ONE_WAY) {
                    return true;
                }

                return TranspiledFileComparator.isEqual(result.getMainFile());
            }
        };
    }

    @Override
    protected Severity getSeverity() {
        return Severity.ERROR;
    }

    @Override
    public List<AnAction> getActions() {

        // details action
        TranspilerInspectionAction detailsAction = new TranspilerInspectionAction("Show Details", AllIcons.General.ShowInfos) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Warning: ").append(getTitle()).append("\n");
                stringBuilder.append(getDescription());

                stringBuilder.append("\n\n### Transpiler Context ###\n\n");
                stringBuilder.append(OutputUtils.printContext(transpilerResult.getTranspilerContext()));

                SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Warning Details");
                dialogBuilder.addEditor(new SimpleEditorBuilder(stringBuilder.toString()).highlightingOff().hideLineNumbers());
                dialogBuilder.buildAndShow();
            }
        };

        // diff viewer action
        TranspilerInspectionAction diffAction = new TranspilerInspectionAction("Show Difference", AllIcons.Actions.Diff) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                TranspilerRequestFile originalMainFile = transpilerResult.getTranspilerContext().getMainFile();
                String originalText = originalMainFile.getContent();
                String transpiledText = transpilerResult.getMainFile().getTranspiledContent();

                new SimpleDiffViewerBuilder(e.getProject())
                        .left(originalMainFile.getPath(), originalText)
                        .right(originalMainFile.getName() + " (transpiled)", transpiledText)
                        .show();
            }
        };

        return Arrays.asList(detailsAction, diffAction);
    }
}
