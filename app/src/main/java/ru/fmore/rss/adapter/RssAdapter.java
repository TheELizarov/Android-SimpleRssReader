package ru.fmore.rss.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.fmore.rss.R;
import ru.fmore.rss.model.Rss;

/**
 * Адаптер для отображения списка новостей
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class RssAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int HEADER = 0;
    private static final int ROW = 1;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

    public RssAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        View v = null;
        switch (getItemViewType(cursor.getPosition())) {
            case HEADER:
                v = View.inflate(context, R.layout.item_header, null);
                vh.header = (TextView) v.findViewById(R.id.header);
                break;
            case ROW:
                v = View.inflate(context, R.layout.item, null);
                break;
        }
        vh.title = (TextView) v.findViewById(R.id.title);
        vh.description = (TextView) v.findViewById(R.id.subtitle);
        vh.item = v.findViewById(R.id.item);
        v.setTag(vh);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();
        Rss rss = new Rss(cursor);
        vh.title.setText(rss.getTitle());
        vh.description.setText(rss.getDescription());
        if (getItemViewType(cursor.getPosition()) == HEADER) {
            vh.header.setText(sdf.format(new Date(rss.getPubDate())));
        }
        int color = rss.isViewed() ? R.color.gray_alpha_90 : android.R.color.transparent;
        vh.item.setBackgroundColor(context.getResources().getColor(color));
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        int result = HEADER;
        if (position > 0) {
            getCursor().moveToPosition(position);
            Rss current = new Rss(getCursor());
            getCursor().moveToPrevious();
            Rss prev = new Rss(getCursor());
            getCursor().moveToPosition(position);
            if (current.compareByPubDate(prev)) {
                result = ROW;
            }
        }
        return result;
    }

    private class ViewHolder {
        View item;
        TextView title, description, header;
    }
}
