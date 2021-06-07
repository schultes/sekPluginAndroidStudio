package drhd.sequalsk.transpiler.utils;

import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.utils.MetaDataHolder;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerLanguage;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import java.util.HashMap;

/**
 * Provides a simple way to generate {@link TranspilerRequest}s.
 */
public class TranspilerRequestBuilder {

    private final TranspilerContext context;
    private TranspilerLanguage inputLanguage = TranspilerLanguage.KOTLIN;
    private TranspilerRequestMode transpilerRequestMode = PluginSettingsHelper.TranspilerSettings.requestMode();
    private final HashMap<String, Object> tags = new HashMap<>();

    /**
     * Start the creation of a new {@link TranspilerRequest}. Default values: kotlin to swift; request type and
     * transpiler mode as configured in the plugin settings.
     */
    public TranspilerRequestBuilder(TranspilerContext context) {
        this.context = context;
    }

    /**
     * Set kotlin as input language.
     */
    public TranspilerRequestBuilder kotlinToSwift() {
        this.transpilerRequestMode = TranspilerRequestMode.ONE_WAY;
        this.inputLanguage = TranspilerLanguage.KOTLIN;
        return this;
    }

    /**
     * Set swift as input language.
     */
    public TranspilerRequestBuilder swiftToKotlin() {
        this.transpilerRequestMode = TranspilerRequestMode.ONE_WAY;
        this.inputLanguage = TranspilerLanguage.SWIFT;
        return this;
    }

    /**
     * Set the request type to two-way.
     */
    public TranspilerRequestBuilder twoWay() {
        this.inputLanguage = TranspilerLanguage.KOTLIN;
        this.transpilerRequestMode = TranspilerRequestMode.TWO_WAY;
        return this;
    }

    /**
     * Set the type of request.
     */
    public TranspilerRequestBuilder type(TranspilerRequestMode requestMode) {
        if (requestMode == TranspilerRequestMode.TWO_WAY) this.twoWay();
        return this;
    }

    /**
     * Adds a tag to the request.
     * @see MetaDataHolder
     */
    public TranspilerRequestBuilder tag(String key, Object value) {
        this.tags.put(key, value);
        return this;
    }

    /**
     * Set the the type of context.
     */
    public TranspilerRequestBuilder contentType(TranspilerContextType content) {
        this.tags.put(TranspilerContextType.TAG_KEY, content.name());
        return this;
    }

    /**
     * Converts all provided information to a transpiler request.
     */
    public TranspilerRequest build() {
        TranspilerRequest transpilerRequest = new TranspilerRequest(
                context,
                inputLanguage,
                transpilerRequestMode
        );
        transpilerRequest.setTags(tags);
        return transpilerRequest;
    }
    
}
