package drhd.sequalsk.transpiler.context.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.FileReferenceResolver;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.AccurateReferenceResolver;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.FastReferenceResolver;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.transpiler.context.utils.TextSelection;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.utils.TranspilerContextType;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import java.util.List;

/**
 * Builds the {@link TranspilerContext} for a part (selected text) of a file. It either passes the whole project as
 * context or it analyzes the main file based on its PsiTree.
 *
 * @see FileContextMode
 */
public class SelectionContextFactory extends FileContextFactory {

    /** The selected text of the input file that the context is generated for. */
    protected final TextSelection textSelection;

    public SelectionContextFactory(Project project, TextSelection textSelection) {
        super(project, textSelection.getVirtualFile());
        this.textSelection = textSelection;
    }

    @Override
    protected TranspilerContextType getContextType() {
        return TranspilerContextType.SELECTION;
    }

    @Override
    public TranspilerContext buildContext() {

        FileContextMode contextMode = PluginSettingsHelper.TranspilerSettings.fileContextMode();

        if (contextMode == FileContextMode.PROJECT) {
            return buildProjectScopeContext();
        }

        FileReferenceResolver referenceResolver = getPsiFileReferenceResolver();
        List<VirtualFile> referencedFiles = referenceResolver.getReferencedFiles();

        return new TranspilerContext(
            generateMainFile(),
            asTranspilerFiles(referencedFiles)
        );
    }

    protected TranspilerRequestFile generateMainFile() {
        return new TranspilerRequestFile(
                textSelection.getVirtualFile().getPath(),
                textSelection.getVirtualFile().getName(),
                textSelection.getSelectedText()
        );
    }

    /** {@inheritDoc} */
    protected TranspilerContext buildProjectScopeContext() {
        ProjectContextFactory projectContextFactory = new ProjectContextFactory(project);
        TranspilerContext projectContext = projectContextFactory.build();

        // convert context so the file to transpile is the main file
        List<TranspilerRequestFile> all = projectContext.getAllFiles();
        TranspilerRequestFile main = generateMainFile();
        all.removeIf(file -> file.getPath().equals(main.getPath()));

        return new TranspilerContext(main, all);
    }

    /** {@inheritDoc} */
    @Override
    protected FileReferenceResolver getPsiFileReferenceResolver() {
        FileReferenceResolver resolver = new FastReferenceResolver(project, mainFile, textSelection.getSelectionRange());

        FileContextMode mode = PluginSettingsHelper.TranspilerSettings.fileContextMode();

        if(mode == FileContextMode.REFERENCES_ACCURATE) {
            resolver = new AccurateReferenceResolver(project, mainFile, textSelection.getSelectionRange());
        }

        return resolver;
    }
}
