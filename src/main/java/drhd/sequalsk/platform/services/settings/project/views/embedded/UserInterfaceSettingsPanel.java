package drhd.sequalsk.platform.services.settings.project.views.embedded;

import drhd.sequalsk.platform.services.settings.SettingsComponentEmbeddable;
import javax.swing.*;

public class UserInterfaceSettingsPanel implements SettingsComponentEmbeddable {

    private JPanel mainPanel;
    private JCheckBox checkBoxShowActionBar;
    private JCheckBox checkBoxShowEditorPopup;
    private JCheckBox checkBoxLineMarkerInfo;
    private JCheckBox checkBoxShowInspections;

    @Override
    public void initialize() {

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Boolean showActionBar() {
        return checkBoxShowActionBar.isSelected();
    }

    public void setShowActionBar(boolean enabled) {
        checkBoxShowActionBar.setSelected(enabled);
    }

    public Boolean showEditorPopupMenuActions() {
        return checkBoxShowEditorPopup.isSelected();
    }

    public void setShowEditorPopupActions(boolean enabled) {
        checkBoxShowEditorPopup.setSelected(enabled);
    }

    public Boolean showInspections() {
        return checkBoxShowInspections.isSelected();
    }

    public void setShowInspections(boolean enabled) {
        checkBoxShowInspections.setSelected(enabled);
    }

    public Boolean showLineMarkerInfos() {
        return checkBoxLineMarkerInfo.isSelected();
    }

    public void setShowLineMarkerInfos(boolean enabled) {
        checkBoxLineMarkerInfo.setSelected(enabled);
    }
}
