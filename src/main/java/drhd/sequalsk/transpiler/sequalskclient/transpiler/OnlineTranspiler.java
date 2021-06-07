package drhd.sequalsk.transpiler.sequalskclient.transpiler;

import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerConfiguration;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerException;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Implements the online mode.
 */
public class OnlineTranspiler implements SequalskTranspiler {

    private TranspilerConfiguration config;

    @Override
    public boolean validateConfiguration(TranspilerConfiguration config) {
        return config != null && config.getHost() != null && !config.getHost().isEmpty();
    }

    @Override
    public String transpile(TranspilerRequest request, TranspilerConfiguration config) throws TranspilerException {
        try {
            this.config = config;

            OkHttpClient httpClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(PluginSettingsHelper.OnlineTranspilerSettings.timeoutInSeconds(), TimeUnit.SECONDS)
                    .readTimeout(PluginSettingsHelper.OnlineTranspilerSettings.timeoutInSeconds(), TimeUnit.SECONDS)
                    .build();

            Response response = httpClient.newCall(this.buildHttpRequest(request)).execute();
            if(!response.isSuccessful() || response.body() == null) throw new TranspilerException("Request was not successful", config, request);

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            throw new TranspilerException(e, config, request);
        }
    }

    private Request buildHttpRequest(TranspilerRequest request) {
        String url = TranspilerConfiguration.generateRequestUrl(config, request.getInputLanguage());
        String requestBodyContent = request.getContext().asSingleContent();
        RequestBody body = RequestBody.create(requestBodyContent, MediaType.parse("application/json"));

        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);

        // request headers
        String requestHeaders = config.getRequestHeaders();
        if (!requestHeaders.isEmpty()) {
            String[] headers = requestHeaders.split(";");
            for (String header : headers) {
                String name = header.split(":")[0];
                String value = header.split(":")[1];
                requestBuilder.addHeader(name, value);
            }
        }

        return requestBuilder.build();
    }
}