package drhd.sequalsk.transpiler.validation.view;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import drhd.sequalsk.transpiler.validation.TranspilerResultInspection;
import org.jetbrains.annotations.NotNull;

/**
 * Contains all actions that are registery by {@link TranspilerResultInspection#getActions()} as dropdown list.
 */
public class ListItemDropdownGroup extends DefaultActionGroup {

    public ListItemDropdownGroup() {
        super("sequalsk.ResultTabDropdownGroup", true);
        getTemplatePresentation().setIcon(AllIcons.General.InspectionsEye);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        JBPopupFactory.getInstance()
                .createActionGroupPopup(
                        null,
                        this,
                        e.getDataContext(),
                        JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                        true)
                .showUnderneathOf(e.getInputEvent().getComponent());
    }

}
