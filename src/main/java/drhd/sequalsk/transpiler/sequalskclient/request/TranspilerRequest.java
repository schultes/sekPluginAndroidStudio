package drhd.sequalsk.transpiler.sequalskclient.request;

import drhd.sequalsk.transpiler.sequalskclient.SequalskClient;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.sequalskclient.utils.MetaDataHolderImpl;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerLanguage;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;
import java.util.HashMap;

/**
 * Object that contains all the information needed to transpile code. Is passed, transpiled and converted to
 * a {@link TranspilerResult} by the {@link SequalskClient}.
 */
public class TranspilerRequest extends MetaDataHolderImpl {

    /** The context of the request which contains all files that must be transpiled. */
    private final TranspilerContext context;

    /** The language (kotlin/swift) of the code that is going to be transpiled. */
    private final TranspilerLanguage inputLanguage;

    /** Determines whether a one-way or two-way request will be executed by the transpiler. */
    private final TranspilerRequestMode requestMode;

    /** {@link TranspilerRequest} */
    public TranspilerRequest(
            TranspilerContext context,
            TranspilerLanguage inputLanguage,
            TranspilerRequestMode requestMode
    ) {
        this.context = context;
        this.inputLanguage = inputLanguage;
        this.requestMode = requestMode;
        this.tags = new HashMap<>();
    }

    /**
     * Returns the programming language (kotlin/swift) of the code that should be transpiled.
     */
    public TranspilerLanguage getInputLanguage() {
        return inputLanguage;
    }

    /**
     * Determines whether a one-way or two-way request should be executed by the transpiler.
     */
    public TranspilerRequestMode getRequestMode() {
        return requestMode;
    }

    /**
     * Returns the context of the request which contains the content that needs to be transpiled.
     */
    public TranspilerContext getContext() {
        return context;
    }

    /**
     * Returns true if the request should be executed as two-way request.
     */
    public boolean isTwoWayRequest() {
        return requestMode == TranspilerRequestMode.TWO_WAY;
    }

    @Override
    public String toString() {
        return "TranspilerRequest for file " + context.getMainFile().getName() + " with " + getContext().getAdditionalFiles().size() + " referenced files";
    }
}