package drhd.sequalsk.platform.services.settings.project.views.embedded;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import drhd.sequalsk.platform.services.settings.SettingsComponentEmbeddable;
import javax.swing.*;

public class ProjectTranspilerSettingsPanel implements SettingsComponentEmbeddable {

    private JPanel mainPanel;
    private TextFieldWithBrowseButton textFieldOutputDirectory;
    private TextFieldWithBrowseButton textFieldProjectRoot;

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void initialize() {
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        textFieldOutputDirectory.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));
        textFieldProjectRoot.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));
    }

    public String getOutputDirectory() {
        return textFieldOutputDirectory.getText();
    }

    public void setOutputDirectory(String directory) {
        textFieldOutputDirectory.setText(directory);
    }

    public String getProjectRootDirectory() {
        return textFieldProjectRoot.getText();
    }

    public void setProjectRootDirectory(String directory) {
        textFieldProjectRoot.setText(directory);
    }

}
