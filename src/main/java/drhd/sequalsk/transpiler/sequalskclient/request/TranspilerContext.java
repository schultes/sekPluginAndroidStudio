package drhd.sequalsk.transpiler.sequalskclient.request;

import drhd.sequalsk.transpiler.sequalskclient.utils.MetaDataHolderImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The context contains all files that must be transpiled in a single {@link TranspilerRequest}.
 *
 * In many cases it is not sufficient to translate a single file/class on its own ("main file").
 * The files that this file has references to must also be handed over to the transpiler ("additional files").
 *
 * For this purpose the context converts all files to a single string that in turn is passed to the transpiler.
 */
public class TranspilerContext extends MetaDataHolderImpl {

    /**
     * The main file that the request is all about.
     */
    private final TranspilerRequestFile mainFile;

    /**
     * All referenced (additional) files that are needed to transpile the main file successfully.
     */
    private final List<TranspilerRequestFile> additionalFiles;

    public TranspilerContext(TranspilerRequestFile mainFile) {
        this.mainFile = mainFile;
        additionalFiles = new ArrayList<>();
    }

    public TranspilerContext(List<TranspilerRequestFile> allFiles) {
        this.mainFile = allFiles.get(0);
        allFiles.remove(0);
        additionalFiles = allFiles;
        removeFilesThatShouldBeIgnored();
    }

    public TranspilerContext(TranspilerRequestFile mainFile, List<TranspilerRequestFile> additionalFiles) {
        this.mainFile = mainFile;
        this.additionalFiles = additionalFiles;
        removeFilesThatShouldBeIgnored();
    }

    public TranspilerContext(TranspilerRequestFile mainFile, TranspilerRequestFile... additionalFiles) {
        this.mainFile = mainFile;
        this.additionalFiles = Arrays.asList(additionalFiles);
        removeFilesThatShouldBeIgnored();
    }

    private void removeFilesThatShouldBeIgnored() {
        additionalFiles.removeIf(file -> file.getName().equals("swiftSupportInKotlin.kt"));
    }

    /** @see TranspilerContext#mainFile */
    public TranspilerRequestFile getMainFile() {
        return mainFile;
    }

    /** @see TranspilerContext#additionalFiles */
    public List<TranspilerRequestFile> getAdditionalFiles() {
        return additionalFiles;
    }

    /**
     * Returns the main file and all additional files in a single list.
     */
    public List<TranspilerRequestFile> getAllFiles() {
        ArrayList<TranspilerRequestFile> allFiles = new ArrayList<>();
        if(mainFile != null) allFiles.add(mainFile);
        allFiles.addAll(additionalFiles);
        return allFiles;
    }

    /**
     * Returns a file by its path and name.
     */
    public TranspilerRequestFile getFile(String pathWithNameAndExtension) {
        for(TranspilerRequestFile transpilerFile : getAllFiles()) {
            if(transpilerFile.getPath().equals(pathWithNameAndExtension)) return transpilerFile;
        }
        return null;
    }

    /**
     * Converts all files from {@link TranspilerContext#getAllFiles()} ()} into the string of a {@link TranspilerRequest}
     * that will be passed to the transpiler.
     *
     * @return all files as String. The files are separated by separators to be able to split the result back into files.
     */
    public String asSingleContent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TranspilerRequestFile file : getAllFiles()) {
            stringBuilder.append(file.getContentWithSeparator());
        }
        return stringBuilder.toString();
    }

}
