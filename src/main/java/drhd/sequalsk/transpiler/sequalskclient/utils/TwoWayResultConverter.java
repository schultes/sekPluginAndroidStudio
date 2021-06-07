package drhd.sequalsk.transpiler.sequalskclient.utils;

import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspiledFile;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Helper for the execution of a two-way-request. The two-way-request consists of two requests: Kotlin->Swift and
 * Swift->Kotlin. This class converts the first result to the second request.  */
public class TwoWayResultConverter {

    public static TranspilerContext convertInterimResult(TranspilerResult interimResult) {

        TranspiledFile interimMainFile = interimResult.getMainFile();
        TranspilerRequestFile mainFile = convertFile(interimMainFile);

        List<TranspilerRequestFile> additionalFiles = new ArrayList<>();
        for(TranspiledFile transpiledFile : interimResult.getAdditionalFiles()) {
            additionalFiles.add(convertFile(transpiledFile));
        }

        return new TranspilerContext(mainFile, additionalFiles);
    }

    private static TranspilerRequestFile convertFile(TranspiledFile transpiledFile) {
        return new TranspilerRequestFile(
                transpiledFile.getOriginalFile().getPath(),
                transpiledFile.getOriginalFile().getName(),
                transpiledFile.getTranspiledContent()
        );
    }

    /**
     * The transpiled files of the final result have the problem that the original content is the swift-content of the interim result.
     * So the final result has to be converted to be connected with the original kotlin content.
     */
    public static TranspilerResult convertFinalResult(TranspilerResult transpilerResult, TranspilerRequest originalRequest) {
        TranspilerContext context = originalRequest.getContext();

        TranspiledFile transpiledMainFileWithSwiftContent = transpilerResult.getMainFile();
        TranspilerRequestFile originalMainFileWithKtContent = originalRequest.getContext().getMainFile();

        List<TranspiledFile> transpiledFilesWithSwiftContent = new ArrayList<>(transpilerResult.getAdditionalFiles());
        List<TranspilerRequestFile> originalFilesWithKtContent = new ArrayList<>(originalRequest.getContext().getAdditionalFiles());

        TranspiledFile finalMainFile = new TranspiledFile(
                originalMainFileWithKtContent,
                transpiledMainFileWithSwiftContent.getTranspiledContent()
        );

        List<TranspiledFile> finalTranspiledFiles = new ArrayList<>(); // list that has the transpiled files with kotlin code as original content
        for(TranspiledFile transpiledFile : transpiledFilesWithSwiftContent) {
            TranspilerRequestFile originalFile = originalFilesWithKtContent
                    .stream()
                    .filter(transpilerRequestFile -> transpilerRequestFile.getPath().equals(transpiledFile.getOriginalFile().getPath()))
                    .collect(Collectors.toList()).get(0);
            finalTranspiledFiles.add(new TranspiledFile(originalFile, transpiledFile.getTranspiledContent()));
        }
        return new TranspilerResult(
                originalRequest,
                finalMainFile,
                finalTranspiledFiles,
                transpilerResult.getTranspilerResponse()
        );
    }
}
