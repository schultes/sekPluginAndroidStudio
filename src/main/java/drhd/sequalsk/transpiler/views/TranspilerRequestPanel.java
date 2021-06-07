package drhd.sequalsk.transpiler.views;

import drhd.sequalsk.transpiler.utils.TranspilerContextType;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerLanguage;
import drhd.sequalsk.transpiler.sequalskclient.utils.OutputUtils;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;

/**
 * Form to show the details of a transpiler request.
 */
public class TranspilerRequestPanel {

    private TranspilerRequest request;

    private JPanel mainPanel;
    private JLabel labelRequestMode;
    private JLabel labelLanguages;
    private JButton buttonShowOriginalCode;

    private JLabel labelJobType;
    private JButton buttonShowContext;

    public TranspilerRequestPanel(@NotNull TranspilerRequest request) {
        setRequest(request);
    }

    public void setRequest(TranspilerRequest request) {
        this.request = request;
        setRequestValues();
    }

    private void setRequestValues() {

        /* request mode */
        labelRequestMode.setText(request.getRequestMode().getName());

        /* languages */
        String inputLanguage = request.getInputLanguage().getName();
        String outputLanguage = TranspilerLanguage.other(request.getInputLanguage()).getName();
        labelLanguages.setText(inputLanguage + " to " + outputLanguage);

        /* job type */
        String contentType = (String) request.getTag(TranspilerContextType.TAG_KEY);
        if (contentType != null && !contentType.isEmpty()) labelJobType.setText(contentType);

        /* original code  */
        buttonShowOriginalCode.addActionListener(e -> {
            String content = request.getContext().asSingleContent();
            SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Original Code");
            dialogBuilder.addEditor(new SimpleEditorBuilder(content));
            dialogBuilder.buildAndShow();
        });

        /* context  */
        buttonShowContext.addActionListener(e -> {
            TranspilerContext context = request.getContext();
            String contextPrintOutput = OutputUtils.printContext(context);
            SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Transpiler Context");
            dialogBuilder.addEditor(new SimpleEditorBuilder(contextPrintOutput).highlightingOff());
            dialogBuilder.build().show();
        });

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
