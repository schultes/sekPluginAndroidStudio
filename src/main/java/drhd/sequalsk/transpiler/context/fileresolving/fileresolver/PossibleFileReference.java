package drhd.sequalsk.transpiler.context.fileresolving.fileresolver;

import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.transpiler.context.utils.FileContextMode;
import org.jetbrains.kotlin.psi.KtReferenceExpression;
import java.util.Objects;

/**
 * Every {@link KtReferenceExpression} that is part of the Psi-Tree is a possible reference to another {@link VirtualFile}
 * of the project. <br><br>
 *
 * {@link PossibleFileReference}s are used for the context generation mode {@link FileContextMode#REFERENCES_FAST}.
 * Instead of resolving the reference to the actual {@link VirtualFile}, the possible file reference works with the
 * assumption that the referenced name is equal to the filename.
 *
 * <b>Example:</b> var sth = SomeClass() - the field sth is of type "SomeClass" which might could be a reference to
 * the file "SomeClass.kt". In this case the property "referencedName" of the KtReferenceExpression has the
 * value "SomeClass", so the {@link PossibleFileReference} object would contain the KtReferenceExpression as well as
 * the value possibleFilename = "SomeClass".
 * <p>
 * The {@link PossibleFileReference} in turn is resolved to a virtual file by the {@link FastReferenceResolver}.
 * The reference is only resolved in two cases: <li>
 *     <ul>The virtual file could not be extracted from the possible file reference</ul>
 *     <ul>More than one virtual file was found with the suggested name.</ul>
 * </li>
 *
 * The {@link KtReferenceExpression} is saved in this object as a backup: if the virtual file could not be extracted
 * from the possible filename the reference expression is used to actually resolve the referenced file. This makes
 * it possible to resolve the file, even if no or more than one file was found with the suggested filename.
 *
 */
public class PossibleFileReference {

    /** The PsiElement that likely contains a reference to the file with name {@link #possibleFilename}. */
    private final KtReferenceExpression ktReferenceExpression;

    /** The filename the {@link #ktReferenceExpression} is probably referencing. */
    private final String possibleFilename;

    public PossibleFileReference(String possibleFilename, KtReferenceExpression ktNameReferenceExpression) {
        this.ktReferenceExpression = ktNameReferenceExpression;
        this.possibleFilename = possibleFilename;
    }

    /** {@link #possibleFilename} */
    public KtReferenceExpression getKtReferenceExpression() {
        return ktReferenceExpression;
    }

    /** {@link #ktReferenceExpression} */
    public String getPossibleFilename() {
        return possibleFilename;
    }

    /** These objects are compared by {@link #possibleFilename}s. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PossibleFileReference)) return false;
        PossibleFileReference that = (PossibleFileReference) o;
        return Objects.equals(possibleFilename, that.possibleFilename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(possibleFilename);
    }
}
