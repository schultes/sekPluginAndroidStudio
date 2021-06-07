package drhd.sequalsk.transpiler.directory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import drhd.sequalsk.utils.debugging.ExceptionUtils;
import javax.swing.*;
import java.io.File;

/**
 * The transpilation of a directory contains four steps: <ol>
 *     <li>Validate and clear the output directory</li>
 *     <li>Build the transpiler context</li>
 *     <li>Transpile the files of the context</li>
 *     <li>Write the swift files to the output directory</li>
 * </ol>
 *
 * Each step of the process is a step in a chain of responsibility. This is the base class for each step implementation.
 */
abstract public class DirectoryTranspilerProcessor {

    /**
     * The dialog that contains the ui-elements that must be updated from this processor.
     */
    protected final TranspileDirectoryDialog dialog;

    /**
     * Shows whether the step completed successfully or not.
     */
    protected boolean success;

    public DirectoryTranspilerProcessor(TranspileDirectoryDialog transpileDirectoryDialog) {
        this.dialog = transpileDirectoryDialog;
    }

    /**
     * Executes the step of the chain and starts the next step on success.
     */
    public void performStep() {
        Runnable runnable = () -> {
            try {
                getProgressLabel().setText("work in progress");
                addEditorContent("\n#### Step " + getStepOrder() + ": " + getStepTitle() + " ####");
                addEditorContent("\n" + getStepDescription() + "... ");
                executeStep();
                dialog.getProgressBar().setValue(getStepOrder());
                getProgressLabel().setText("done");

                if(getNextProcessor() != null && success) getNextProcessor().performStep();
            } catch (Exception e) {
                success = false;
                addEditorContent(ExceptionUtils.getStackTraceAsString(e));
                e.printStackTrace();
            }
        };
        ApplicationManager.getApplication().invokeLater(runnable, ModalityState.any());
    }

    abstract protected Integer getStepOrder();

    abstract protected String getStepTitle();

    abstract protected String getStepDescription();

    abstract protected JLabel getProgressLabel();

    abstract protected DirectoryTranspilerProcessor getNextProcessor();

    abstract protected void executeStep();

    protected void finishedSuccess() {
        addEditorContent("done!\n");
        success = true;
    }

    protected void finishedSuccess(String finishedMessage) {
        addEditorContent(finishedMessage + "\n");
        success = true;
    }

    protected void finishedFailure(String finishedMessage) {
        addEditorContent(finishedMessage + "\n");
        success = false;
    }

    protected void finishedFailure(Exception e) {
        addEditorContent(ExceptionUtils.getStackTraceAsString(e));
        success = false;
    }

    /**
     * Adds content to the output window of the project transpiler dialog.
     */
    protected void addEditorContent(String content) {
        // the runWriteAction is getting ignored sometimes for unknown reasons - so as a the runWriteAction is embedded in invokeLaterRunnable
        Runnable runnable = () -> {
            ApplicationManager.getApplication().runWriteAction(() -> {
                String text = dialog.getProgressEditor().getDocument().getText();
                dialog.getProgressEditor().getDocument().setText(text + content);
                EditorUtil.scrollToTheEnd(dialog.getProgressEditor(), true);
            });
        };
        ApplicationManager.getApplication().invokeLater(runnable);
    }

    protected File asFile(String path) {
        return new File(path);
    }

}
