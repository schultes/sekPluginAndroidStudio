package drhd.sequalsk.transpiler.views;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.ui.DialogWrapper;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.utils.TranspilerRequestBuilder;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.transpiler.sequalskclient.SequalskClient;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerConfiguration;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerException;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.CodeExamples;
import drhd.sequalsk.utils.uibuilder.SimpleEditorBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class OnlineTranspilerTestDialog extends DialogWrapper {

    private JPanel mainPanel;
    private JPanel inputEditorContainer;
    private TranspilerConfigPanel transpilerConfigPanel;
    private JButton loadExampleButton;
    private JButton transpileButton;

    private EditorImpl inputEditor;
    private EditorImpl outputEditor;

    public OnlineTranspilerTestDialog() {
        super(true);
        init();
        setTitle("Online Transpiler Test");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        TranspilerConfiguration configuration = PluginSettingsHelper.asTranspilerConfig();
        transpilerConfigPanel.setTranspilerConfiguration(configuration);

        // input editor
        inputEditor = new SimpleEditorBuilder("// type kotlin code here or load example code").build();
        inputEditor.getComponent().setPreferredSize(new Dimension(450, 500));
        inputEditorContainer.add(inputEditor.getComponent(), BorderLayout.CENTER);

        // output editor
        outputEditor = new SimpleEditorBuilder("// see transpiler results here ...").readOnly().build();
        outputEditor.getComponent().setPreferredSize(new Dimension(650, 0));
        mainPanel.add(outputEditor.getComponent(), BorderLayout.CENTER);

        // load example button
        loadExampleButton.addActionListener(e ->
                ApplicationManager
                        .getApplication()
                        .runWriteAction(() -> inputEditor.getDocument().setText(CodeExamples.KOTLIN_EXAMPLE.getCode()))
        );

        // transpile code button
        transpileButton.addActionListener(e -> {
            try {
                SequalskClient transpiler = new SequalskClient(PluginSettingsHelper.asTranspilerConfig());

                TranspilerRequestBuilder requestBuilder = new TranspilerRequestBuilder(generateTestContext());
                TranspilerRequest request = requestBuilder.kotlinToSwift().build();

                TranspilerResult result = transpiler.transpile(request);

                ApplicationManager.getApplication().runWriteAction(() -> {
                    outputEditor.getDocument().setText(result.getTranspilerResponse());
                });

            } catch (TranspilerException transpilerException) {
                transpilerException.printStackTrace();
                new TranspilerExceptionDialog(transpilerException).show();
                ApplicationManager.getApplication().runWriteAction(() -> {
                    outputEditor.getDocument().setText("// could not transpile code");
                });
            }
        });

        return mainPanel;
    }

    private TranspilerContext generateTestContext() {
        return new TranspilerContext(new TranspilerRequestFile("kotlin-example-01.kt", inputEditor.getDocument().getCharsSequence().toString()));
    }

    /**
     * Hide cancel button
     */
    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{getOKAction()};
    }

    @Override
    protected void dispose() {
        super.dispose();
        EditorFactory.getInstance().releaseEditor(inputEditor);
        EditorFactory.getInstance().releaseEditor(outputEditor);
    }
}
