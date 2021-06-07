package drhd.sequalsk.platform.services.toolwindow.tabs.result;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import drhd.sequalsk.platform.services.toolwindow.tabs.result.actions.*;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;

/**
 * The {@link DefaultActionGroup} that is visible above the editor of the {@link ResultTab}.
 * Manages/Updates the visible actions based on the {@link TranspilerRequestMode} of the latest {@link TranspilerResult}
 * This is necessary because some actions are only usable for two-way-requests (e.g. showing interim results).
 */
public class ResultTabActionGroup extends DefaultActionGroup {

    private final ToolWindowModel toolWindowModel;

    // request code
    private final ShowRequestAllAction showRequestAllAction;
    private final ShowRequestMainAction showRequestMainAction;

    // result code
    private final ShowResultAllAction showResultAllAction;

    // interim result code
    private final ShowInterimResultAllAction showInterimResultAllAction;
    private final ShowInterimResultMainAction showInterimResultMainAction;

    // diff view
    private final ShowDiffAction showDiffAction;

    // dropdown transpiler actions
    private final ShowTranspilerRequestAction showTranspilerRequestAction;
    private final ShowTranspilerContextAction showTranspilerContextAction;

    /**
     * {@link ResultTabActionGroup}
     */
    public ResultTabActionGroup(ToolWindowModel toolWindowModel) {
        super("result action group", true);
        this.toolWindowModel = toolWindowModel;

        showRequestAllAction = new ShowRequestAllAction(toolWindowModel);
        showRequestMainAction = new ShowRequestMainAction(toolWindowModel);
        showResultAllAction = new ShowResultAllAction(toolWindowModel);
        showInterimResultAllAction = new ShowInterimResultAllAction(toolWindowModel);
        showInterimResultMainAction = new ShowInterimResultMainAction(toolWindowModel);
        showDiffAction = new ShowDiffAction(toolWindowModel);
        showTranspilerRequestAction = new ShowTranspilerRequestAction(toolWindowModel);
        showTranspilerContextAction = new ShowTranspilerContextAction(toolWindowModel);
    }

    /**
     * Sets all actions visible/invisible that are allowed for the request type (one-way/two-way) of the last request.
     */
    public void updateActionVisibilities() {
        final TranspilerResult currentTranspilerResult = toolWindowModel.getCurrentResult();

        if (currentTranspilerResult == null) return;

        ApplicationManager.getApplication().invokeLater(() -> {
            TranspilerRequestMode requestMode = currentTranspilerResult.getRequest().getRequestMode();
            if (requestMode == TranspilerRequestMode.ONE_WAY) showOneWayActions();
            if (requestMode == TranspilerRequestMode.TWO_WAY) showTwoWayActions();
        });
    }

    /**
     * Sets all actions visible that are allowed for one-way-transpiled requests.
     */
    private void showOneWayActions() {
        removeAll();

        add(showRequestMainAction);
        add(showRequestAllAction);
        addSeparator();
        add(showResultAllAction);
        addSeparator();
        addDropdownActionGroup();
    }

    /**
     * Sets all actions visible that are allowed for two-way-transpiled requests.
     */
    private void showTwoWayActions() {
        removeAll();

        add(showRequestMainAction);
        add(showRequestAllAction);
        addSeparator();
        add(showResultAllAction);
        addSeparator();
        add(showInterimResultMainAction);
        add(showInterimResultAllAction);
        addSeparator();
        add(showDiffAction);
        addDropdownActionGroup();
    }

    /**
     * Adds the dropdown menu that contains all transpiler related actions.
     */
    private void addDropdownActionGroup() {
        addSeparator();
        DefaultActionGroup dropdownGroup = new ResultTabDropdownGroup();
        dropdownGroup.add(showTranspilerRequestAction);
        dropdownGroup.add(showTranspilerContextAction);
        add(dropdownGroup);
    }

}
