package drhd.sequalsk.transpiler.context.fileresolving.elementresolver;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.PossibleFileReference;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import org.jetbrains.kotlin.psi.KtReferenceExpression;
import java.util.HashMap;
import java.util.List;


/**
 * Base class to analyze {@link KtReferenceExpression}s that are part of a file that a {@link TranspilerContext}
 * is generated for.
 * <p>
 * It takes a single {@link KtReferenceExpression} as input and analyzes if that element is a reference to another
 * project file. Basically it creates a list of {@link PossibleFileReference}s or {@link VirtualFile}s from that
 * {@link KtReferenceExpression} - based on the configured {@link FileContextMode}.
 * <p>
 * Basically a {@link KtReferenceExpression} is "everything that you can click on and the IDE jumps to another
 * location" - the location that the IDE jumps to is the PsiElement that the {@link KtReferenceExpression} is
 * referencing to / has its reference to.
 * <p>
 */
abstract public class PsiElementResolver<T extends KtReferenceExpression> {

    /**
     * This method analyzes the given KtReferenceExpression and turns it into {@link PossibleFileReference}s.
     */
    abstract public List<PossibleFileReference> resolvePossibleReferences(T ktReferenceExpression);

    /**
     * This method analyzes the given KtReferenceExpression and resolves the actual virtual file.
     *
     * @return Hashmap with path as key and virtual file
     */
    public HashMap<String, VirtualFile> resolveReferences(T ktReferenceExpression, List<String> projectKtFilenames) {
        HashMap<String, VirtualFile> resolvedFiles = new HashMap<>();

        for (PsiReference psiReference : ktReferenceExpression.getReferences()) {
            PsiElement resolvedElement = psiReference.resolve();
            if (validElement(resolvedElement)) {
                VirtualFile virtualFile = resolvedElement.getContainingFile().getVirtualFile();
                if (isProjectFile(virtualFile, projectKtFilenames)) {
                    resolvedFiles.put(virtualFile.getNameWithoutExtension(), virtualFile);
                }
            }
        }

        return resolvedFiles;
    }

    protected boolean validElement(PsiElement psiElement) {
        return psiElement != null && psiElement.getContainingFile() != null;
    }

    protected boolean isProjectFile(VirtualFile virtualFile, List<String> projectKtFilenames) {
        return projectKtFilenames.contains(virtualFile.getNameWithoutExtension());
    }
}
