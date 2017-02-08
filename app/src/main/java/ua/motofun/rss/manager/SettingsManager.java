package ua.motofun.rss.manager;

import android.content.Context;
import android.content.SharedPreferences;

import ua.motofun.rss.model.Chanel;

/**
 * Менеджер для упраления настройками приложения
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class SettingsManager {
    public static final int MILLISECONDS = 60 * 1000;
    public static final int DEFAULT_SYNC_INTERVAL = 5;
    private static final String PREFS = "setting_manager_prefs";
    private static final String SYNC_INTERVAL = "sync_interval";
    private static final String SYNC_TIME = "sync_time";

    /**
     * Сохраняем время синхронизации rss в миллисекундах
     * @param context
     * @param syncInterval интервал синхронизации в минтуах
     */
    public static void setSyncInterval(Context context, int syncInterval) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putLong(SYNC_INTERVAL, syncInterval * MILLISECONDS).apply();
    }

    /**
     * Получаем интервал синхронизации в миллисекундах
     * @param context
     * @return миллисекунды
     */
    public static long getSyncInterval(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getLong(SYNC_INTERVAL, DEFAULT_SYNC_INTERVAL);
    }

    /**
     * Интервал синхронизации в минутах
     * @param context
     * @return количество минут
     */
    public static int getSyncIntervalInMinutes(Context context) {
        return (int) getSyncInterval(context) / MILLISECONDS;
    }

    /**
     * Увеличеваем время синхронизации на величину интервала синхронизации
     * @param context
     */
    public static void incrementSyncTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        long nextSyncTime = System.currentTimeMillis() + getSyncInterval(context);
        prefs.edit().putLong(SYNC_TIME, nextSyncTime).apply();
    }

    /**
     * Получаем интервал синхронизации
     * @param context
     */
    public static boolean isSyncTimeExpired(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        long current = System.currentTimeMillis();
        long syncTime = prefs.getLong(SYNC_TIME, current);
        return syncTime <= current;
    }

    /**
     * На случай, если понадобиться стереть настройки
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    /**
     * Получаем текущий, выбранный канал rss
     * @param context
     * @return данные, описывающие rss канал
     */
    public static Chanel getSelectedChanel(Context context) {
        return ChanelManager.getCurrentChanel(context);
    }
}
