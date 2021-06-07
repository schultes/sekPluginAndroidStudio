package drhd.sequalsk.transpiler.context.fileresolving.elementresolver;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.PossibleFileReference;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import org.jetbrains.kotlin.idea.references.KtSimpleNameReference;
import org.jetbrains.kotlin.psi.KtCallExpression;
import org.jetbrains.kotlin.psi.KtFunction;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Analyzes {@link KtCallExpression}s and converts it to {@link PossibleFileReference}s or {@link VirtualFile}s,
 * which depends on the used {@link FileContextMode}.
 * <p>
 * {@link KtCallExpression}s are method calls and constructor calls. The call of a constructor is ignored in this case
 * because it is already covered by {@link KtNameReferenceResolver} (starts with uppercase).
 * <p>
 * Problem: In case of a method call the referenced name does not represent a possible file reference, because it starts
 * with a lowercase letter.
 * Example: var i = SomeClass.calculateValue() - the referencedName "calculateValue" does not lead to a virtual file
 * called "calculatedValue.kt". This causes the problem that the type of "i" is not getting added to the context.
 * Solution: This classes purpose is to analyze the return type of the method and add it to the context.
 */
public class KtCallExpressionResolver extends PsiElementResolver<KtCallExpression> {

    @Override
    public List<PossibleFileReference> resolvePossibleReferences(KtCallExpression ktCallExpression) {

        List<PossibleFileReference> references = new ArrayList<>();

        /* the passed KtCallExpression is either a method call or a constructor call */

        if (isConstructorCall(ktCallExpression)) {
            return references;
        }

        /* is function call */

        KtFunction ktFunction = resolveKtFunction(ktCallExpression);
        references.addAll(getReferenceElements(ktFunction));

        return references;
    }


    private boolean isConstructorCall(KtCallExpression ktCallExpression) {
        return Character.isUpperCase(ktCallExpression.getText().charAt(0));
    }

    /**
     * The passed KtCallExpression is either a method call or a constructor call.
     * If the passed PsiElement is a function call it contains a PsiReference to that function.
     * This method resolves the reference and extracts the actual KtFunction object that the passed PsiElement has a
     * reference to.
     */
    private KtFunction resolveKtFunction(KtCallExpression psiElement) {

        for (PsiReference psiReference : psiElement.getReferences()) {

            // the actual PsiReference that resolves to the called KtFunction is of type KtSimpleNameReference
            if (psiReference instanceof KtSimpleNameReference) {
                PsiElement resolvedElement = psiReference.resolve();

                if (resolvedElement instanceof KtFunction) {
                    return (KtFunction) resolvedElement;
                }

            }
        }

        return null;
    }


    /**
     * Returns all KtReferenceExpressions of the TypeReference of the passed KtFunction as {@link PossibleFileReference}s
     *
     * <p><b>Example: private fun method(): Type? {...}</b></p>
     *
     * <p>The KtFunction element is the parent of several psi elements: modifier, fun-keyword, parameter list, method block
     * and the KtTypeReference. In this case the KtTypeReference is "Type?". The KtTypeReference in turn contains
     * one or many KtReferenceExpressions that contain references to the actual return type of the method.
     * In this case it contains one KtReferenceExpression element: "Type". This element could be a reference to a
     * virtual file called "Type.kt". </p>
     *
     * <p>This method does not return a single {@link PossibleFileReference} because a TypeReference could look like
     * this: <code>MutableList&lt;Type&gt;</code>". In that case "MutableList" and "Type" are wrapped as {@link PossibleFileReference}s.
     */
    private List<PossibleFileReference> getReferenceElements(KtFunction ktFunction) {
        List<PossibleFileReference> references = new ArrayList<>();

        if (ktFunction == null || !ktFunction.hasDeclaredReturnType()) {
            return references;
        }

        Collection<KtNameReferenceExpression> nameReferenceExpressions = PsiTreeUtil.collectElementsOfType(
                ktFunction.getTypeReference(),
                KtNameReferenceExpression.class
        );

        for (KtNameReferenceExpression nameReferenceExpression : nameReferenceExpressions) {
            if (Character.isUpperCase(nameReferenceExpression.getReferencedName().charAt(0))) {
                references.add(new PossibleFileReference(
                        nameReferenceExpression.getReferencedName(),
                        nameReferenceExpression)
                );
            }
        }

        return references;
    }

}
