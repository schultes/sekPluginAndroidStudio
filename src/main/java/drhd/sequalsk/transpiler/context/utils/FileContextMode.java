package drhd.sequalsk.transpiler.context.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import drhd.sequalsk.transpiler.context.factory.FileContextFactory;
import drhd.sequalsk.transpiler.context.factory.SelectionContextFactory;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.PossibleFileReference;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;

/**
 * Defines how the {@link FileContextFactory} and {@link SelectionContextFactory} generate the {@link TranspilerContext}
 * for a {@link TranspilerRequest}. Depending on the chosen mode the additional files of the context can vary.
 * <p>
 * There are two ways of building a context for a file (or selection of a file): Either the whole project is used as
 * context or the file is analyzed based on its PSI-Tree to generate a list of all referenced files. The analysis of the
 * psi tree can be done in two ways: slow (but very accurate) or fast (very fast, could lead to edge-case-errors).
 * <p>
 * See {@link #PROJECT}, {@link #REFERENCES_ACCURATE} and {@link #REFERENCES_FAST} for details about the effects of the
 * modes on generating the context.
 */
public enum FileContextMode {

    /**
     * The whole project (all kotlin files that are member of the project) are passed as referenced files to the {@link TranspilerContext}
     */
    PROJECT,

    /**
     * All PsiElements of the file are analyzed for PsiReferences. All those PsiReferences are actually resolved to
     * their target-elements by {@link PsiReference#resolve()}. A list of referenced files is created by calling
     * {@link PsiElement#getContainingFile()} on the resolved target element.
     * This ensures that absolutely every referenced file is present, but at the cost of performance.
     * <p>
     * Although some performance increasing features are implemented (blacklist for kotlin keywords & caching of already
     * resolved referenced names) this is usually slower than the fast-mode (especially on slow computers).
     */
    REFERENCES_ACCURATE,

    /**
     * All PsiElements of the file are analyzed for PsiReferences. Unlike the accurate mode those references are NOT
     * resolved to their target elements by calling {@link PsiReference#resolve()}. Instead this mode works with
     * the assumption that most referenced names also correspond to the file names (e.g. constructor calls).
     * Example: var obj = SomeClass() - must refer to SomeClass.kt to find the file.
     * <p>
     * These assumptions are generated to {@link PossibleFileReference}s whose wrapped filenames are resolved to files.
     * <p>
     * Only in case of not found or duplicate filenames the references are actually resolved.
     * <p>
     * Although this mode does work pretty reliable - some edge cases where referenced files are not found cannot be 100% ruled out.
     */
    REFERENCES_FAST

}
