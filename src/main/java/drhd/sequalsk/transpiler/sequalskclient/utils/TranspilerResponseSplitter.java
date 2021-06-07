package drhd.sequalsk.transpiler.sequalskclient.utils;

import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspiledFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes the transpiled string (which contains all files as single string) and splits it into several
 * {@link TranspiledFile}s.
 */
public class TranspilerResponseSplitter {

    private final TranspilerContext context;

    private TranspiledFile mainFile;
    private final List<TranspiledFile> additionalFiles;

    public TranspilerResponseSplitter(TranspilerContext context) {
        this.context = context;
        additionalFiles = new ArrayList<>();
    }

    public void split(String transpiledCode) {

        /* split transpiled code into array: [fileinfo][code][fileinfo][code]... */
        String[] parts = transpiledCode.split("(\\/{2}<\\+{10}>)");

        /* the first part is always an empty string -> start at index 1 */
        for (int i = 1; i < parts.length; i+=2) {
            String name = getName(parts[i]);
            String path = getPath(parts[i]);
            String content = parts[i + 1];
            while (content.startsWith("\n")) content = content.replaceFirst("\n", "");

            TranspiledFile file = new TranspiledFile(
                    getOriginalFile(name, path, context),
                    content
            );

            if (isMainFile(file)) mainFile = file;
            else additionalFiles.add(file);
        }

        /* validate result and add dummy files if necessary */
        if(mainFile == null) {
            mainFile = new TranspiledFile(context.getMainFile(), "-");
        }

        for(TranspilerRequestFile requestFile : context.getAdditionalFiles()) {
            // if additional files does not contain that file add it

            boolean hasFile = false;
            for(TranspiledFile transpiledFile : additionalFiles) {
                hasFile = transpiledFile.getOriginalFile().getPath().equals(requestFile.getPath());
                if(hasFile) break;
            }

            if(!hasFile) additionalFiles.add(new TranspiledFile(requestFile, "-"));
        }

    }

    /** Extracts the name of a file from the separator */
    private String getName(String separatorInfo) {
        String[] parts = separatorInfo.split("\\|\\|");
        return parts[0];
    }

    /** Extracts the path of a file from the separator */
    private String getPath(String separatorInfo) {
        String[] parts = separatorInfo.split("\\|\\|");
        return parts[1];
    }

    /**
     * Returns a file from the context that matches the given name and path. Helper method to link a transpiled file
     * to the original file.
     *
     * @param name name of the transpiled file
     * @param path path of the transpiled file
     * @param context the context containing the original file
     */
    private TranspilerRequestFile getOriginalFile(String name, String path, TranspilerContext context) {

        for(TranspilerRequestFile transpilerFile : context.getAllFiles()) {
            String originalPath = transpilerFile.getPath();
            String originalName = transpilerFile.getName();
            if(originalName.equals(name) && originalPath.equals(path)) return transpilerFile;
        }

        throw new RuntimeException("Could not find original file for file " + name + " with path " + path);
    }

    /** Checks if the transpiled file is the main file of the context. */
    private boolean isMainFile(TranspiledFile transpiledFile) {
        String mainFileName = context.getMainFile().getName();
        String mainFilePath = context.getMainFile().getPath();

        String transpiledFileName = transpiledFile.getOriginalFile().getName();
        String transpiledFilePath = transpiledFile.getOriginalFile().getPath();

        return mainFileName.equals(transpiledFileName) && transpiledFilePath.equals(mainFilePath);
    }

    public TranspiledFile getMainFile() {
        return mainFile;
    }

    public List<TranspiledFile> getAdditionalFiles() {
        return additionalFiles;
    }
}
