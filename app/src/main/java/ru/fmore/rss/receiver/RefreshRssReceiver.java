package ru.fmore.rss.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import java.util.List;

import ru.fmore.rss.R;
import ru.fmore.rss.controller.NotificationController;
import ru.fmore.rss.controller.ServiceApiController;
import ru.fmore.rss.http.HttpUtils;
import ru.fmore.rss.manager.RssManager;
import ru.fmore.rss.manager.SettingsManager;
import ru.fmore.rss.model.Rss;


/**
 * Для периодического опроса сервиса на
 * наличие новых rss используем alarm manager.
 * При срабатывании делается запрос на получение rss ленты,
 * В случае обнаружения новых новостей пускаем нотификацию
 * Если пользователь перезагруил телефон, то стартуем работу
 * RefreshRssRecуiver {@link ru.fmore.rss.receiver.BootCompletedReceiver}
 *
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 28.03.2015
 */
public class RefreshRssReceiver extends BroadcastReceiver {
    public static final long SYNC_REFRESH_RSS = 2 * 60 * 1000;
	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wl.acquire();
		execute(context);
		wl.release();
	}

	private void execute(final Context context) {
        if (SettingsManager.isSyncTimeExpired(context)) {
            refreshRss(context);
            SettingsManager.incrementSyncTime(context);
        }
	}

    private void refreshRss(final Context context) {
        ServiceApiController.RssListener listener = new ServiceApiController.RssListener() {
            @Override
            public void onLoaded(final List<Rss> loadedRss) {
                RssManager.RssSaveListener listener = new RssManager.RssSaveListener() {
                    @Override
                    public void onBefore() {
                    }

                    @Override
                    public void onAfter(int newRssCount) {
                        try {
                            if (newRssCount > 0) {
                                NotificationController.hasNewRss(context, newRssCount);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                };
                RssManager.save(context, loadedRss, listener);
            }

            @Override
            public void onError(HttpUtils.HttpResult result) {
            }
        };
        ServiceApiController.loadRss(context, listener);
    }

	public static void start(Context context) {
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, RefreshRssReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), SYNC_REFRESH_RSS, pi);
	}

	public static void stop(Context context) {
		Intent intent = new Intent(context, RefreshRssReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}
