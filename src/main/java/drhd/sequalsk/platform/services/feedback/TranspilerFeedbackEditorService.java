package drhd.sequalsk.platform.services.feedback;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import drhd.sequalsk.platform.services.PluginBaseService;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import java.awt.*;

/**
 * Service that uses the {@link PreparedFeedbackItem}s to highlight the affected code in the editor.
 * Forces a regeneration of the gutter icons so the {@link PreparedFeedbackItem}s will be converted to gutter icons from
 * the {@link TranspilerFeedbackLineMarkerProvider}.
 */
public class TranspilerFeedbackEditorService extends PluginBaseService implements TranspilerFeedbackSubscriber {

    /**
     * The prepared feedback that belongs to the current editor file.
     */
    private PreparedFeedback currentFeedback;

    @Override
    protected void initializeService() {
        project.getMessageBus().connect().subscribe(TranspilerFeedbackSubscriber.TOPIC, this);
    }

    @Override
    public void onFeedbackPrepared(PreparedFeedback preparedFeedback) {

        this.currentFeedback = preparedFeedback;

        if(!PluginSettingsHelper.UserInterfaceSettings.showLineMarkerInfos()) return;

        // force rebuild of highlighting and gutter icons
        DaemonCodeAnalyzer.getInstance(project).restart();

        // highlight elements in editor
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        for(PreparedFeedbackItem item : preparedFeedback.getPreparedItems()) {
            editor.getMarkupModel().addRangeHighlighter(
                    item.getTextRange().getStartOffset(),
                    item.getTextRange().getEndOffset(),
                    HighlighterLayer.LAST,
                    new TextAttributes(null, new Color(245,234,193), null, null, Font.PLAIN),
                    HighlighterTargetArea.EXACT_RANGE);
        }
    }

    /** {@link #currentFeedback} */
    public PreparedFeedback getCurrentFeedback() {
        return currentFeedback;
    }
}
