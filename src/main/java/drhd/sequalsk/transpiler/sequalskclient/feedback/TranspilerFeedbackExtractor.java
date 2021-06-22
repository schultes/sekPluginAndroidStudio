package drhd.sequalsk.transpiler.sequalskclient.feedback;

import java.util.ArrayList;
import java.util.List;

/**
 * The transpiler's response may contain feedback on aspects of the source code that cannot be
 * transpiled properly. Each problem occupies an own line, terminated by "\n". Each line consists
 * of start index, end index, and problem description, separated by ";".
 * An empty line marks the beginning of the transpiled code.
 * This class provides an utility method to extract the feedback from the transpiler's response.
 */
public class TranspilerFeedbackExtractor {
    /**
     * @param transpiledCode the response received from the transpiler
     * @param feedback an empty object that feedback items can be added to
     * @return the transpiled code minus the feedback lines at the beginning
     */
    public static String extractFeedback(String transpiledCode, TranspilerFeedback feedback) {
        final List<String> feedbackLines = new ArrayList<>();
        String firstLine;
        do {
            int endOfFirstLine = transpiledCode.indexOf("\n");
            firstLine = transpiledCode.substring(0, endOfFirstLine);
            transpiledCode = transpiledCode.substring(endOfFirstLine+1); // contents beginning with the second line
            if (!firstLine.isEmpty()) feedbackLines.add(firstLine);
        } while (!firstLine.isEmpty());

        // Before the source code is sent to the transpiler a comment line is added that describes
        // the file. The feedback indices received from the transpiler refer to the source including
        // the comment line. However, in the editor the source code without the comment line is shown
        // and highlighting must refer to the shown source code without the comment line.
        // Therefore, subsequently the length of the comment line must be subtracted from the received indices.
        final int lengthOfSplitterComment = transpiledCode.indexOf("\n")+1;

        for (String feedbackLine : feedbackLines) {
            final String[] parts = feedbackLine.split(";");
            final TranspilerFeedbackItem item = new TranspilerFeedbackItem(
                    Integer.parseInt(parts[0])-lengthOfSplitterComment,
                    Integer.parseInt(parts[1])-lengthOfSplitterComment, parts[2]);
            feedback.getFeedbackItemList().add(item);
        }

        return transpiledCode;
    }
}
