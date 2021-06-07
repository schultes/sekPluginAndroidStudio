package drhd.sequalsk.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import drhd.sequalsk.transpiler.context.fileresolving.fileresolver.PossibleFileReference;
import org.jetbrains.kotlin.idea.KotlinFileType;
import org.jetbrains.kotlin.psi.KtFile;
import org.jetbrains.kotlin.psi.KtReferenceExpression;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains utility methods to simplify working with virtual files.
 */
public class PluginVirtualFileUtils {

    /**
     * Returns a list of all virtual files that a part of the passed project.
     * A list of all possible FileTypes can be read by "FileBasedIndex.getInstance().getAllKeys(FileTypeIndex.NAME, project)"
     */
    public static List<VirtualFile> getVirtualFiles(Project project, LanguageFileType fileType) {
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(
                fileType,
                GlobalSearchScope.projectScope(project)
        );

        return new ArrayList<>(virtualFiles);
    }

    /**
     * Returns a list of all virtual kotlin files that are part of the passed project.
     */
    public static List<VirtualFile> getVirtualKtFiles(Project project) {
        return getVirtualFiles(project, KotlinFileType.INSTANCE);
    }

    /**
     * Returns a list of all virtual kotlin files that are part of the passed directory.
     */
    public static List<VirtualFile> getVirtualKtFiles(Project project, VirtualFile directory) {
        List<VirtualFile> virtualFiles = VfsUtil.collectChildrenRecursively(directory);
        return virtualFiles.stream()
                .filter(virtualFile -> PluginVirtualFileUtils.isKotlinFile(virtualFile))
                .collect(Collectors.toList());
    }

    public static VirtualFile getVirtualFile(String path) {
        return LocalFileSystem.getInstance().findFileByIoFile(new File(path));
    }

    /**
     * Returns a list of all kotlin filenames (not paths) without extension that are part of the passed project.
     * If there are duplicate filenames in different locations of the project the name is contained multiple times in
     * the generated list.
     */
    public static List<String> getKotlinFilenames(Project project) {
        List<String> filenames = new ArrayList();
        getVirtualKtFiles(project).forEach(virtualFile -> filenames.add(virtualFile.getNameWithoutExtension()));
        return filenames;
    }

    /**
     * Resolves a given filename (without extension) or path to a list of virtual files that are part of the passed project.
     * In case of non-unique filenames the list contains all files matching that filename. In case of a unique filename
     * or full path the list contains one element. Returns an empty list if no file was found.
     */
    public static List<VirtualFile> resolvePathOrFilename(String nameOrPath, Project project) {
        List<VirtualFile> virtualFiles = new ArrayList<>();

        if (nameOrPath.contains("/")) {
            virtualFiles.add(VirtualFileManager.getInstance().findFileByUrl("file://" + nameOrPath));
            return virtualFiles;
        }

        for (VirtualFile virtualFile : getVirtualKtFiles(project)) {
            if (virtualFile.getNameWithoutExtension().equals(nameOrPath))
                virtualFiles.add(virtualFile);
        }

        return virtualFiles;
    }

    /**
     * Returns the document that belongs to a virtual file.
     */
    public static Document getDocument(VirtualFile virtualFile) {
        if (virtualFile == null) return null;
        return FileDocumentManager.getInstance().getDocument(virtualFile);
    }

    /**
     * Returns true if the virtual file is a kotlin file.
     */
    public static boolean isKotlinFile(VirtualFile virtualFile) {
        if (virtualFile == null) return false;
        return virtualFile.getFileType().getName().equals("Kotlin");
    }

    /**
     * Returns the content/text of the virtual file.
     */
    public static CharSequence getText(VirtualFile virtualFile) {
        return getDocument(virtualFile).getCharsSequence();
    }

    /**
     * Returns a concatenated String of file names ("a.kt, b.kt, c.kt, ...")
     */
    public static String asNameList(List<VirtualFile> virtualFiles) {
        String names = "";
        for (VirtualFile virtualFile : virtualFiles) {
            names += virtualFile.getName() + ", ";
        }
        if (names.endsWith(", ")) names = names.substring(0, names.length() - 2);
        return names;
    }

    /**
     * Converts a {@link PossibleFileReference} to a virtual file that is part of the given project.
     */
    public static VirtualFile toVirtualFile(PossibleFileReference possibleFileReference, List<String> projectKtFilenames, Project project) {
        String name = possibleFileReference.getPossibleFilename();
        KtReferenceExpression referenceExpression = possibleFileReference.getKtReferenceExpression();

        int occurrences = Collections.frequency(projectKtFilenames, name);

        if (occurrences == 0) return null;

        if (occurrences == 1) {
            return PluginVirtualFileUtils.resolvePathOrFilename(name, project).get(0);
        }

        for (PsiReference psiReference : referenceExpression.getReferences()) {
            if (psiReference.resolve() != null) {
                PsiFile containingFile = psiReference.resolve().getContainingFile();
                if(containingFile != null) {
                    String path = ((KtFile) psiReference.resolve().getContainingFile()).getVirtualFilePath();
                    return PluginVirtualFileUtils.resolvePathOrFilename(path, project).get(0);
                }
            }
        }

        return null;
    }
}
