package drhd.sequalsk.transpiler.sequalskclient.feedback;

import java.util.ArrayList;
import java.util.List;

public class DummyFeedbackProvider {

    public static TranspilerFeedback generateFeedback() {
        List<TranspilerFeedbackItem> transpilerFeedback = new ArrayList<>();
        transpilerFeedback.add(new TranspilerFeedbackItem(
                0,
                7,
                "dummy feedback 1")
        );

        transpilerFeedback.add(new TranspilerFeedbackItem(
                59,
                60,
                "dummy feedback 2")
        );

        return new TranspilerFeedback(transpilerFeedback);
    }
}
