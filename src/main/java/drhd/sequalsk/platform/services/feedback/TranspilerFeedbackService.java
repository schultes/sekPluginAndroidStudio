package drhd.sequalsk.platform.services.feedback;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedback;
import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedbackItem;
import drhd.sequalsk.platform.services.PluginBaseService;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.utils.PluginVirtualFileUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that links the {@link TranspilerFeedback} with the PSI tree of the current editor file.
 * For this purpose the individual {@link TranspilerFeedbackItem}s are each linked to the PSI-elements based on their
 * offset.
 */
public class TranspilerFeedbackService extends PluginBaseService implements TranspilerFeedbackSubscriber {

    @Override
    protected void initializeService() {
        project.getMessageBus().connect().subscribe(TranspilerFeedbackSubscriber.TOPIC, this);
    }

    @Override
    public void onResultReceived(final TranspilerResult result) {

        Runnable runnable = () -> {
            VirtualFile virtualFile = PluginVirtualFileUtils.getVirtualFile(result.getMainFile().getOriginalFile().getPath());
            PsiFile psiFile = PsiUtil.getPsiFile(project, virtualFile);
            TranspilerFeedback originalFeedback = result.getTranspilerFeedback();

            List<PreparedFeedbackItem> preparedItems = new ArrayList<>();

            for (TranspilerFeedbackItem feedbackItem : originalFeedback.getFeedbackItemList()) {

                PsiElement firstElement = getLeafElement(psiFile.findElementAt(feedbackItem.getOffsetStart()));
                PsiElement lastElement = getLeafElement(psiFile.findElementAt(feedbackItem.getOffsetEnd()));

                PreparedFeedbackItem preparedItem = new PreparedFeedbackItem(
                        feedbackItem,
                        firstElement,
                        lastElement
                );
                preparedItems.add(preparedItem);
            }

            PreparedFeedback preparedFeedback = new PreparedFeedback(originalFeedback, preparedItems, virtualFile.getPath());
            project.getMessageBus().syncPublisher(TranspilerFeedbackSubscriber.TOPIC).onFeedbackPrepared(preparedFeedback);
        };

        ApplicationManager.getApplication().invokeLater(runnable);
    }

    private PsiElement getLeafElement(PsiElement psiElement) {
        PsiElement[] elements = PsiTreeUtil.collectElements(psiElement, element -> element instanceof LeafPsiElement);
        return elements[0];
    }

}
