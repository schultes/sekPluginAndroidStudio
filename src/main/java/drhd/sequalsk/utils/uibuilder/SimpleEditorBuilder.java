package drhd.sequalsk.utils.uibuilder;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.ProjectManager;

/**
 * Class to provide a simple way to create a basic code editor window.
 */
public class SimpleEditorBuilder {

    private String content = "";
    private boolean lineNumbersShown = true;
    private boolean isEditable = true;
    private String highlightedLanguageId = "JAVA";
    private int fontSize = 0;
    private int additionalLineCount = 1;

    /**
     * Creates a new EditorBuilder without content. Default values: line numbers visible, editable, highlighted language
     * is java, ide default font size.
     */
    public SimpleEditorBuilder() {}

    /**
     * Creates a new EditorBuilder with content. Default values: line numbers visible, editable, highlighted language
     * is java, ide default font size
     */
    public SimpleEditorBuilder(String content) {
        this.content = content;
    }

    /**
     * Call this to hide the line numbers of the editor. Line numbers are visible by default.
     */
    public SimpleEditorBuilder hideLineNumbers() {
        this.lineNumbersShown = false;
        return this;
    }

    /**
     * Call this to make the editor read only. The editor is editable by default.
     */
    public SimpleEditorBuilder readOnly() {
        this.isEditable = false;
        return this;
    }

    /**
     * Set the content that will be shown in the editor.
     */
    public SimpleEditorBuilder content(String content) {
        this.content = content;
        return this;
    }

    /**
     * Call this to set the syntax highlighting to java.
     */
    public SimpleEditorBuilder highlightJava() {
        this.highlightedLanguageId = "java";
        return this;
    }

    /**
     * Call this to set the syntax highlighting to kotlin.
     */
    public SimpleEditorBuilder highlightKotlin() {
        this.highlightedLanguageId = "kotlin";
        return this;
    }

    /**
     * Call this to remove syntax highlighting.
     */
    public SimpleEditorBuilder highlightingOff() {
        this.highlightedLanguageId = "";
        return this;
    }

    /**
     * Call this to set the syntax highlighting to a custom language.
     */
    public SimpleEditorBuilder highlight(String languageId) {
        this.highlightedLanguageId = languageId;
        return this;
    }

    /**
     * Call this to set a custom font size. The IDEs font size is used by default.
     */
    public SimpleEditorBuilder fontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * Call this to set the number of additional lines. Default is 1.
     */
    public SimpleEditorBuilder additionalLineCount(int lineCount) {
        this.additionalLineCount = lineCount;
        return this;
    }

    /**
     * Build the editor based on the configured settings
     */
    public EditorImpl build() {

        EditorFactory editorFactory = EditorFactory.getInstance();
        Document document = editorFactory.createDocument(content);
        EditorImpl editor = (EditorImpl) (isEditable ? editorFactory.createEditor(document) : editorFactory.createViewer(document));

        EditorSettings settings = editor.getSettings();
        settings.setLineNumbersShown(lineNumbersShown);
        settings.setAdditionalLinesCount(additionalLineCount);

        applyFontSize(editor);
        applyHighlighting(editor);

        settings.setLineMarkerAreaShown(false);
        settings.setIndentGuidesShown(false);
        settings.setGutterIconsShown(false);

        // how to remove the small bar on the left side entirely? settings -> code style -> editor -> contains exactly that kind of editor needed

        return editor;
    }

    private void applyFontSize(EditorEx editor) {
        if (fontSize != 0) editor.setFontSize(fontSize);
    }

    private void applyHighlighting(EditorEx editor) {
        Language language = Language.findLanguageByID(this.highlightedLanguageId);
        FileType fileType = language != null ? language.getAssociatedFileType() : null;
        if (fileType != null) {
            editor.setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(ProjectManager.getInstance().getDefaultProject(), fileType));
        }
    }

}
