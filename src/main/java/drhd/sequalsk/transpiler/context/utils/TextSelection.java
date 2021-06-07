package drhd.sequalsk.transpiler.context.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Wrapper around a text selection inside of a virtual file.
 */
public class TextSelection {

    private final VirtualFile virtualFile;
    private final String selectedText;
    private final TextRange selectionRange;

    public TextSelection(VirtualFile virtualFile, String selectedText, int offsetStart, int offsetEnd) {
        this.virtualFile = virtualFile;
        this.selectedText = selectedText;
        this.selectionRange = new TextRange(offsetStart, offsetEnd);
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public TextRange getSelectionRange() {
        return selectionRange;
    }

    public static TextSelection fromEditor(Editor editor) {
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        return new TextSelection(
                virtualFile,
                editor.getSelectionModel().getSelectedText(),
                editor.getSelectionModel().getSelectionStart(),
                editor.getSelectionModel().getSelectionEnd()
        );
    }

}
