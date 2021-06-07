package drhd.sequalsk.platform.services.transpiler;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.refactoring.listeners.RefactoringEventData;
import com.intellij.refactoring.listeners.RefactoringEventListener;
import com.intellij.usageView.UsageInfo;
import drhd.sequalsk.platform.services.PluginBaseService;
import drhd.sequalsk.platform.services.fileservice.FileContentChangeSubscriber;
import drhd.sequalsk.platform.services.transpiler.utils.CachedTranspilerResultList;
import drhd.sequalsk.platform.services.transpiler.utils.SimpleRefactoringSubscriber;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.utils.debugging.DebugLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service / Cache that stores {@link TranspilerResult}s of recently executed {@link TranspilerRequest}s.
 * Takes care that no outdated results are stored (e.g. due to content changes or refactorings).
 */
public class TranspilerServiceCache extends PluginBaseService implements FileContentChangeSubscriber, SimpleRefactoringSubscriber {

    /** Contains all cached TranspilerResults */
    private final CachedTranspilerResultList cachedResults = new CachedTranspilerResultList();

    @Override
    protected void initializeService() {
        project.getMessageBus().connect().subscribe(RefactoringEventListener.REFACTORING_EVENT_TOPIC, this);
        project.getMessageBus().connect().subscribe(FileContentChangeSubscriber.TOPIC, this);
    }

    /**
     * Adds a result to the cache.
     */
    public void add(TranspilerResult result) {
        if (!active()) {
            DebugLogger.info(this, "Not adding result, cache is inactive.");
            return;
        }

        cachedResults.add(result);
        DebugLogger.info(this, "added result to cache");
    }

    /**
     * Returns a cached {@link TranspilerResult} to the given request or null if no result was found.
     * <p>
     * More precisely it searches for a result with the same main file as the request.
     */
    public TranspilerResult get(TranspilerRequest request) {
        if (!active()) {
            DebugLogger.info(this, "cache is inactive, not searching for result");
            return null;
        }

        return cachedResults.get(request);
    }

    /**
     * Returns a cached {@link TranspilerResult} to the given virtual file or null if no result was found.
     * More precisely it searches for a result whose main file is equal to the passed virtual file (compared by path)
     */
    public TranspilerResult get(VirtualFile mainFile) {
        if (!active()) {
            DebugLogger.info(this, "cache is inactive, not searching for result");
            return null;
        }

        return cachedResults.get(mainFile);
    }

    /**
     * Removes all cached results that have a reference to the changed file (main file & additional files).
     *
     * @param virtualFile the file whose content has changed
     */
    @Override
    public void onKotlinFileContentChanged(VirtualFile virtualFile) {
        cachedResults.remove(virtualFile.getPath());
    }

    /**
     * Clears the cache.
     */
    public void clear() {
        cachedResults.clear();
        DebugLogger.info(this, "Cleared cache");
    }

    /** Checks if the cache is activated in the plugin settings */
    private boolean active() {
        return PluginSettingsHelper.TranspilerSettings.cacheFiles();
    }

    @Override
    public void refactoringStarted(@NotNull String refactoringId, @Nullable RefactoringEventData beforeData) {
        Collection<UsageInfo> usageInfos = beforeData.getUserData(RefactoringEventData.USAGE_INFOS_KEY);

        List<VirtualFile> affectedFiles = new ArrayList<>();
        for(UsageInfo usageInfo : usageInfos) {
            affectedFiles.add(usageInfo.getVirtualFile());
        }

        for(VirtualFile virtualFile : affectedFiles) {
            cachedResults.remove(virtualFile.getPath());
        }
    }

}
