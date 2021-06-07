package drhd.sequalsk.utils.uibuilder;

import com.intellij.ui.IdeBorderFactory;

import javax.swing.*;
import java.awt.*;

public class ViewUtils {

    public static void setTitleSeparator(JPanel jPanel, String title) {
        jPanel.setBorder(IdeBorderFactory.createTitledBorder(title));
    }

    public static void replaceBorderLayoutCenter(JPanel panel, Component replacement) {
        BorderLayout layout = (BorderLayout)panel.getLayout();
        panel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        panel.add(replacement, BorderLayout.CENTER);
        panel.updateUI();
        panel.invalidate();
    }

}
