package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import drhd.sequalsk.platform.services.toolwindow.tabs.result.ResultTab;
import org.jetbrains.annotations.NotNull;

/**
 * {@link DefaultActionGroup} that is included in the action toolbar of the {@link ResultTab}.
 * Acts as dropdown menu for all actions that are related to the transpiler (e.g. to show transpiler request details or
 * transpiler context).
 */
public class ResultTabDropdownGroup extends DefaultActionGroup {

    public ResultTabDropdownGroup() {
        super("sequalsk.ResultTabDropdownGroup", true);
        getTemplatePresentation().setIcon(AllIcons.Actions.More);
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
