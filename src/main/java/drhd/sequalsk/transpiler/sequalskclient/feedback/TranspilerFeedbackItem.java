package drhd.sequalsk.transpiler.sequalskclient.feedback;

/**
 * Represents a single entry of the converted data exchange format received by the SequalsK transpiler.
 * Contains the feedback message of the transpiler.
 */
public class TranspilerFeedbackItem {

    /** start of the text area affected by the feedback */
    private final int offsetStart;

    /** end of the text area affected by the feedback.  */
    private final int offsetEnd;

    /** text message as received from the transpiler */
    private final String feedback;

    public TranspilerFeedbackItem(int offsetStart, int offsetEnd, String feedback) {
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.feedback = feedback;
    }

    /** {@link #offsetStart} */
    public int getOffsetStart() {
        return offsetStart;
    }

    /** {@link #offsetEnd} */
    public int getOffsetEnd() {
        return offsetEnd;
    }

    /** {@link #feedback} */
    public String getFeedback() {
        return feedback;
    }

}
