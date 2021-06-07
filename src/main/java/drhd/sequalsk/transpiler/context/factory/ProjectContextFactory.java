package drhd.sequalsk.transpiler.context.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.utils.TranspilerContextType;
import drhd.sequalsk.utils.PluginVirtualFileUtils;
import java.util.List;

/**
 * Generates a {@link TranspilerContext} that contains all project files.
 */
public class ProjectContextFactory extends TranspilerContextFactory {

    public ProjectContextFactory(Project project) {
        super(project);
    }

    @Override
    protected TranspilerContextType getContextType() {
        return TranspilerContextType.PROJECT;
    }

    @Override
    public TranspilerContext buildContext() {
        VirtualFile virtualFile = PluginVirtualFileUtils.getVirtualFile(PluginSettingsHelper.DirectoryTranspilerSettings.projectRootDirectory());
        // TODO: 23.02.2021 properly validate directory
        if(virtualFile == null || !virtualFile.isDirectory()) {
            throw new IllegalArgumentException("Could not find project root directory");
        }
        List<VirtualFile> directoryFiles = PluginVirtualFileUtils.getVirtualKtFiles(project, virtualFile);
        return new TranspilerContext(
                asTranspilerFiles(directoryFiles)
        );
    }
}
