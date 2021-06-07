package drhd.sequalsk.transpiler.sequalskclient.result;

import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;

/**
 * Wraps a {@link TranspilerRequestFile} and contains the transpiled content of that file.
 */
public class TranspiledFile {

    /**
     * The original file that had to be transpiled
     */
    private final TranspilerRequestFile originalFile;

    /**
     * The transpiled content of the original file
     */
    private final String transpiledContent;

    /** {@link TranspiledFile} */
    public TranspiledFile(TranspilerRequestFile originalFile, String transpiledContent) {
        this.originalFile = originalFile;
        this.transpiledContent = transpiledContent;
    }

    /**
     * @see TranspiledFile#originalFile
     */
    public TranspilerRequestFile getOriginalFile() {
        return originalFile;
    }

    /**
     * @see TranspiledFile#transpiledContent
     */
    public String getTranspiledContent() {
        return transpiledContent;
    }

}
