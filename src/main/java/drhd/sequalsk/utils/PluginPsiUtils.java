package drhd.sequalsk.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import java.util.Arrays;
import java.util.List;

/**
 * Contains utility methods to simplify working with the psi.
 */
public class PluginPsiUtils {

    /**
     * For performance reasons LineMarkerProviders and Inspections should be shown on leaf elements only.
     * This method extracts the leaf element of the given psi element.
     */
    public static PsiElement getLeafElement(PsiElement psiElement) {
        if (psiElement.getChildren() == null || psiElement.getChildren().length == 0) return psiElement;

        PsiElement leaf = null;
        while (psiElement.getFirstChild() != null) {
            leaf = psiElement.getFirstChild();
            if (leaf.getFirstChild() == null) break;
        }
        return leaf;
    }

}
