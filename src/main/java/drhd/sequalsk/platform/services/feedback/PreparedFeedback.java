package drhd.sequalsk.platform.services.feedback;

import com.intellij.psi.PsiElement;
import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedback;
import java.util.List;

/**
 * The {@link TranspilerFeedbackService} links the {@link TranspilerFeedback} received from the SequalsK-Client
 * to the PSI tree of the current editor file. This class is the result of that process.
 */
public class PreparedFeedback {

    /** The original {@link TranspilerFeedback} that was linked to the psi file of the editor. */
    private final TranspilerFeedback originalFeedback;

    /** The absolute path to the file that this feedback was linked to */
    private final String linkedFilePath;

    /** The list of {@link PreparedFeedbackItem} that make up this feedback. */
    private final List<PreparedFeedbackItem> preparedItems;

    public PreparedFeedback(TranspilerFeedback originalFeedback, List<PreparedFeedbackItem> preparedItems, String linkedFilePath) {
        this.originalFeedback = originalFeedback;
        this.preparedItems = preparedItems;
        this.linkedFilePath = linkedFilePath;
    }

    public TranspilerFeedback getOriginalFeedback() {
        return originalFeedback;
    }

    public List<PreparedFeedbackItem> getPreparedItems() {
        return preparedItems;
    }

    public String getLinkedFilePath() {
        return linkedFilePath;
    }

    public PreparedFeedbackItem getItem(PsiElement psiElement) {
        for(PreparedFeedbackItem item : preparedItems) {
            if(item.getElementStart().isEquivalentTo(psiElement)) return item;
        }
        return null;
    }

}
