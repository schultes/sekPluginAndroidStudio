package drhd.sequalsk.platform.services.transpiler.utils;

import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.platform.services.transpiler.TranspilerServiceCache;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.utils.debugging.DebugLogger;
import java.util.*;

/**
 * Helper class for the {@link TranspilerServiceCache}. Basically just a wrapper for a HashMap that stores several
 * transpiler results. Provides convenience methods to add/remove results to the list of cached results.
 */
public class CachedTranspilerResultList {

    /** Contains all cached results that are identified by the path of the main file. */
    private final HashMap<String, TranspilerResult> map = new HashMap<>();

    /**
     * Adds a result to the list.
     */
    public void add(TranspilerResult transpilerResult) {
        map.put(transpilerResult.getMainFile().getOriginalFile().getPath(), transpilerResult);
    }

    /**
     * Reads (but does not remove) the result for the given request or returns null if no result is available.
     */
    public TranspilerResult get(TranspilerRequest request) {
        return map.get(request.getContext().getMainFile().getPath());
    }

    /**
     * Reads (but does not remove) the result that has the passed file as main file.
     */
    public TranspilerResult get(VirtualFile mainFile) {
        for (TranspilerResult result : map.values()) {
            if (hasFileAsMainFile(result, mainFile)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Removes the result whose main file is equal to the passed path.
     */
    public void remove(String mainFilePath) {
        if(map.containsKey(mainFilePath)) {
            DebugLogger.info(this, "Removing " + mainFilePath + " from cache");
        }
        map.remove(mainFilePath);
    }

    /**
     * Removes all results.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Checks if the main file of the given result is equal to the path of the virtual file.
     */
    private boolean hasFileAsMainFile(TranspilerResult result, VirtualFile virtualFile) {
        return hasFileAsMainFile(result, virtualFile.getPath());
    }

    /**
     * Checks if the main file of the given result is equal to the passed path.
     */
    private boolean hasFileAsMainFile(TranspilerResult result, String filepath) {
        return result.getTranspilerContext().getMainFile().getPath().equals(filepath);
    }

}
