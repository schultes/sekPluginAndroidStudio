package drhd.sequalsk.transpiler.views;


import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerConfiguration;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerLanguage;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;

/**
 * Form to show the details of a transpiler configuration.
 */
public class TranspilerConfigPanel {

    private TranspilerConfiguration config;

    private JLabel labelHost;
    private JLabel labelTimeout;
    private JLabel labelRequestHeaders;
    private JLabel labelPostRequestPath;
    private JPanel mainPanel;
    private JLabel labelExampleUrl;

    public TranspilerConfigPanel() {}

    public TranspilerConfigPanel(TranspilerConfiguration config) {
        this.config = config;
        setConfigValues();
    }

    public void setTranspilerConfiguration(@NotNull TranspilerConfiguration config) {
        this.config = config;
        setConfigValues();
    }

    private void setConfigValues() {
        labelHost.setText(config.getHost());
        labelPostRequestPath.setText(config.getPostRequestPath());
        labelTimeout.setText(config.getTimeoutInSeconds() + "");
        labelRequestHeaders.setText(config.getRequestHeaders());
        labelExampleUrl.setText(TranspilerConfiguration.generateRequestUrl(config, TranspilerLanguage.KOTLIN));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
