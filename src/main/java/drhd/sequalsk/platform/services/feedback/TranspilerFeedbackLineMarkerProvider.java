package drhd.sequalsk.platform.services.feedback;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedbackItem;
import drhd.sequalsk.utils.PluginIcons;
import drhd.sequalsk.utils.uibuilder.SimpleDialogBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TranspilerFeedbackLineMarkerProvider extends LineMarkerProviderDescriptor {

    @Override
    public String getName() {
        return "transpiler feedback line marker provider";
    }

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {

        Project project = element.getProject();
        TranspilerFeedbackEditorService editorFeedbackService = project.getService(TranspilerFeedbackEditorService.class);
        PreparedFeedback currentFeedback = editorFeedbackService.getCurrentFeedback();
        if(currentFeedback == null) return null;

        PreparedFeedbackItem feedbackItem = currentFeedback.getItem(element);
        if(feedbackItem != null && PluginSettingsHelper.UserInterfaceSettings.showLineMarkerInfos()) {
            return createLineMarker(editorFeedbackService.getCurrentFeedback().getItem(element), element);
        }

        return null;
    }


    /**
     * Returns a LineMarkerInfo object that can be added to the gutter.
     */
    private LineMarkerInfo<PsiElement> createLineMarker(PreparedFeedbackItem item, PsiElement leafElement) {

        return new LineMarkerInfo(
                leafElement,
                leafElement.getTextRange(),
                PluginIcons.SeK,
                tooltip -> item.getOriginalItem().getFeedback(),
                null,
                GutterIconRenderer.Alignment.CENTER
        ) {
            @Override
            public GutterIconRenderer createGutterRenderer() {
                return new LineMarkerGutterIconRenderer<PsiElement>(this) {
                    @Override
                    public AnAction getClickAction() {
                        return createWarningDetailsAction(item.getOriginalItem());
                    }

                    @Override
                    public boolean isNavigateAction() {
                        return true; // show hand icon on hover
                    }

                    @Override
                    public ActionGroup getPopupMenuActions() {
                        return null;
                    }
                };
            }
        };
    }


    /**
     * Generates the default action which shows a dialog that contains the information from the {@link TranspilerFeedbackItem}.
     */
    private AnAction createWarningDetailsAction(TranspilerFeedbackItem item) {
        return new AnAction("Show Details") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder("Warning Details");
                dialogBuilder.addLabel(item.getFeedback());
                DialogWrapper dialog = dialogBuilder.build();
                dialog.setModal(true);
                dialog.show();
            }
        };
    }


}
