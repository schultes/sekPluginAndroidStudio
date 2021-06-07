package drhd.sequalsk.platform.services.settings.project.views.embedded;

import drhd.sequalsk.platform.services.settings.SettingsComponentEmbeddable;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;
import javax.swing.*;

public class TranspilerSettingsPanel implements SettingsComponentEmbeddable {

    private JPanel mainPanel;
    private JRadioButton radioButtonOneWay;
    private JRadioButton radioButtonTwoWay;
    private JCheckBox checkBoxCacheActive;
    private JRadioButton radioButtonFastContextMode;
    private JRadioButton radioButtonAccurateContextMode;
    private JRadioButton radioButtonProjectContextMode;

    @Override
    public void initialize() {

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public TranspilerRequestMode getRequestMode() {
        if(radioButtonOneWay.isSelected()) return TranspilerRequestMode.ONE_WAY;
        return TranspilerRequestMode.TWO_WAY;
    }

    public void setRequestMode(TranspilerRequestMode requestMode) {
        radioButtonOneWay.setSelected(requestMode == TranspilerRequestMode.ONE_WAY);
        radioButtonTwoWay.setSelected(requestMode == TranspilerRequestMode.TWO_WAY);
    }

    public FileContextMode getFileContextMode() {
        if(radioButtonProjectContextMode.isSelected()) return FileContextMode.PROJECT;
        if(radioButtonAccurateContextMode.isSelected()) return FileContextMode.REFERENCES_ACCURATE;
        return FileContextMode.REFERENCES_FAST;
    }

    public void setFileContextMode(FileContextMode contextMode) {
        radioButtonProjectContextMode.setSelected(contextMode == FileContextMode.PROJECT);
        radioButtonAccurateContextMode.setSelected(contextMode == FileContextMode.REFERENCES_ACCURATE);
        radioButtonFastContextMode.setSelected(contextMode == FileContextMode.REFERENCES_FAST);
    }

    public Boolean getCacheFiles() {
        return checkBoxCacheActive.isSelected();
    }

    public void setCacheFiles(boolean b) {
        checkBoxCacheActive.setSelected(b);
    }

}
