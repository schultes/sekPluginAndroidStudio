package drhd.sequalsk.transpiler.utils;

import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;

public class TranspilerRequestUtils {

    /**
     * Returns true if the context of the request contains the tag {@link TranspilerContextType#SELECTION}
     */
    public static boolean isSelectionRequest(TranspilerRequest request) {
        return request.getContext().getTag(TranspilerContextType.TAG_KEY).equals(TranspilerContextType.SELECTION);
    }

    /**
     * Returns true if the context of the request contains the tag {@link TranspilerContextType#FILE}
     */
    public static boolean isFileRequest(TranspilerRequest request) {
        return request.getContext().getTag(TranspilerContextType.TAG_KEY).equals(TranspilerContextType.FILE);
    }

    /**
     * Returns true if the context of the request contains the tag {@link TranspilerContextType#DIRECTORY}
     */
    public static boolean isDirectoryJob(TranspilerRequest request) {
        return request.getContext().getTag(TranspilerContextType.TAG_KEY).equals(TranspilerContextType.DIRECTORY);
    }

    /**
     * Returns true if the context of the request contains the tag {@link TranspilerContextType#PROJECT}
     */
    public static boolean isProjectJob(TranspilerRequest request) {
        return request.getContext().getTag(TranspilerContextType.TAG_KEY).equals(TranspilerContextType.PROJECT);
    }

}
