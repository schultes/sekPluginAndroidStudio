package drhd.sequalsk.transpiler.validation.inspections;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspiledFile;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;
import drhd.sequalsk.transpiler.validation.TranspilerResultInspection;
import drhd.sequalsk.transpiler.validation.TranspilerInspectionAction;
import drhd.sequalsk.transpiler.validation.utils.TranspiledFileComparator;
import drhd.sequalsk.transpiler.sequalskclient.utils.OutputUtils;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleDiffViewerBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * In most cases a {@link TranspilerRequest} contains referenced files in addition to the main file. If at least one
 * of the referenced files was received with transpiled content that does not match the original file this warning
 * is shown.
 */
public class UnequalAdditionalFilesInspection extends TranspilerResultInspection {

    private final List<TranspiledFile> equalFiles = new ArrayList<>();
    private final List<TranspiledFile> unequalFiles = new ArrayList<>();

    @Override
    public String getTitle() {
        return "Referenced file(s) not equal to original content";
    }

    @Override
    public String getDescription() {
        return "Found files whose content does not match the original content";
    }

    @Override
    public TranspilerResultInspector generateResultInspector() {
        return result -> {
            if(result.getRequest().getRequestMode() == TranspilerRequestMode.ONE_WAY) return true;

            for(TranspiledFile transpiledFile : result.getAdditionalFiles()) {

                boolean equal = TranspiledFileComparator.isEqual(transpiledFile);
                if (equal) {
                    equalFiles.add(transpiledFile);
                } else {
                    unequalFiles.add(transpiledFile);
                }
            }
            return unequalFiles.size() == 0;
        };
    }

    @Override
    protected Severity getSeverity() {
        return Severity.WARNING;
    }

    @Override
    public List<AnAction> getActions() {

        // details action
        TranspilerInspectionAction detailsAction = new TranspilerInspectionAction("Show Details", AllIcons.General.ShowInfos) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Warning: ").append(getTitle()).append("\n");
                stringBuilder.append("Transpiler issues in referenced file(s) detected: Detected two-way-transpiled files whose content does not match the original content\n");
                stringBuilder.append("\n\n### Files with unequal content ###\n\n");
                stringBuilder.append(OutputUtils.printTranspiledFiles(unequalFiles));
                stringBuilder.append("\n\n### Transpiler Context ###\n\n");
                stringBuilder.append(OutputUtils.printContext(transpilerResult.getTranspilerContext()));

                SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Warning Details");
                dialogBuilder.addEditor(new SimpleEditorBuilder(stringBuilder.toString()).highlightingOff().hideLineNumbers());
                dialogBuilder.buildAndShow();
            }
        };

        // diff viewer action
        TranspilerInspectionAction diffAction = new TranspilerInspectionAction("Show Differences", AllIcons.Actions.Diff) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                String originalText = transpilerResult.getOriginalCode();
                String transpiledText = transpilerResult.getTranspilerResponse();

                new SimpleDiffViewerBuilder(e.getProject())
                        .left("Original.kt", originalText)
                        .right("Transpiled.kt", transpiledText)
                        .show();
            }
        };

        return Arrays.asList(detailsAction, diffAction);
    }
}
