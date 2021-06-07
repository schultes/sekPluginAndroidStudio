package drhd.sequalsk.platform.services.feedback;

import com.intellij.util.messages.Topic;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowService;
import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedback;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;

/**
 * Subscriber for transpiler feedback events.
 */
public interface TranspilerFeedbackSubscriber {

    Topic<TranspilerFeedbackSubscriber> TOPIC = Topic.create("transpiler_feedback_topic", TranspilerFeedbackSubscriber.class);

    /** Published as soon as a {@link TranspilerResult} is added to the {@link ToolWindowService}  */
    default void onResultReceived(TranspilerResult result) {}

    /** Published as soon as the {@link TranspilerFeedbackService} linked the {@link TranspilerFeedback} to the current editor file. */
    default void onFeedbackPrepared(PreparedFeedback transpilerFeedback) {}

}
