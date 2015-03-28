package ru.fmore.rss.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import java.util.List;

import ru.fmore.rss.model.Chanel;
import ru.fmore.rss.model.Db;
import ru.fmore.rss.model.Rss;

/**
 * Менеджер дял работы с бд rss
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 24.03.2015
 */
public abstract class RssManager {
    /**
     * получение объекта rss по его идентификатору
     * @param context
     * @param rssId идентификтатор rss
     * @return rss объект
     */
    public static Rss get(Context context, long rssId) {
        Rss result = null;
        ContentResolver cr = context.getContentResolver();
        String[] selection = null;
        String where = BaseColumns._ID + "=?";
        String[] args = new String[] {String.valueOf(rssId)};
        String sortOrder = null;
        Cursor cursor = cr.query(Db.URI_RSS, selection, where, args, sortOrder);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = new Rss(cursor);
            }
            cursor.close();
        }
        return result;
    }

    /**
     * Помечаем rss как просмотренную
     * @param context
     * @param rssId идентифкатор просмотренной rrs-ки
     */
    public static void setViewed(Context context, long rssId) {
        ContentResolver cr = context.getContentResolver();
        String where = BaseColumns._ID + "=?";
        String[] args = new String[] {String.valueOf(rssId)};
        cr.update(Db.URI_RSS, Rss.asContentValuesForViewed(), where, args);
    }

    /**
     * Сохраняем список rss в бд в ui потоке
     * @param context
     * @param savedRss список rrs
     * @return количество новых rss
     */
    public static int save(Context context, List<Rss> savedRss) {
        int result = 0;
        ContentResolver cr = context.getContentResolver();
        for (Rss rss : savedRss) {
            if (!update(cr, rss)) {
                insert(cr, rss);
                ++result;
            }
        }
        return result;
    }

    /**
     * Сохраняем rss аснхронно
     * @param context
     * @param savedRss список rss
     * @param rssSaveListener callback для обработки моментов начала и окончания сохранения rss
     */
    public static void save(final Context context, final List<Rss> savedRss, final RssSaveListener rssSaveListener) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (rssSaveListener != null) {
                    rssSaveListener.onBefore();
                }
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                return save(context,savedRss);
            }

            @Override
            protected void onPostExecute(Integer newRssCount) {
                super.onPostExecute(newRssCount);
                if (rssSaveListener != null) {
                    rssSaveListener.onAfter(newRssCount);
                }
            }
        }.execute();
    }

    /**
     * ОБновление rss
     * @param cr
     * @param rss
     * @return
     */
    private static boolean update(ContentResolver cr, Rss rss) {
        String where = Db.Rss.RSS_ID + "=?";
        String[] args = new String[] {rss.getRssId()};
        ContentValues cv = rss.asContentValues();
        cv.remove(Db.Rss.VIEWED);
        return 0 < cr.update(Db.URI_RSS, cv, where, args);
    }

    /**
     * метод добавления rss в бд
     * @param cr
     * @param rss
     */
    private static void insert(ContentResolver cr, Rss rss) {
        cr.insert(Db.URI_RSS, rss.asContentValues());
    }

    public interface RssSaveListener {
        void onBefore();
        void onAfter(int newRssCount);
    }
}
