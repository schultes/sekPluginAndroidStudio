package drhd.sequalsk.platform.services.feedback;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedback;
import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedbackItem;

/**
 * The {@link TranspilerFeedbackService} links the {@link TranspilerFeedback} received from the SequalsK-Client
 * to the PSI tree of the current editor file. Every {@link TranspilerFeedbackItem} gets converted to an object
 * of this class during that process.
 */
public class PreparedFeedbackItem {

    /** The original {@link TranspilerFeedbackItem} that was linked to the file. */
    private final TranspilerFeedbackItem originalItem;

    /** The first {@link PsiElement} of the text area affected by this feedback item. */
    private final PsiElement elementStart;

    /** The last {@link PsiElement} of the text area affected by this feedback item. */
    private final PsiElement elementEnd;

    /** The {@link TextRange} affected by this feedback item. */
    private final TextRange textRange;

    public PreparedFeedbackItem(TranspilerFeedbackItem originalItem, PsiElement elementStart, PsiElement elementEnd) {
        this.originalItem = originalItem;
        this.elementStart = elementStart;
        this.elementEnd = elementEnd;
        this.textRange = new TextRange(elementStart.getTextOffset(), elementEnd.getTextOffset() + elementEnd.getTextLength());
    }

    /** {@link #originalItem} */
    public TranspilerFeedbackItem getOriginalItem() {
        return originalItem;
    }

    /** {@link #elementStart} */
    public PsiElement getElementStart() {
        return elementStart;
    }

    /** {@link #elementEnd} */
    public PsiElement getElementEnd() {
        return elementEnd;
    }

    /** {@link #textRange} */
    public TextRange getTextRange() {
        return textRange;
    }

}
