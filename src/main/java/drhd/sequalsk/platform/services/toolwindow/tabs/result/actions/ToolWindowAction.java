package drhd.sequalsk.platform.services.toolwindow.tabs.result.actions;

import com.intellij.openapi.actionSystem.AnAction;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.platform.services.toolwindow.tabs.result.ResultTab;
import javax.swing.*;

/**
 * Base class for all actions that appear in the action toolbar of the {@link ResultTab}.
 */
abstract public class ToolWindowAction extends AnAction {

    protected final ToolWindowModel toolWindowModel;

    public ToolWindowAction(String text, Icon icon, ToolWindowModel toolWindowModel) {
        super(text);
        this.getTemplatePresentation().setIcon(icon);
        this.getTemplatePresentation().setText(text);
        this.toolWindowModel = toolWindowModel;
    }

}
