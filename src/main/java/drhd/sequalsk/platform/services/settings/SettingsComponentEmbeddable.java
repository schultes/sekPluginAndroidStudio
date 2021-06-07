package drhd.sequalsk.platform.services.settings;

import drhd.sequalsk.platform.services.settings.application.views.ApplicationSettingsPanel;
import drhd.sequalsk.platform.services.settings.project.views.ProjectSettingsPanel;

import javax.swing.*;

/**
 * For a better overview the settings have been split into several forms.
 * The main panels {@link ApplicationSettingsPanel} and {@link ProjectSettingsPanel}
 * each contain subviews for specific settings, that must be marked with this interface.
 */
public interface SettingsComponentEmbeddable {

    JPanel getMainPanel();

    /**
     * Use this method to initialize the content of the settings component (e.g. setting action listener)
     */
    void initialize();

}
