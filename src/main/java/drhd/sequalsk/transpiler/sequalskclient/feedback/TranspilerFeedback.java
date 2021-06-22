package drhd.sequalsk.transpiler.sequalskclient.feedback;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the converted data exchange format received by the SequalsK transpiler.
 */
public class TranspilerFeedback {

    /**
     * List of the items that represent the actual feedback messages of the transpiler.
     */
    private final List<TranspilerFeedbackItem> feedbackItemList = new ArrayList<>();

    /** {@link #feedbackItemList} */
    public List<TranspilerFeedbackItem> getFeedbackItemList() {
        return feedbackItemList;
    }

}
