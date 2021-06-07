package drhd.sequalsk.transpiler.utils;

import drhd.sequalsk.transpiler.context.factory.TranspilerContextFactory;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.utils.MetaDataHolder;
import drhd.sequalsk.transpiler.sequalskclient.utils.MetaDataHolderImpl;

/**
 * There are four types of possible {@link TranspilerContext} objects: <ul>
 * <li>Selection: The request transpiles only a selection of a single file (+ referenced files)</li>
 * <li>File: The request transpiles a single file (+ referenced files)</li>
 * <li>Directory: The request transpiles a directory (+ referenced files)</li>
 * <li>Project: The request contains the whole project</li>
 * </ul>
 * <p>
 * Added to the meta data of the context by the {@link TranspilerContextFactory} by reading it from the context.
 */
public enum TranspilerContextType {

    /**
     * The request transpiles only a selection of a single file (+ referenced files)
     */
    SELECTION,

    /**
     * The request transpiles a single file (+ referenced files)
     */
    FILE,

    /**
     * The request transpiles a directory (+ referenced files). Note that the main file is a random file.
     */
    DIRECTORY,

    /**
     * The request contains the whole project. Note that the main file is a random file.
     */
    PROJECT;

    /**
     * The key that is used for the tag at the request.
     *
     * @see MetaDataHolder
     * @see MetaDataHolderImpl
     */
    public static final String TAG_KEY = "transpiler_request_type";

}
