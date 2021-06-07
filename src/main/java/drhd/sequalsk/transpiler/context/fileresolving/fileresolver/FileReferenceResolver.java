package drhd.sequalsk.transpiler.context.fileresolving.fileresolver;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.utils.PluginVirtualFileUtils;
import java.util.List;

/**
 * Helper class to build the {@link TranspilerContext} for a {@link TranspilerRequest}.
 * <p>
 * It takes a {@link VirtualFile} as input and generates a list of VirtualFiles that the input file has references to and
 * that are part of the project.
 * <p>
 * Example: Class A uses an Object of Class B and an object of Type C that are declared in another file - the returned
 * List contains B and C as Virtual Files.
 * <p>
 * Implements the {@link KtReferenceElementVisitor} to analyze every PsiElement of the input file that might could have
 * a reference to project file that must be part of the context.
 */
abstract public class FileReferenceResolver extends KtReferenceElementVisitor {

    /** The project that the referenced files are part of. */
    protected Project project;

    /** the input psi file for which the referenced files are resolved for */
    protected PsiFile inputPsiFile;

    /** the input file for which the referenced files are resolved for */
    protected VirtualFile inputVirtualFile;

    /** all filenames of all kotlin files in the current project */
    protected List<String> projectKtFilenames;

    /** {@link FileReferenceResolver} */
    public FileReferenceResolver(Project project, VirtualFile virtualFile) {
        this.initialize(project, virtualFile, null);
    }

    /** {@link FileReferenceResolver} */
    public FileReferenceResolver(Project project, VirtualFile virtualFile, TextRange textRange) {
        initialize(project, virtualFile, textRange);
    }

    /**
     * Helper method for the constructor to initialize the values.
     */
    private void initialize(Project project, VirtualFile virtualFile, TextRange textRange) {
        this.project = project;
        this.inputVirtualFile = virtualFile;
        this.inputPsiFile = PsiManager.getInstance(project).findFile(virtualFile);
        this.textRange = textRange;
        this.projectKtFilenames = PluginVirtualFileUtils.getKotlinFilenames(project);
    }

    abstract public List<VirtualFile> getReferencedFiles();
}
