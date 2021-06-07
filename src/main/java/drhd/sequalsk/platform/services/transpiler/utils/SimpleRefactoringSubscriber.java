package drhd.sequalsk.platform.services.transpiler.utils;

import com.intellij.refactoring.listeners.RefactoringEventData;
import com.intellij.refactoring.listeners.RefactoringEventListener;
import drhd.sequalsk.platform.services.transpiler.TranspilerServiceCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link RefactoringEventListener}
 *
 * Helps to keep the {@link TranspilerServiceCache} clean.
 */
public interface SimpleRefactoringSubscriber extends RefactoringEventListener {

    @Override
    default void refactoringDone(@NotNull String refactoringId, @Nullable RefactoringEventData afterData) {

    }

    @Override
    default void conflictsDetected(@NotNull String refactoringId, @NotNull RefactoringEventData conflictsData) {

    }

    @Override
    default void undoRefactoring(@NotNull String refactoringId) {

    }
}
