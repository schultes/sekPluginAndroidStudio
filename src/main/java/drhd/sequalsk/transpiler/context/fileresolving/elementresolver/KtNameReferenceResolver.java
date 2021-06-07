package drhd.sequalsk.transpiler.context.fileresolving.elementresolver;

import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.PossibleFileReference;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import org.jetbrains.kotlin.psi.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes and converts Kotlin-PsiReference-Elements of type {@link KtNameReferenceExpression} to {@link PossibleFileReference}s
 * or {@link VirtualFile}s, depending on the used {@link FileContextMode}.
 * <p>
 * {@link KtNameReferenceExpression}s are able to find almos all referenced files. {@link KtNameReferenceExpression}
 * It includes the analysis of ...
 * <ul>
 *     <li>TYPE_REFERENCEs - e.g. fun method(): Int <--- TypeReference </li>
 *     <li>CALL_EXPRESSIONs that represent a constructor call (method calls are analysed by KtCallExpressionResolver)</li>
 *     <li>DOT_QUALIFIED_EXPRESSIONs that are static method calls of another class (e.g. SomeClass.getSomething()) or call of an enum value</li>
 * </ul>
 */
public class KtNameReferenceResolver extends PsiElementResolver<KtNameReferenceExpression> {

    public List<PossibleFileReference> resolvePossibleReferences(KtNameReferenceExpression ktNameReferenceExpression) {

        List<PossibleFileReference> references = new ArrayList<>();

        String referencedName = ktNameReferenceExpression.getReferencedName();

        if (Character.isUpperCase(referencedName.charAt(0))) {
            references.add(new PossibleFileReference(
                    referencedName,
                    ktNameReferenceExpression)
            );
        }

        return references;
    }

}
