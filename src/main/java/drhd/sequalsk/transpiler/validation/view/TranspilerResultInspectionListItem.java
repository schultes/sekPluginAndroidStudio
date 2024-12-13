package drhd.sequalsk.transpiler.validation.view;

import com.intellij.openapi.actionSystem.*;
import drhd.sequalsk.transpiler.validation.TranspilerResultInspection;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * JPanel that is the view-representation of a {@link TranspilerResultInspection}.
 * Acts as a list item in a {@link TranspilerResultInspectionList}.
 */
public class TranspilerResultInspectionListItem extends JPanel {

    public TranspilerResultInspectionListItem(TranspilerResultInspection warning) {
        super(new FlowLayout(FlowLayout.LEFT));
        ((FlowLayout) this.getLayout()).setVgap(0);
        ((FlowLayout) this.getLayout()).setHgap(5);
        this.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel severityIcon = new JLabel();
        severityIcon.setIcon(warning.getIcon());
        this.add(severityIcon);
        this.add(new JLabel(warning.getTitle()));

        List<AnAction> warningActions = warning.getActions();

        if(warningActions != null && !warningActions.isEmpty()) {
            DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
            ListItemDropdownGroup dropdownGroup = new ListItemDropdownGroup();
            dropdownGroup.addAll(warningActions);
            defaultActionGroup.add(dropdownGroup);

            ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(warning.getTitle(), defaultActionGroup, true);
            this.add(actionToolbar.getComponent());
        }
    }
}
