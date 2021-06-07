package drhd.sequalsk.platform.services.settings.application.views;

import drhd.sequalsk.utils.uibuilder.ViewUtils;
import javax.swing.*;

public class ApplicationSettingsPanel {

    private JPanel mainPanel;

    private OnlineTranspilerSettingsPanel onlineTranspilerSettingsPanel;

    public JPanel createPanel() {
        onlineTranspilerSettingsPanel.initialize();

        ViewUtils.setTitleSeparator(onlineTranspilerSettingsPanel.getMainPanel(), "Online Transpiler Configuration");

        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return onlineTranspilerSettingsPanel.getMainPanel();
    }

    public OnlineTranspilerSettingsPanel onlineTranspilerSettings() {
        return onlineTranspilerSettingsPanel;
    }
}
