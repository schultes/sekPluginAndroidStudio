package drhd.sequalsk.platform.services.settings.project.views.embedded;

import drhd.sequalsk.platform.services.settings.SettingsComponentEmbeddable;
import drhd.sequalsk.platform.services.settings.PluginSettingsDefaults;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import javax.swing.*;

public class AutoRefreshSettingsPanel implements SettingsComponentEmbeddable {

    private JPanel mainPanel;
    private JCheckBox checkBoxEnabled;
    private JCheckBox checkBoxOnContentChange;
    private JCheckBox checkBoxOnFileSelection;
    private JSpinner spinnerFileSelection;
    private JSpinner spinnerContentChange;

    @Override
    public void initialize() {
        double autoReloadMinValue = PluginSettingsDefaults.autoRefreshDelayMinValue;
        double autoReloadMaxValue = PluginSettingsDefaults.autoRefreshDelayMaxValue;
        double fsValue = PluginSettingsHelper.AutoRefreshSettings.fileSelectionDelay();
        double fcsValue = PluginSettingsHelper.AutoRefreshSettings.fileContentChangeDelay();

        spinnerFileSelection.setModel(new SpinnerNumberModel(fsValue, autoReloadMinValue, autoReloadMaxValue, 0.1));
        spinnerContentChange.setModel(new SpinnerNumberModel(fcsValue, autoReloadMinValue, autoReloadMaxValue, 0.1));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Boolean enabled() {
        return checkBoxEnabled.isSelected();
    }

    public void setEnabled(boolean enabled) {
        checkBoxEnabled.setSelected(enabled);
    }

    public Boolean onFileSelectionEnabled() {
        return checkBoxOnFileSelection.isSelected();
    }

    public void setOnFileSelectionEnabled(boolean enabled) {
        checkBoxOnFileSelection.setSelected(enabled);
    }

    public Boolean onContentChangeEnabled() {
        return checkBoxOnContentChange.isSelected();
    }

    public void setOnContentChangeEnabled(boolean enabled) {
        checkBoxOnContentChange.setSelected(enabled);
    }

    public Double getDelayTimeFileSelection() {
        return (Double) spinnerFileSelection.getValue();
    }

    public void setDelayTimeFileSelection(Double value) {
        spinnerFileSelection.getModel().setValue(value);
    }

    public Double getDelayTimeFileContentChange() {
        return (Double) spinnerContentChange.getValue();
    }

    public void setDelayTimeFileContentChange(Double value) {
        spinnerContentChange.getModel().setValue(value);
    }
}
