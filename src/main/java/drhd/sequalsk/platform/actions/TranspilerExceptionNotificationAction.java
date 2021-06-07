package drhd.sequalsk.platform.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.views.TranspilerExceptionDialog;
import drhd.sequalsk.transpiler.views.TranspilerExceptionHelper;
import org.jetbrains.annotations.NotNull;

/**
 * In case of a failed transpiler request a notification is shown by the help of the
 * {@link TranspilerExceptionHelper#showNotification}.
 * That notification contains this action to show the {@link TranspilerExceptionDialog}
 */
public class TranspilerExceptionNotificationAction extends NotificationAction {

    private final TranspilerRequest transpilerRequest;
    private final Throwable throwable;

    /**
     * {@link TranspilerExceptionNotificationAction}
     */
    public TranspilerExceptionNotificationAction(TranspilerRequest transpilerRequest, Throwable throwable) {
        super("Show Error Details");
        this.transpilerRequest = transpilerRequest;
        this.throwable = throwable;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
        new TranspilerExceptionDialog(throwable, transpilerRequest).show();
        notification.expire();
    }
}
