package drhd.sequalsk.transpiler.sequalskclient.utils;

import drhd.sequalsk.transpiler.sequalskclient.SequalskClient;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerLanguage;

/**
 * Contains information for the {@link SequalskClient} to process a {@link TranspilerRequest}.
 */
public class TranspilerConfiguration {

    private final String host;
    private final String postRequestPath;
    private final Integer timeoutInSeconds;
    private final String requestHeaders;

    public TranspilerConfiguration(String host, String postRequestPath, int timeoutInSeconds, String requestHeaders) {
        this.host = host;
        this.postRequestPath = postRequestPath;
        this.timeoutInSeconds = timeoutInSeconds;
        this.requestHeaders = requestHeaders;
    }

    public String getHost() {
        return host;
    }

    public String getPostRequestPath() {
        return postRequestPath;
    }

    public Integer getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public static String generateRequestUrl(TranspilerConfiguration config, TranspilerLanguage language) {
        String url = config.getHost() + config.getPostRequestPath() + "/?input=" + language.getName();
        if (language == TranspilerLanguage.KOTLIN) url += "&feedback=true";
        return url;
    }
}
