package ru.fmore.rss.controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import ru.fmore.rss.R;
import ru.fmore.rss.gui.activity.DashboardActivity;
import ru.fmore.rss.gui.activity.RssListActivity;

/**
 * Контроллер инкапсулирует создание уведовмлений для пользователя
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 28.03.2015
 */
public abstract class NotificationController {
    public static int notificationId = 0;

    /**
     * Оповещаем пользователя о том, что доступно такое-то  количество новостей
     * @param context
     * @param countNewRss количество новых блоков rss
     */
    public static void hasNewRss(Context context, int countNewRss) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = RssListActivity.getStartIntentForViewingRss(context);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DashboardActivity.class);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent contentIntent = stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.rss)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.news))
                        .setContentTitle(context.getString(R.string.available_new_rss))
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentText(String.format(context.getString(R.string.title_notify, countNewRss)))
                        .setSubText(context.getString(R.string.subtitle_notify));

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
