package drhd.sequalsk.transpiler.validation;

import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.validation.inspections.EmptyAdditionalFilesInspection;
import drhd.sequalsk.transpiler.validation.inspections.EmptyMainFileInspection;
import drhd.sequalsk.transpiler.validation.inspections.UnequalAdditionalFilesInspection;
import drhd.sequalsk.transpiler.validation.inspections.UnequalMainFileInspection;
import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes a {@link TranspilerResult} and generates a list of {@link TranspilerResultInspection}s.
 * If the result does not contain any problem the generated list is empty.
 */
public class TranspilerResultAnalyzer {

    /**
     * Contains a reference to every possible warning.
     */
    private final List<TranspilerResultInspection> allWarnings;

    /** {@link TranspilerResultAnalyzer} */
    public TranspilerResultAnalyzer() {
        this.allWarnings = new ArrayList<>();
        allWarnings.add(new EmptyAdditionalFilesInspection());
        allWarnings.add(new EmptyMainFileInspection());
        allWarnings.add(new UnequalMainFileInspection());
        allWarnings.add(new UnequalAdditionalFilesInspection());
    }

    /**
     * {@link TranspilerResultAnalyzer}
     */
    public List<TranspilerResultInspection> analyzeResult(TranspilerResult result) {
        List<TranspilerResultInspection> warnings = new ArrayList<>();

        for (TranspilerResultInspection transpilerWarning : allWarnings) {
            if(!transpilerWarning.isValidResult(result)) {
                warnings.add(transpilerWarning);
            }
        }

        return warnings;
    }

}
