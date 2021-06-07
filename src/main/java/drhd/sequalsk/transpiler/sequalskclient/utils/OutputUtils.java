package drhd.sequalsk.transpiler.sequalskclient.utils;

import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspiledFile;

import java.util.ArrayList;
import java.util.List;

public class OutputUtils {

    public static String printContext(TranspilerContext context) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("### Number of files\n");
        stringBuilder.append(context.getAllFiles().size());
        stringBuilder.append("\n\n### Main File");
        stringBuilder.append("\n").append(context.getMainFile().getName());
        stringBuilder.append("\n").append(context.getMainFile().getPath());
        stringBuilder.append("\n\n### Referenced Files\n\n");

        List<TranspilerRequestFile> transpiledFileList = context.getAdditionalFiles();
        stringBuilder.append(OutputUtils.printRequestFiles(transpiledFileList));

        return stringBuilder.toString();
    }

    public static String printRequestFiles(List<TranspilerRequestFile> files) {
        List<String> referencedFiles = new ArrayList<>();
        files.forEach(virtualFile -> {
                    String name = virtualFile.getName();
                    String path = virtualFile.getPath();
                    String combined = name + "\t" + path;
                    referencedFiles.add(combined);
                }
        );
        return String.join("\n", referencedFiles);
    }

    public static String printTranspiledFiles(List<TranspiledFile> files) {
        List<TranspilerRequestFile> requestFiles = new ArrayList<>();
        files.forEach(file -> requestFiles.add(file.getOriginalFile()));
        return OutputUtils.printRequestFiles(requestFiles);
    }

}
