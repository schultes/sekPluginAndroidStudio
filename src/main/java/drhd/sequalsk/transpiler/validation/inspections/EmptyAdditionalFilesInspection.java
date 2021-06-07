package drhd.sequalsk.transpiler.validation.inspections;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspiledFile;
import drhd.sequalsk.transpiler.validation.TranspilerResultInspection;
import drhd.sequalsk.transpiler.validation.TranspilerInspectionAction;
import drhd.sequalsk.transpiler.sequalskclient.utils.OutputUtils;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * In most cases a {@link TranspilerRequest} contains referenced files in addition to the main file. If at least one
 * of the referenced files was received without transpiled content this warning is shown.
 */
public class EmptyAdditionalFilesInspection extends TranspilerResultInspection {

    private final List<TranspiledFile> emptyFiles = new ArrayList<>();
    private final List<TranspiledFile> nonEmptyFiles = new ArrayList<>();

    @Override
    public String getTitle() {
        return "Result contains empty additional files";
    }

    @Override
    public String getDescription() {
        return "Result contains files without content: not all documents of the context were translated";
    }

    @Override
    public TranspilerResultInspector generateResultInspector() {
        return result -> {
            for (TranspiledFile transpiledFile : result.getAdditionalFiles()) {
                if (transpiledFile.getTranspiledContent().equals("-")) {
                    emptyFiles.add(transpiledFile);
                } else {
                    nonEmptyFiles.add(transpiledFile);
                }
            }
            return emptyFiles.size() == 0;
        };
    }

    @Override
    protected Severity getSeverity() {
        return Severity.WARNING;
    }

    @Override
    public List<AnAction> getActions() {
        TranspilerInspectionAction detailsAction = new TranspilerInspectionAction("Show Details", AllIcons.General.ShowInfos) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Warning: ").append(getTitle()).append("\n")
                        .append(getDescription())
                        .append("\n\n### Files without content ###\n\n");
                stringBuilder.append(OutputUtils.printTranspiledFiles(emptyFiles))
                        .append("\n\n### Transpiler Context ###\n\n")
                        .append(OutputUtils.printContext(transpilerResult.getTranspilerContext()));

                SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Warning Details");
                dialogBuilder.addEditor(new SimpleEditorBuilder(stringBuilder.toString()).highlightingOff().hideLineNumbers());
                dialogBuilder.buildAndShow();
            }
        };

        return Collections.singletonList(detailsAction);
    }
}
