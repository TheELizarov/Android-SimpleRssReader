package ua.motofun.rss.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Структура данных, инкапсулирует новостные данные rss
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class Rss implements Serializable {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    /**
     * chanelId - идентификатор канала, к которому относится новость
     * pubDate - дата публикации новости берется зи описания rss
     * created - время добавления в бд
     */
    private long id, chanelId, created, pubDate;
    /**
     * данные из rrs
     * rssId - guid новости
     */
    private String title, description, link, rssId;
    /**
     * отметка просмотра новости
     */
    private boolean viewed;

    /**
     * Курсок должен быть установлен на нужной позиции,
     * после создания объекта курсор закрывать извне, не в конструкторе
     * @param c "подготовленный" курсок
     */
    public Rss(Cursor c) {
        id = c.getLong(c.getColumnIndex(BaseColumns._ID));
        chanelId = c.getLong(c.getColumnIndex(Db.Rss.CHANEL_ID));
        title = c.getString(c.getColumnIndex(Db.Rss.TITLE));
        description = c.getString(c.getColumnIndex(Db.Rss.DESCRIPTION));
        link = c.getString(c.getColumnIndex(Db.Rss.LINK));
        rssId = c.getString(c.getColumnIndex(Db.Rss.RSS_ID));
        created = c.getLong(c.getColumnIndex(Db.Rss.CREATED));
        pubDate = c.getLong(c.getColumnIndex(Db.Rss.PUB_DATE));
        viewed = 1 == c.getInt(c.getColumnIndex(Db.Rss.VIEWED));
    }

    /**
     * содаем объект из xml описания
     * @param item узел xml, описывающий новость
     * @param chanelId идентификатор канала rss
     */
    public Rss(Node item, long chanelId) {
        NodeList items = item.getChildNodes();
        for (int j = 0; j < items.getLength(); ++j) {
            Node child = items.item(j);
            String tag = child.getNodeName();
            if ("title".equalsIgnoreCase(tag)) {
                title = child.getFirstChild().getNodeValue();
                continue;
            }
            if ("guid".equalsIgnoreCase(tag)) {
                rssId = child.getFirstChild().getNodeValue();
                continue;
            }
            if ("link".equalsIgnoreCase(tag)) {
                link = child.getFirstChild().getNodeValue();
                continue;
            }
            if ("pubDate".equalsIgnoreCase(tag)) {
                pubDate = convertToLong(child.getFirstChild().getNodeValue());
                continue;
            }
            if ("description".equalsIgnoreCase(tag)) {
                description = child.getFirstChild().getNodeValue();
            }
        }
        created = System.currentTimeMillis();
        this.chanelId = chanelId;
    }

    public ContentValues asContentValues() {
        ContentValues result = new ContentValues(5);
        result.put(Db.Rss.CHANEL_ID, chanelId);
        result.put(Db.Rss.TITLE, title);
        result.put(Db.Rss.DESCRIPTION, description);
        result.put(Db.Rss.LINK, link);
        result.put(Db.Rss.VIEWED, viewed ? 1 : 0);
        result.put(Db.Rss.CREATED, created);
        result.put(Db.Rss.PUB_DATE, pubDate);
        result.put(Db.Rss.RSS_ID, rssId);
        return result;
    }

    public long getId() {
        return id;
    }

    public long getChanelId() {
        return chanelId;
    }

    public String getTitle() {
        return title;
    }

    public Rss setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Rss setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Rss setLink(String link) {
        this.link = link;
        return this;
    }

    public boolean isViewed() {
        return viewed;
    }

    public Rss setViewed(boolean viewed) {
        this.viewed = viewed;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public Rss setCreated(long created) {
        this.created = created;
        return this;
    }

    public String getRssId() {
        return rssId;
    }

    public Rss setRssId(String rssId) {
        this.rssId = rssId;
        return this;
    }

    public boolean compareByPubDate(Rss other) {
        boolean result = false;
        if (other != null) {
            result = compareToDay(pubDate, other.pubDate);
        }
        return result;
    }

    public static boolean compareToDay(long date1, long date2) {
        return sdf.format(date1).equalsIgnoreCase(sdf.format(date2));
    }

    public static ContentValues asContentValuesForViewed() {
        ContentValues result = new ContentValues(1);
        result.put(Db.Rss.VIEWED, 1);
        return result;
    }

    private long convertToLong(String pubDate) {
        long result = System.currentTimeMillis();
        try {
            result = sdf2.parse(pubDate).getTime();
        } catch (ParseException ignored) {
        }
        return result;
    }

    public Long getPubDate() {
        return pubDate;
    }

    public Rss setPubDate(long pubDate) {
        this.pubDate = pubDate;
        return this;
    }
}
