package drhd.sequalsk.transpiler.views;

import com.intellij.openapi.ui.DialogWrapper;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerConfiguration;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerException;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.utils.debugging.ExceptionUtils;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Form to show the details of a transpiler exception or any other exception that occurred during a transpilation process.
 */
public class TranspilerExceptionDialog extends DialogWrapper {

    private final Throwable throwable;
    private TranspilerRequest transpilerRequest;

    private JPanel mainPanel;
    private JLabel labelMessage;
    private JLabel labelCause;
    private JButton buttonConfiguration;
    private JButton buttonRequestDetails;
    private JButton buttonStacktrace;

    public TranspilerExceptionDialog(Throwable throwable) {
        super(true);
        this.throwable = throwable;
        if (throwable instanceof TranspilerException) {
            this.transpilerRequest = ((TranspilerException) throwable).getTranspilerRequest();
        }
        initialize();
    }

    public TranspilerExceptionDialog(Throwable throwable, TranspilerRequest transpilerRequest) {
        super(true);
        this.throwable = throwable;
        this.transpilerRequest = transpilerRequest;
        initialize();
    }

    private void initialize() {
        init();
        setTitle("Transpilation Error");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        exceptionDetails();
        buttonRequestDetails();
        buttonStacktrace();
        buttonConfiguration();
        return mainPanel;
    }

    private void exceptionDetails() {
        labelMessage.setText(throwable.getMessage());
        labelCause.setText("-");
        if (throwable.getCause() != null) {
            labelCause.setText(throwable.getCause().toString());
        }
    }

    private void buttonConfiguration() {
        buttonConfiguration.addActionListener(e -> {
            TranspilerConfiguration config = PluginSettingsHelper.asTranspilerConfig();
            SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Transpiler Configuration");
            dialogBuilder.addComponent(new TranspilerConfigPanel(config).getMainPanel());
            dialogBuilder.buildAndShow();
        });
    }

    private void buttonStacktrace() {
        buttonStacktrace.addActionListener(e -> {
            String message = ExceptionUtils.printStacktraceAsString(throwable);
            if (throwable.getCause() != null) message = ExceptionUtils.getStackTraceAsString(throwable.getCause());

            final String stacktrace = message;

            SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Stacktrace");
            dialogBuilder.addEditor(new SimpleEditorBuilder(stacktrace));
            dialogBuilder.buildAndShow();
        });
    }

    private void buttonRequestDetails() {
        buttonRequestDetails.addActionListener(e -> {

            if (this.transpilerRequest != null) {
                SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Transpiler Request Details");
                dialogBuilder.addComponent(new TranspilerRequestPanel(transpilerRequest).getMainPanel());
                dialogBuilder.buildAndShow();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "No Transpiler Request available.");
            }
        });
    }

    @Override @NotNull
    protected Action[] createActions() {
        return new Action[]{getOKAction()};
    }
}
