package drhd.sequalsk.transpiler.context.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.AccurateReferenceResolver;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.FastReferenceResolver;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.FileReferenceResolver;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.sequalskclient.utils.MetaDataHolder;
import drhd.sequalsk.transpiler.sequalskclient.utils.MetaDataHolderImpl;
import drhd.sequalsk.transpiler.utils.TranspilerContextType;
import drhd.sequalsk.utils.PluginVirtualFileUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Generates a {@link TranspilerContext} that contains all files (and their referenced files) of a directory.
 */
public class DirectoryContextFactory extends TranspilerContextFactory {

    /** The directory that the context is created for. */
    protected final VirtualFile directory;

    /**
     * Shows wether the file is in the input directory or a referenced file in another directory.
     * @see MetaDataHolder
     * @see MetaDataHolderImpl
     */
    public static final String IS_DIRECTORY_FILE_TAG = "IS_DIRECTORY_FILE_TAG";

    public DirectoryContextFactory(Project project, VirtualFile directory) {
        super(project);
        this.directory = directory;
    }

    @Override
    protected TranspilerContextType getContextType() {
        return TranspilerContextType.DIRECTORY;
    }

    @Override
    protected TranspilerContext buildContext() {
        List<VirtualFile> directoryFiles = PluginVirtualFileUtils.getVirtualKtFiles(project, directory);
        HashMap<String, VirtualFile> contextFiles = new HashMap<>();

        for(VirtualFile virtualFile : directoryFiles) {
            contextFiles.put(virtualFile.getPath(), virtualFile);
            FileReferenceResolver resolver = getPsiFileReferenceResolver(virtualFile);
            for(VirtualFile referencedFile : resolver.getReferencedFiles()) {
                contextFiles.put(referencedFile.getPath(), referencedFile);
            }
        }

        return new TranspilerContext(
                asTranspilerFiles(new ArrayList<>(contextFiles.values())));
    }

    @Override
    protected List<TranspilerRequestFile> asTranspilerFiles(List<VirtualFile> contextFiles) {
        List<TranspilerRequestFile> requestFiles = new ArrayList<>();
        for(VirtualFile virtualFile : contextFiles) {
            TranspilerRequestFile requestFile = asTranspilerFile(virtualFile);
            requestFile.addTag(IS_DIRECTORY_FILE_TAG, isDirectoryFile(virtualFile));
            requestFiles.add(requestFile);
        }
        return requestFiles;
    }

    /**
     * Returns true if the given file is part of the input {@link #directory}.
     */
    private Boolean isDirectoryFile(VirtualFile virtualFile) {
        String inputPath = directory.getPath();
        String filePath = virtualFile.getPath();
        return filePath.startsWith(inputPath + "/");
    }

    /**
     * Depending on the configured {@link FileContextMode} this method returns the corresponding implementation of
     * the {@link FileReferenceResolver}.
     */
    protected FileReferenceResolver getPsiFileReferenceResolver(VirtualFile virtualFile) {
        FileReferenceResolver resolver = new FastReferenceResolver(project, virtualFile);

        FileContextMode mode = PluginSettingsHelper.TranspilerSettings.fileContextMode();

        if(mode == FileContextMode.REFERENCES_ACCURATE) {
            resolver = new AccurateReferenceResolver(project, virtualFile);
        }

        return resolver;
    }
}
