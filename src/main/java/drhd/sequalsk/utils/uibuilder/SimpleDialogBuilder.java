package drhd.sequalsk.utils.uibuilder;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that provides a more simple way of generating DialogWrappers, the default way of showing dialogs in IntelliJ.
 */
public class SimpleDialogBuilder {

    /**
     * This interface exists for convenience reasons: it allows to call addComponent() without breaking
     * the dot-chain and being forced to create components before the dialog building process.
     * See calls of addComponent(ComponentBuilder) for examples.
     */
    public interface ComponentBuilder {
        Component buildComponent();
    }

    /**
     * This interface provides a workaround for memory leaks after closing dialogs.
     * In some cases components added to the dialog must be released or disposed after te dialog was closed.
     * Add those components by {@link SimpleDialogBuilder#addComponentDisposable}. As soon as the dialog gets disposed
     * the method {@link ComponentDisposer#disposeComponent()} of this interface is called.
     */
    public interface ComponentDisposer {

        /** Provide a way to dispose or release the component */
        void disposeComponent();
    }


    private final JPanel mainLayout;
    private final String title;
    private boolean showCancelAction = false;
    private final List<ComponentDisposer> componentDisposers = new ArrayList<>();


    /**
     * Start the creation of a new DialogWrapper. The default layout orientation is vertical. By default the cancel
     * button is not visible.
     *
     * @param title The title of the dialog.
     */
    public SimpleDialogBuilder(String title) {
        this.title = title;
        mainLayout = new JPanel();
        mainLayout.setLayout(new BoxLayout(mainLayout, BoxLayout.Y_AXIS));
    }


    /**
     * Call this to set the dialog layout direction to horizontal.
     */
    public SimpleDialogBuilder horizontalLayout() {
        mainLayout.setLayout(new FlowLayout());
        return this;
    }


    /**
     * Adds a label with the given text to the dialog layout.
     */
    public SimpleDialogBuilder addLabel(String text) {
        mainLayout.add(new Label(text));
        return this;
    }

    public SimpleDialogBuilder addLabelBold(String text) {
        Label label = new Label(text);
        Font font = new Font("Courier", Font.BOLD,12);
        label.setFont(font);
        mainLayout.add(label);
        return this;
    }


    /**
     * Adds a custom component to the dialog layout.
     */
    public SimpleDialogBuilder addComponent(JComponent jComponent) {
        mainLayout.add(jComponent);
        return this;
    }


    /**
     * Adds a custom component to the dialog layout.
     */
    public SimpleDialogBuilder addComponent(ComponentBuilder componentBuilder) {
        mainLayout.add(componentBuilder.buildComponent());
        return this;
    }


    /**
     * Adds a custom component to the dialog layout which will be disposed after the dialog was closed.
     */
    public SimpleDialogBuilder addComponentDisposable(JComponent jComponent, ComponentDisposer componentDisposer) {
        mainLayout.add(jComponent);
        componentDisposers.add(componentDisposer);
        return this;
    }


    /**
     * Adds a custom component to the dialog layout which will be disposed after the dialog was closed.
     */
    public SimpleDialogBuilder addComponentDisposable(ComponentBuilder componentBuilder, ComponentDisposer componentDisposer) {
        mainLayout.add(componentBuilder.buildComponent());
        componentDisposers.add(componentDisposer);
        return this;
    }


    /**
     * Shortcut to add an Editor to the dialog. Use this method if you do not want to take care of building and releasing
     * of the editor after the dialog was closed.
     */
    public SimpleDialogBuilder addEditor(SimpleEditorBuilder editorBuilder) {
        Editor editor = editorBuilder.build();
        this.addComponentDisposable(
                () -> editor.getComponent(),
                () -> EditorFactory.getInstance().releaseEditor(editor)
        );
        return this;
    }


    /**
     * Call this to show the cancel button. Cancel is invisible by default.
     */
    public SimpleDialogBuilder withCancelAction() {
        this.showCancelAction = true;
        return this;
    }


    /**
     * Build and show the dialog in one step. If you need access the generated dialog use {@link SimpleDialogBuilder#build()}
     * instead.
     */
    public void buildAndShow() {
        this.build().show();
    }

    /**
     * Convert the given dialog settings and components to a DialogWrapper.
     */
    public DialogWrapper build() {

        return new DialogWrapper(true) {

            @Nullable
            @Override
            protected JComponent createCenterPanel() {
                return mainLayout;
            }

            @NotNull
            @Override
            protected Action[] createActions() {
                if (showCancelAction) {
                    return super.createActions();
                }
                return new Action[]{getOKAction()};
            }

            @Override
            protected void createDefaultActions() {
                super.createDefaultActions();
                init();
                setTitle(title);
            }

            @Override
            protected void dispose() {
                super.dispose();
                for (ComponentDisposer componentDisposer : componentDisposers) {
                    componentDisposer.disposeComponent();
                }
            }
        };
    }
}