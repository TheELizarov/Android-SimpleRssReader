package ua.motofun.rss.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ua.motofun.rss.R;
import ua.motofun.rss.model.Rss;
import ua.motofun.rss.utils.TypefaceUtils;

/**
 * Адаптер для отображения списка новостей
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class RssAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int HEADER = 0;
    private static final int ROW = 1;

    private Typeface light, medium, bold;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

    public RssAdapter(Context context, Cursor c) {
        super(context, c, true);
        light = TypefaceUtils.getRobotoLight(context);
        medium = TypefaceUtils.getRobotoRegular(context);
        bold = TypefaceUtils.getRobotoBold(context);
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
        vh.description.setText(Html.fromHtml(rss.getDescription().replaceAll("<img.+?>", "")));
        if (getItemViewType(cursor.getPosition()) == HEADER) {
            vh.header.setText(sdf.format(new Date(rss.getPubDate())));
        }
        int color = rss.isViewed() ? R.color.viewed_item_background : R.color.item_background;
        vh.item.setBackgroundColor(context.getResources().getColor(color));

        vh.title.setTypeface(rss.isViewed() ? medium : bold);
        vh.description.setTypeface(rss.isViewed() ? light : medium);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        /* если хотите группировку по датам и вывод даты, то раскомментируйте это
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
        */
        return ROW;
    }

    private class ViewHolder {
        View item;
        TextView title, description, header;
    }
}
