package drhd.sequalsk.platform.services.settings.application;

import com.intellij.openapi.options.Configurable;
import drhd.sequalsk.platform.services.settings.application.views.ApplicationSettingsPanel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class ApplicationSettingsConfigurable implements Configurable {

    private ApplicationSettingsPanel settingsPanel;

    @Override
    public String getDisplayName() {
        return "SequalsK Settings (Application)";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return settingsPanel.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsPanel = new ApplicationSettingsPanel();
        return settingsPanel.createPanel();
    }

    @Override
    public boolean isModified() {
        ApplicationSettingsService settingsState = ApplicationSettingsService.getInstance();

        boolean modified = false;

        // online transpiler configuration
        modified |= !settingsPanel.onlineTranspilerSettings().getHost().equals(settingsState.getHost());
        modified |= !settingsPanel.onlineTranspilerSettings().getRequestPath().equals(settingsState.getRequestPath());
        modified |= !settingsPanel.onlineTranspilerSettings().getRequestHeaders().equals(settingsState.getRequestHeaders());
        modified |= !settingsPanel.onlineTranspilerSettings().getTimeout().equals(settingsState.getTimeout());

        return modified;
    }

    @Override
    public void apply() {
        ApplicationSettingsService settings = ApplicationSettingsService.getInstance();

        // online transpiler configuration
        settings.setHost(settingsPanel.onlineTranspilerSettings().getHost());
        settings.setRequestPath(settingsPanel.onlineTranspilerSettings().getRequestPath());
        settings.setRequestHeaders(settingsPanel.onlineTranspilerSettings().getRequestHeaders());
        settings.setTimeout(settingsPanel.onlineTranspilerSettings().getTimeout());
    }

    @Override
    public void reset() {
        ApplicationSettingsService settings = ApplicationSettingsService.getInstance();

        // online transpiler configuration
        settingsPanel.onlineTranspilerSettings().setHost(settings.getHost());
        settingsPanel.onlineTranspilerSettings().setRequestPath(settings.getRequestPath());
        settingsPanel.onlineTranspilerSettings().setRequestHeaders(settings.getRequestHeaders());
        settingsPanel.onlineTranspilerSettings().setTimeout(settings.getTimeout());
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }
}
