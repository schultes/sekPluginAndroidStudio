package drhd.sequalsk.transpiler.context.fileresolving.fileresolver;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.context.fileresolving.elementresolver.KtCallExpressionResolver;
import drhd.sequalsk.transpiler.context.fileresolving.elementresolver.KtNameReferenceResolver;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.utils.PluginVirtualFileUtils;
import org.jetbrains.kotlin.psi.KtCallExpression;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;
import java.util.*;

/**
 * Implements the context generation mode {@link FileContextMode#REFERENCES_FAST}.
 */
public class FastReferenceResolver extends FileReferenceResolver {

    /** List of all KtNameReferenceExpressions that ... */
    private final HashSet<PossibleFileReference> possibleFileReferences = new HashSet<>();

    /** {@link FastReferenceResolver} */
    public FastReferenceResolver(Project project, VirtualFile virtualFile) {
        super(project, virtualFile);
    }

    /** {@link FastReferenceResolver} */
    public FastReferenceResolver(Project project, VirtualFile virtualFile, TextRange textRange) {
        super(project, virtualFile, textRange);
    }

    /**
     * Generates a List of VirtualFiles (other kotlin files of the project) that the input file has references to.
     */
    public List<VirtualFile> getReferencedFiles() {
        this.visitElement(inputPsiFile);

        // remove input file from references
        PossibleFileReference referenceToRemove = null;
        for(PossibleFileReference possibleFileReference : possibleFileReferences) {
            if(possibleFileReference.getPossibleFilename().equals(inputVirtualFile.getNameWithoutExtension())) {
                referenceToRemove = possibleFileReference;
            }
        }
        possibleFileReferences.remove(referenceToRemove);

        return this.resolvePossibleReferences();
    }

    /**
     * Resolves all {@link PossibleFileReference}s that were found on traversing the PSI-Tree to an actual virtual file
     * that is part of the current project.
     */
    private List<VirtualFile> resolvePossibleReferences() {

        List<VirtualFile> virtualFiles = new ArrayList<>();

        for(PossibleFileReference possibleFileReference : possibleFileReferences) {
            VirtualFile referencedFile = PluginVirtualFileUtils.toVirtualFile(
                    possibleFileReference,
                    projectKtFilenames,
                    project
            );
            if(referencedFile != null) virtualFiles.add(referencedFile);
        }

        return virtualFiles;
    }


    @Override
    protected void visitKtNameReferenceExpression(KtNameReferenceExpression referenceExpression) {
        KtNameReferenceResolver nameReferenceResolver = new KtNameReferenceResolver();
        possibleFileReferences.addAll(nameReferenceResolver.resolvePossibleReferences(referenceExpression));
    }

    @Override
    protected void visitKtCallExpression(KtCallExpression ktCallExpression) {
        KtCallExpressionResolver callExpressionResolver = new KtCallExpressionResolver();
        possibleFileReferences.addAll(callExpressionResolver.resolvePossibleReferences(ktCallExpression));
    }

}
