package drhd.sequalsk.transpiler.context.fileresolving.fileresolver;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.psi.KtCallExpression;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;

/**
 * Helper class to build the {@link TranspilerContext} for a {@link TranspilerRequest}.
 * <p>
 * Implementation of a {@link PsiRecursiveElementWalkingVisitor} which recursively visits the children of the PsiElement
 * on which the visit was started. The purpose of this visitor is to visit every PsiElement that has a possible
 * reference to another project file - thats why it only visits {@link KtCallExpression}s and {@link KtNameReferenceExpression}s.
 * Those are the PsiElements that possibly contain a PsiReference to a project file that must
 * be part of the TranspilerContext.
 */
abstract public class KtReferenceElementVisitor extends PsiRecursiveElementWalkingVisitor {

    /** The selected text in the analyzed input psi file - elements outside of this text range are not visited */
    protected TextRange textRange;

    @Override
    public void visitElement(@NotNull PsiElement element) {

        if (!inSelectionRange(element)) {
            super.visitElement(element);
            return;
        }

        if (element instanceof KtCallExpression) {
            this.visitKtCallExpression((KtCallExpression) element);
        } else if (element instanceof KtNameReferenceExpression) {
            this.visitKtNameReferenceExpression((KtNameReferenceExpression) element);
        }

        super.visitElement(element);
    }

    abstract protected void visitKtNameReferenceExpression(KtNameReferenceExpression referenceExpression);

    abstract protected void visitKtCallExpression(KtCallExpression ktCallExpression);

    private boolean inSelectionRange(PsiElement element) {
        if (this.textRange == null) return true;

        int firstPosition = element.getTextOffset();
        int lastPosition = element.getTextLength() + firstPosition;

        boolean containsFirstPosition = textRange.containsOffset(firstPosition);
        boolean containsLastPosition = textRange.containsOffset(lastPosition);

        return containsFirstPosition || containsLastPosition;
    }
}
