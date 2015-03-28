package ru.fmore.rss.model;

import android.net.Uri;

/**
 * Константы для бд
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public interface Db {
    String AUTHORITY = "ru.fmore.rss.dbprovider";

    String TABLE_RSS = "rss";
    String TABLE_CHANEL = "chanel";

    public Uri URI_RSS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_RSS);
    public Uri URI_RSS_CHANEL = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CHANEL);

    public interface Rss {
        String CHANEL_ID = "chanel_id";
        String RSS_ID = "rss_id";
        String TITLE = "title";
        String DESCRIPTION = "description";
        String LINK = "link";
        String CREATED = "created";
        String PUB_DATE = "pub_date";
        String VIEWED = "viewed";
    }

    public interface Chanel {
        String NAME = "name";
        String TYPE = "type";
        String LINK = "link";
        String CHECKED = "checked";
    }
}
