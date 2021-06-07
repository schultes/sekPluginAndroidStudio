package drhd.sequalsk.platform.services.settings.application.views;

import drhd.sequalsk.platform.services.settings.SettingsComponentEmbeddable;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.utils.SimpleDocumentListener;
import drhd.sequalsk.transpiler.views.OnlineTranspilerTestDialog;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;

public class OnlineTranspilerSettingsPanel implements SettingsComponentEmbeddable {

    private JPanel mainPanel;
    private JTextField textFieldHost;
    private JTextField textFieldRequestHeader;
    private JTextField textFieldRequestPath;
    private JLabel labelFullRequestPath;
    private JSpinner spinnerTimeout;
    private JButton testTranspilerButton;

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void initialize() {
        spinnerTimeout.setModel(new SpinnerNumberModel(1, 1, 20, 1));

        textFieldHost.getDocument().addDocumentListener((SimpleDocumentListener) event -> updateExampleUrl());
        textFieldRequestPath.getDocument().addDocumentListener((SimpleDocumentListener) event -> updateExampleUrl());

        testTranspilerButton.addActionListener(e -> new OnlineTranspilerTestDialog().show());
    }

    public String getHost() {
        return textFieldHost.getText();
    }

    public void setHost(@NotNull String value) {
        textFieldHost.setText(value);
    }

    public String getRequestHeaders() {
        return textFieldRequestHeader.getText();
    }

    public void setRequestHeaders(@NotNull String value) {
        textFieldRequestHeader.setText(value);
    }

    public String getRequestPath() {
        return textFieldRequestPath.getText();
    }

    public void setRequestPath(String postRequestPath) {
        textFieldRequestPath.setText(postRequestPath);
        labelFullRequestPath.setText(PluginSettingsHelper.OnlineTranspilerSettings.host() + postRequestPath);
    }

    public Integer getTimeout() {
        return (int) spinnerTimeout.getModel().getValue();
    }

    public void setTimeout(Integer timeout) {
        spinnerTimeout.getModel().setValue(timeout);
    }

    private void updateExampleUrl() {
        String host = getHost();
        String postRequestPath = getRequestPath();
        String fullPath = host + postRequestPath;
        labelFullRequestPath.setText(fullPath);
    }
}
