package drhd.sequalsk.platform.services.toolwindow.tabs;

import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import drhd.sequalsk.platform.services.toolwindow.ResultChangeListener;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowModel;
import javax.swing.*;

/**
 * Base class for all tabs that are displayed in the plugin tool window.
 */
abstract public class ToolWindowTab implements ResultChangeListener {

    /**
     * {@link ToolWindowModel}
     */
    protected final ToolWindowModel model;

    /**
     * The content that is displayed in the tool window while the tab is selected.
     */
    protected final Content tabContent;

    /** {@link ToolWindowTab} */
    public ToolWindowTab(ToolWindowModel model) {
        this.model = model;
        this.model.addListener(this);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        tabContent = contentFactory.createContent(null, getTitle(), false); // display name = tab name
        tabContent.setComponent(createMainLayout());
    }

    /** {@link ToolWindowTab#tabContent} */
    public Content getTabContent() {
        return tabContent;
    }

    /**
     * Returns the title that is displayed as label in the tab-bar of the tool window.
     */
    abstract protected String getTitle();

    /**
     * Creates the layout and returns the main container that will be displayed as content in the tool window.
     */
    abstract protected JComponent createMainLayout();
}
