package drhd.sequalsk.transpiler.context.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.FileReferenceResolver;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.FastReferenceResolver;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.AccurateReferenceResolver;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequestFile;
import drhd.sequalsk.transpiler.utils.TranspilerContextType;
import java.util.List;

/**
 * Builds the {@link TranspilerContext} for a single file. It either passes the whole project as context or it
 * analyzes the main file based on its PsiTree.
 *
 * @see FileContextMode
 */
public class FileContextFactory extends TranspilerContextFactory {

    /** The file the context is created for. */
    protected final VirtualFile mainFile;

    public FileContextFactory(Project project, VirtualFile mainFile) {
        super(project);
        this.mainFile = mainFile;
    }

    @Override
    protected TranspilerContextType getContextType() {
        return TranspilerContextType.FILE;
    }

    @Override
    protected TranspilerContext buildContext() {

        FileContextMode contextMode = PluginSettingsHelper.TranspilerSettings.fileContextMode();
        if (contextMode == FileContextMode.PROJECT) {
            return buildProjectScopeContext();
        }

        FileReferenceResolver resolver = getPsiFileReferenceResolver();
        List<VirtualFile> referencedFiles = resolver.getReferencedFiles();

        return new TranspilerContext(
                asTranspilerFile(mainFile),
                asTranspilerFiles(referencedFiles)
        );
    }

    /**
     * Creates the context if the plugin is configured to use the whole project as context for file translations.
     */
    protected TranspilerContext buildProjectScopeContext() {
        ProjectContextFactory projectContextFactory = new ProjectContextFactory(project);
        TranspilerContext projectContext = projectContextFactory.build();

        // convert context so the file to transpile is the main file
        List<TranspilerRequestFile> all = projectContext.getAllFiles();
        TranspilerRequestFile main = asTranspilerFile(mainFile);
        all.removeIf(file -> file.getPath().equals(main.getPath()));

        return new TranspilerContext(main, all);
    }

    /**
     * Depending on the {@link FileContextMode} this method returns the corresponding implementation of
     * the {@link FileReferenceResolver}.
     */
    protected FileReferenceResolver getPsiFileReferenceResolver() {
        FileReferenceResolver resolver = new FastReferenceResolver(project, mainFile);

        FileContextMode mode = PluginSettingsHelper.TranspilerSettings.fileContextMode();

        if(mode == FileContextMode.REFERENCES_ACCURATE) {
            resolver = new AccurateReferenceResolver(project, mainFile);
        }

        return resolver;
    }
}
