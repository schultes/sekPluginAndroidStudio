package drhd.sequalsk.transpiler.context.fileresolving.fileresolver;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.transpiler.context.utils.KeywordBlacklist;
import drhd.sequalsk.transpiler.context.fileresolving.elementresolver.KtCallExpressionResolver;
import drhd.sequalsk.transpiler.context.fileresolving.elementresolver.KtNameReferenceResolver;
import drhd.sequalsk.transpiler.context.fileresolving.elementresolver.PsiElementResolver;
import org.jetbrains.kotlin.psi.KtCallExpression;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;
import org.jetbrains.kotlin.psi.KtReferenceExpression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Implements the context generation mode {@link FileContextMode#REFERENCES_ACCURATE}.
 */
public class AccurateReferenceResolver extends FileReferenceResolver {

    /** List that contains all files that were resolved. The key is a filename without extension. */
    private final HashMap<String, VirtualFile> resolvedFiles = new HashMap<>();

    /** Cache for already resolved referenced names which prevents resolving the same references multiple times. */
    private final HashSet<String> resolvedReferencedNames = new HashSet<>();

    public AccurateReferenceResolver(Project project, VirtualFile virtualFile) {
        super(project, virtualFile);
    }

    public AccurateReferenceResolver(Project project, VirtualFile virtualFile, TextRange textRange) {
        super(project, virtualFile, textRange);
    }

    @Override
    public List<VirtualFile> getReferencedFiles() {
        this.visitElement(inputPsiFile);
        resolvedFiles.remove(inputVirtualFile.getNameWithoutExtension());
        return new ArrayList<>(resolvedFiles.values());
    }

    @Override
    protected void visitKtNameReferenceExpression(KtNameReferenceExpression referenceExpression) {
        boolean alreadyResolved = resolvedReferencedNames.contains(referenceExpression.getReferencedName());
        boolean blacklistedKeyword = KeywordBlacklist.isBlacklisted(referenceExpression.getReferencedName());

        if (!alreadyResolved && !blacklistedKeyword) {
            resolveReferences(referenceExpression, new KtNameReferenceResolver());
            resolvedReferencedNames.add(referenceExpression.getReferencedName());
        }
    }

    @Override
    protected void visitKtCallExpression(KtCallExpression ktCallExpression) {
        resolveReferences(ktCallExpression, new KtCallExpressionResolver());
    }

    private void resolveReferences(KtReferenceExpression element, PsiElementResolver resolver) {
        resolvedFiles.putAll(resolver.resolveReferences(element, projectKtFilenames));
    }

}
