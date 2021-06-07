package drhd.sequalsk.transpiler.validation;

import com.intellij.openapi.actionSystem.AnAction;
import drhd.sequalsk.transpiler.validation.view.TranspilerResultInspectionListItem;
import javax.swing.*;


/**
 * Base class for all actions that appear in {@link TranspilerResultInspectionListItem}s.
 */
abstract public class TranspilerInspectionAction extends AnAction {

    public TranspilerInspectionAction(String text, Icon icon) {
        super(text);
        this.getTemplatePresentation().setIcon(icon);
        this.getTemplatePresentation().setText(text);
    }

}
