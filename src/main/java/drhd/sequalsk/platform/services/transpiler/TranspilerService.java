package drhd.sequalsk.platform.services.transpiler;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import drhd.sequalsk.platform.services.PluginBaseService;
import drhd.sequalsk.platform.services.fileservice.FileContentChangeSubscriber;
import drhd.sequalsk.platform.services.fileservice.FileSelectionSubscriber;
import drhd.sequalsk.platform.services.toolwindow.ToolWindowService;
import drhd.sequalsk.platform.services.transpiler.utils.TranspilerServiceUtils;
import drhd.sequalsk.transpiler.context.factory.FileContextFactory;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.utils.TranspilerRequestBuilder;
import drhd.sequalsk.transpiler.utils.TranspilerContextType;
import drhd.sequalsk.transpiler.utils.TranspilerRequestUtils;
import drhd.sequalsk.transpiler.sequalskclient.SequalskClient;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerException;
import drhd.sequalsk.platform.services.settings.PluginSettingsHelper;
import drhd.sequalsk.transpiler.views.TranspilerExceptionHelper;
import drhd.sequalsk.utils.debugging.DebugLogger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


/**
 * Service to execute {@link TranspilerRequest}s and to implement the automatic mode.
 * Adds the {@link TranspilerResult}s to {@link TranspilerServiceCache} if necessary.
 *
 * The plugin settings allow the user to activate auto transpilation. If auto refreshing is active this service ... <ul>
 * <li>... transpiles a file that was selected in the editor (and tries to read it from cache first) and sends the result to the tool window </li>
 * <li>... transpiles a file if the content of that file has changed (after the delay expired) and sends the result to the tool window </li>
 * </ul>
 *
 */
public class TranspilerService extends PluginBaseService implements FileContentChangeSubscriber, FileSelectionSubscriber {

    @Override
    protected void initializeService() {
        project.getMessageBus().connect().subscribe(FileContentChangeSubscriber.TOPIC, this);
        project.getMessageBus().connect().subscribe(FileSelectionSubscriber.TOPIC, this);
    }

    /**
     * Executes a {@link TranspilerRequest} in a CompletableFuture and returns that Future. Tries to read the result
     * from cache (if active). If the result was found in cache the future will complete almost instantly.
     * <p>
     * Note that a possible {@link TranspilerException} is wrapped to a {@link CompletionException} due to the execution
     * of the {@link TranspilerRequest} in a CompletableFuture.
     */
    public CompletableFuture<TranspilerResult> executeRequest(TranspilerRequest request) {

        boolean cacheActive = PluginSettingsHelper.TranspilerSettings.cacheFiles();
        boolean isFileJob = TranspilerRequestUtils.isFileRequest(request);

        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        DebugLogger.info(this, "transpiling " + request.toString());

                        /* try to read the result from cache */

                        if (cacheActive && isFileJob) {
                            TranspilerResult result = getCache().get(request);
                            if (result == null) DebugLogger.info(this, "Did not find result in cache");
                            else {
                                DebugLogger.info(this, "got result from cache");
                                return result;
                            }
                        }

                        /* execute request */

                        SequalskClient transpiler = new SequalskClient(PluginSettingsHelper.asTranspilerConfig());
                        TranspilerResult result = transpiler.transpile(request);

                        if (cacheActive && isFileJob) {
                            getCache().add(result);
                        }

                        return result;
                    } catch (TranspilerException transpilerException) {
                        transpilerException.printStackTrace();
                        throw new CompletionException(transpilerException);
                    }
                }
        );
    }

    //
    // Subscription Events
    //

    /**
     * {@inheritDoc}
     *
     * If the selected file is available in the transpiler cache the cached result is sent to the tool window
     * immediately.
     *
     */
    @Override
    public void onFileSelected(VirtualFile virtualFile) {
        if(TranspilerServiceUtils.readResultOnFileSelection(virtualFile)) {
            showSelectedFileFromCache(virtualFile);
        }
    }

    /**
     * Transpiles the selected file after the selection delay has expired and it could not be read from cache.
     */
    @Override
    public void onFileSelectionDelayExpired(VirtualFile virtualFile) {
        if(TranspilerServiceUtils.transpileFileOnSelection(virtualFile)){
            /* check if file was already read from cache */
            if (temporaryCachedResult != null) {
                DebugLogger.info(this, virtualFile.getName() + " already read from cache.");
                return;
            }

            /* transpile file */
            transpileAndSendToToolWindow(virtualFile);
        }
    }

    /**
     * Transpiles the file after the content change delay has expired (if allowed)
     *
     * @param virtualFile the file of the editor that has changed
     */
    @Override
    public void onContentChangeDelayExpired(VirtualFile virtualFile) {
        if(TranspilerServiceUtils.transpileFileOnContentChange()) {
            transpileAndSendToToolWindow(virtualFile);
        }
    }

    /**
     * If a file was selected, the cached result (if available) is temporarily stored here. Necessary to avoid the
     * transpilation of a cached file after the selection delay has expired.
     */
    private TranspilerResult temporaryCachedResult;

    /**
     * Tries to read the given virtual file from the cache. If the cache has that file, the cached result will be send
     * to the tool window.
     */
    private void showSelectedFileFromCache(VirtualFile virtualFile) {

        temporaryCachedResult = null;

        /* read file from cache */
        TranspilerResult cachedResult = getCache().get(virtualFile);

        if (cachedResult == null) {
            DebugLogger.info(this, virtualFile.getName() + " is not cached");
            return;
        }

        temporaryCachedResult = cachedResult;
        DebugLogger.info(this, "got result for " + virtualFile.getName() + " from cache");
        toolWindowService().updateContent(temporaryCachedResult);
    }

    /**
     * Transpiles the given virtual file and sends the result to the tool window.
     */
    private void transpileAndSendToToolWindow(VirtualFile virtualFile) {
        ApplicationManager.getApplication().invokeLater(() -> {

            TranspilerContext context = new FileContextFactory(project, virtualFile).build();
            TranspilerRequest request = new TranspilerRequestBuilder(context).contentType(TranspilerContextType.FILE).build();

            CompletableFuture<TranspilerResult> future = executeRequest(request);

            future.thenAccept(transpilerResult -> {
                toolWindowService().updateContent(transpilerResult);
            });

            future.exceptionally(throwable -> {
                throwable.printStackTrace();
                toolWindowService().setCustomEditorContent("// something went wrong ...");
                TranspilerExceptionHelper.showNotification(request, throwable);
                return null;
            });
        });
    }

    private ToolWindowService toolWindowService() {
        return project.getService(ToolWindowService.class);
    }

    private TranspilerServiceCache getCache() {
        return project.getService(TranspilerServiceCache.class);
    }

}