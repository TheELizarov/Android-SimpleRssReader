package ru.fmore.rss.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import ru.fmore.rss.model.Chanel;
import ru.fmore.rss.model.Db;

/**
 * Менеджер для работы с каналами, инкапсулирует логику:
 * 1) получения текущего, выбранного канала rss
 * 2) смену выбранного rss канала
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public abstract class ChanelManager {
    /**
     * Получааем текущий rss канал
     * @param context
     * @return
     */
    public static Chanel getCurrentChanel(Context context) {
        Chanel result = null;
        ContentResolver cr = context.getContentResolver();
        String[] selection = null;
        String where = Db.Chanel.CHECKED + "=?";
        String[] args = new String[] {"1"};
        String sortOrder = null;
        Cursor cursor = cr.query(Db.URI_RSS_CHANEL, selection, where, args, sortOrder);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = new Chanel(cursor);
            }
            cursor.close();
        }
        return result;
    }

    /**
     * Делаем канал rss с указанным идентификатором текущим
     * @param context
     * @param chanelId идентифкатор rss канала
     */
    public static void setCurrentChanel(Context context, long chanelId) {
        ContentResolver cr = context.getContentResolver();
        /**
         * Снимаем выделение с текущего канала
         */
        String where = null;
        String[] args = null;
        cr.update(Db.URI_RSS_CHANEL,
                Chanel.asContentValuesForCurrent(false),
                where,
                args);
        /**
         * Делаем текущим канал с идентификаторм chanelId
         */
        where = BaseColumns._ID + "=?";
        args = new String[] {String.valueOf(chanelId)};
        cr.update(Db.URI_RSS_CHANEL,
                Chanel.asContentValuesForCurrent(true),
                where,
                args);
    }
}
