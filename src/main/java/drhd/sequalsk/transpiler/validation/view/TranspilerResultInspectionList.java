package drhd.sequalsk.transpiler.validation.view;

import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.validation.TranspilerResultAnalyzer;
import drhd.sequalsk.transpiler.validation.TranspilerResultInspection;
import javax.swing.*;
import java.util.List;


/**
 * List that displays the results of the automatic analysis of the latest transpiler result.
 *
 * JPanel that shows a list of {@link TranspilerResultInspectionListItem}s.
 *
 * @see TranspilerResultAnalyzer
 * @see TranspilerResultInspection
 */
public class TranspilerResultInspectionList extends JPanel {

    public TranspilerResultInspectionList() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentY(LEFT_ALIGNMENT);
    }

    public void updateWarningItems(TranspilerResult transpilerResult) {
        TranspilerResultAnalyzer analyzer = new TranspilerResultAnalyzer();
        List<TranspilerResultInspection> warnings = analyzer.analyzeResult(transpilerResult);
        updateWarningItems(warnings);
    }

    private void updateWarningItems(List<TranspilerResultInspection> transpilerWarnings) {
        this.removeAll();
        for(TranspilerResultInspection warning : transpilerWarnings) {
            this.add(new TranspilerResultInspectionListItem(warning));
        }
    }
}
