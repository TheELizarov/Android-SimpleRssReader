package ru.fmore.rss.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Структура данных, инкапсулирует информацию об rss канале
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class Chanel implements Serializable {
    private long id;
    /**
     * Описание канала
     * name - имя,
     *
     * type - наименование,
     * используемое для группировки rss лент,
     * принадлежащий одному порталу
     *
     * link - ссылка для получения rrs ленты
     */
    private String name, type, link;
    /**
     * метка выбранной темы, канала
     */
    private boolean checked;

    /**
     * Создание объекта из курсора аналогично {@link ru.fmore.rss.model.Rss}
     * @param c
     */
    public Chanel(Cursor c) {
        id = c.getLong(c.getColumnIndex(BaseColumns._ID));
        name = c.getString(c.getColumnIndex(Db.Chanel.NAME));
        type = c.getString(c.getColumnIndex(Db.Chanel.TYPE));
        link = c.getString(c.getColumnIndex(Db.Chanel.LINK));
        checked = 1 == c.getInt(c.getColumnIndex(Db.Chanel.CHECKED));
    }

    public ContentValues asContentValues() {
        ContentValues result = new ContentValues(5);
        result.put(Db.Chanel.NAME, name);
        result.put(Db.Chanel.TYPE, type);
        result.put(Db.Chanel.LINK, link);
        result.put(Db.Chanel.CHECKED, checked ? 1 : 0);
        return result;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Chanel setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Chanel setType(String type) {
        this.type = type;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Chanel setLink(String link) {
        this.link = link;
        return this;
    }

    public boolean isChecked() {
        return checked;
    }

    public Chanel setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public static ContentValues asContentValuesForCurrent(boolean enable) {
        ContentValues result = new ContentValues(1);
        result.put(Db.Chanel.CHECKED, enable ? 1 : 0);
        return result;
    }

    public boolean compare(Chanel other) {
        return other != null && type.equalsIgnoreCase(other.type);
    }
}
