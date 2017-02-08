package ua.motofun.rss.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * После перезагрузки устройства стартуем процесс обновления
 * ленты {@link ua.motofun.rss.receiver.RefreshRssReceiver}
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 28.03.2015
 */
public class BootCompletedReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (!TextUtils.isEmpty(action)
				&& Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())) {
				RefreshRssReceiver.start(context);
		}
	}
}
