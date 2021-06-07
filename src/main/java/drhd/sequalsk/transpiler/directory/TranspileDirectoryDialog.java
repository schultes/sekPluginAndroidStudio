package drhd.sequalsk.transpiler.directory;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import drhd.sequalsk.utils.uibuilder.ViewUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog to transpile a directory to the specified output directory.
 */
public class TranspileDirectoryDialog extends DialogWrapper {

    private final Project project;
    private JPanel mainPanel;
    private TextFieldWithBrowseButton textFieldInputDirectory;
    private TextFieldWithBrowseButton textFieldOutputDirectory;
    private JPanel settingsPanel;
    private JButton startButton;
    private JPanel progressPanel;
    private JLabel labelStepOneProgress;
    private JLabel labelStepTwoProgress;
    private JLabel labelStepThreeProgress;
    private JLabel labelStepFourProgress;
    private JProgressBar progressBar;
    private EditorImpl progressEditor;
    private final String projectRootPath;

    public TranspileDirectoryDialog(Project project) {
        super(true);
        this.project = project;
        this.projectRootPath = PluginSettingsHelper.DirectoryTranspilerSettings.projectRootDirectory();
        init();
        setTitle("Transpile Directory");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        ViewUtils.setTitleSeparator(settingsPanel, "Settings");
        ViewUtils.setTitleSeparator(progressPanel, "Progress");

        textFieldInputDirectory.setText(projectRootPath);
        textFieldOutputDirectory.setText(PluginSettingsHelper.DirectoryTranspilerSettings.outputDirectory());

        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        textFieldInputDirectory.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));
        textFieldOutputDirectory.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));

        startButton.addActionListener(actionEvent -> {
            // dialog to confirm the output directory
            SimpleDialogBuilder builder = new SimpleDialogBuilder("Confirmation");
            builder.addLabel("All files of the output directory");
            builder.addLabel("\t" + getOutputDirectory());
            builder.addLabel("will be lost. Continue?");
            builder.withCancelAction();
            DialogWrapper dialogWrapper = builder.build();

            // transpile if user clicked ok
            if(dialogWrapper.showAndGet()) {
                transpileProject();
            }
        });

        progressEditor = new SimpleEditorBuilder().highlightingOff().hideLineNumbers().readOnly().build();
        progressEditor.getComponent().setPreferredSize(new Dimension(650, 250));
        mainPanel.add(progressEditor.getComponent(), BorderLayout.CENTER);

        return mainPanel;
    }

    protected void transpileProject() {
        // step 1: Clear output directory
        OutputDirectoryCleaner outputDirectoryCleaner = new OutputDirectoryCleaner(this);
        outputDirectoryCleaner.performStep();
        // next steps are performed automatically (chain of responsibility pattern)
    }

    public Project getProject() {
        return project;
    }

    public JLabel getLabelStepOneProgress() {
        return labelStepOneProgress;
    }

    public JLabel getLabelStepTwoProgress() {
        return labelStepTwoProgress;
    }

    public JLabel getLabelStepThreeProgress() {
        return labelStepThreeProgress;
    }

    public JLabel getLabelStepFourProgress() {
        return labelStepFourProgress;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public EditorImpl getProgressEditor() {
        return progressEditor;
    }

    public String getOutputDirectory() {
        return textFieldOutputDirectory.getText();
    }

    public String getInputDirectory() {
        return textFieldInputDirectory.getText();
    }

    @Override @NotNull
    protected Action[] createActions() {
        return new Action[]{getOKAction()};
    }

    @Override
    protected void dispose() {
        EditorFactory.getInstance().releaseEditor(progressEditor);
        super.dispose();
    }
}
