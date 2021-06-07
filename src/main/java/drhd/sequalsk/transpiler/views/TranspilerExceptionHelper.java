package drhd.sequalsk.transpiler.views;

import drhd.sequalsk.platform.actions.TranspilerExceptionNotificationAction;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.utils.uibuilder.SimpleNotificationBuilder;

public class TranspilerExceptionHelper {

    public static void showNotification(TranspilerRequest transpilerRequest, Throwable throwable) {
        new SimpleNotificationBuilder("Transpilation Error")
                .error()
                .action(new TranspilerExceptionNotificationAction(transpilerRequest, throwable))
                .buildAndShow();
    }

}
