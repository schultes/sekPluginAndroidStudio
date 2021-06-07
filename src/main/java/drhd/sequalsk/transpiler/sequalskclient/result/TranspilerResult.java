package drhd.sequalsk.transpiler.sequalskclient.result;


import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedback;
import drhd.sequalsk.transpiler.sequalskclient.feedback.DummyFeedbackProvider;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the transpiled content of a {@link TranspilerRequest}.
 */
public class TranspilerResult {

    /**
     * The request for which this result was generated.
     */
    private final TranspilerRequest request;

    /**
     * The transpiler output - text/result that was received from the transpiler as single string
     */
    private final String transpilerResponse;

    /**
     * The transpiled main file of the context {@link TranspilerContext#getMainFile()}
     */
    private final TranspiledFile mainFile;

    /**
     * All transpiled additional files of the context {@link TranspilerContext#getAdditionalFiles()}
     */
    private final List<TranspiledFile> additionalFiles;

    /** {@link TranspilerResult} */
    public TranspilerResult(
            TranspilerRequest request,
            TranspiledFile mainFile,
            List<TranspiledFile> additionalFiles,
            String transpilerResponse
    ) {
        this.request = request;
        this.mainFile = mainFile;
        this.additionalFiles = additionalFiles;
        this.transpilerResponse = transpilerResponse;
    }

    /**
     * Returns the (input) code that was originally sent to the transpiler (as single string).
     * {@link TranspilerContext#asSingleContent()}
     */
    public String getOriginalCode() {
        return request.getContext().asSingleContent();
    }

    /**
     * Returns the context of the request.
     */
    public TranspilerContext getTranspilerContext() {
        return request.getContext();
    }

    /**
     * Returns the (output) code that was received from the transpiler (as single string).
     * Contains all transpiled files as single string.
     */
    public String getTranspilerResponse() {
        return transpilerResponse;
    }

    /** @see TranspilerResult#request */
    public TranspilerRequest getRequest() {
        return request;
    }

    /** @see TranspilerResult#mainFile */
    public TranspiledFile getMainFile() {
        return mainFile;
    }

    /** @see TranspilerResult#additionalFiles */
    public List<TranspiledFile> getAdditionalFiles() {
        return additionalFiles;
    }

    /**
     * Returns all transpiled files as list (main file & additional files)
     */
    public List<TranspiledFile> getAllFiles() {
        ArrayList<TranspiledFile> allFiles = new ArrayList<>();
        if(mainFile != null) allFiles.add(mainFile);
        allFiles.addAll(additionalFiles);
        return allFiles;
    }

    /**
     * Converted feedback received by the SequalsK-Transpiler.
     */
    public TranspilerFeedback getTranspilerFeedback() {
        return DummyFeedbackProvider.generateFeedback();
    }

    @Override
    public String toString() {
        return "TranspilerResult for " + getRequest().toString();
    }

}
