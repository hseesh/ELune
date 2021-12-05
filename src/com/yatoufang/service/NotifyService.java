package com.yatoufang.service;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.yatoufang.templet.Application;

/**
 * @author hse
 * @date 2021/4/15 0015
 */
public class NotifyService {

    private static final NotificationGroup notify = new NotificationGroup("com.yatoufang.notify", NotificationDisplayType.TOOL_WINDOW, true);

    public static void notify(String content) {
        notify.createNotification(content, NotificationType.INFORMATION).notify(Application.project);
    }

    public static void notifyWarning(String content) {
        notify.createNotification(content, NotificationType.WARNING).notify(Application.project);
    }

    public static void notifyError(String domain) {
        String content = domain + " A fatal error occurred, Please contact plugin author";
        notify.createNotification(content, NotificationType.ERROR).notify(Application.project);
    }

}
