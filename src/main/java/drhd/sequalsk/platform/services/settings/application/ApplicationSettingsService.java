package drhd.sequalsk.platform.services.settings.application;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import drhd.sequalsk.platform.services.settings.PluginSettingsDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * From Jetbrains Documentation: Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "settings.SequalskSettingsState",
        storages = {@Storage("Sequalsk.PluginSettings.xml")}
)
public class ApplicationSettingsService implements PersistentStateComponent<ApplicationSettingsService> {

    private String host = PluginSettingsDefaults.host;
    private String requestPath = PluginSettingsDefaults.requestPath;
    private String requestHeaders = PluginSettingsDefaults.requestHeaders;
    private Integer timeout = PluginSettingsDefaults.timeout;

    public static ApplicationSettingsService getInstance() {
        return ServiceManager.getService(ApplicationSettingsService.class);
    }

    @Nullable
    @Override
    public ApplicationSettingsService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ApplicationSettingsService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}