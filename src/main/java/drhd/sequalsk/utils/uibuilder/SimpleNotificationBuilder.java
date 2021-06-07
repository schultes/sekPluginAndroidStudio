package drhd.sequalsk.utils.uibuilder;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import javax.swing.*;


/**
 * Provides a more simple way to build notifications for the notification system of intellij which are displayed in the
 * bottom right corner of the ide.
 */
public class SimpleNotificationBuilder {

    private final String title;
    private String subtitle;
    private String content;
    private NotificationAction action;
    private Icon icon = AllIcons.General.Information;
    private NotificationType type = NotificationType.INFORMATION;

    /**
     * Create a new notification. The default notification type is information.
     */
    public SimpleNotificationBuilder(String title) {
        this.title = title;
    }

    public SimpleNotificationBuilder subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public SimpleNotificationBuilder content(String content) {
        this.content = content;
        return this;
    }

    public SimpleNotificationBuilder info() {
        icon = AllIcons.General.Information;
        type = NotificationType.INFORMATION;
        return this;
    }

    public SimpleNotificationBuilder warning() {
        icon = AllIcons.General.Warning;
        type = NotificationType.WARNING;
        return this;
    }

    public SimpleNotificationBuilder error() {
        icon = AllIcons.General.Error;
        type = NotificationType.ERROR;
        return this;
    }

    public SimpleNotificationBuilder action(NotificationAction action) {
        this.action = action;
        return this;
    }

    public Notification build() {
        Notification notification = new Notification(
                Notifications.SYSTEM_MESSAGES_GROUP_ID,
                icon,
                title,
                subtitle,
                content,
                type,
                null);
        if(action != null) notification.addAction(action);
        return notification;
    }

    public void buildAndShow() {
        Notification notification = build();
        Notifications.Bus.notify(notification);
    }

}
