package drhd.sequalsk.transpiler.context;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.context.factory.*;
import drhd.sequalsk.transpiler.context.utils.TextSelection;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;

/**
 * Utility class to provide easy access to the implementations of {@link TranspilerContextFactory}
 */
public class TranspilerContextGenerator {

    public static TranspilerContext buildDirectoryContext(Project project, VirtualFile virtualFile) {
        DirectoryContextFactory factory = new DirectoryContextFactory(project, virtualFile);
        return factory.build();
    }

    public static TranspilerContext buildProjectContext(Project project) {
        ProjectContextFactory factory = new ProjectContextFactory(project);
        return factory.build();
    }

    public static TranspilerContext buildFileContext(Project project, VirtualFile mainFile) {
        FileContextFactory factory = new FileContextFactory(project, mainFile);
        return factory.build();
    }

    public static TranspilerContext buildSelectionContext(Project project, TextSelection textSelection) {
        SelectionContextFactory factory = new SelectionContextFactory(project, textSelection);
        return factory.build();
    }

}
