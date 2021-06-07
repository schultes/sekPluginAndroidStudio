package drhd.sequalsk.transpiler.validation.utils;

import drhd.sequalsk.transpiler.sequalskclient.result.TranspiledFile;

/**
 * Comparator that checks if the transpiled content of a {@link TranspiledFile} is a clone of the original code.
 */
public class TranspiledFileComparator {

    public static boolean isEqual(TranspiledFile transpiledFile) {

        String transpiledContent = transpiledFile.getTranspiledContent();
        String originalContent = transpiledFile.getOriginalFile().getContent();

        transpiledContent = removeImports(transpiledContent);
        originalContent = removeImports(originalContent);

        transpiledContent = removeWhitespacesAndLinebreaks(transpiledContent);
        originalContent = removeWhitespacesAndLinebreaks(originalContent);

        return transpiledContent.equals(originalContent);
    }

    private static String removeWhitespacesAndLinebreaks(String input) {
        return input.replaceAll("[\\n\\t ]", "");
    }

    private static String removeImports(String input) {
        return input.replaceAll("(import)[ ](.*)[\n\t ]", "");
    }

}
