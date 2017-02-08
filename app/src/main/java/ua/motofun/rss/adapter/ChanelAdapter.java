package ua.motofun.rss.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.motofun.rss.R;
import ua.motofun.rss.model.Chanel;

/**
 * Адаптер для отображения списка rss каналов
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class ChanelAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int HEADER = 0;
    private static final int ROW = 1;

    public ChanelAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        View v = null;
        switch (getItemViewType(cursor.getPosition())) {
            case HEADER:
                v = View.inflate(context, R.layout.item_header, null);
                vh.type = (TextView) v.findViewById(R.id.header);
                break;
            case ROW:
                v = View.inflate(context, R.layout.item, null);
                break;
        }
        vh.name = (TextView) v.findViewById(R.id.title);
        vh.link = (TextView) v.findViewById(R.id.subtitle);
        vh.item = v.findViewById(R.id.item);
        v.setTag(vh);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();
        Chanel chanel = new Chanel(cursor);
        vh.name.setText(chanel.getName());
        vh.link.setText(chanel.getLink());
        if (getItemViewType(cursor.getPosition()) == HEADER) {
            vh.type.setText(chanel.getType().toUpperCase());
        }
        int color = chanel.isChecked() ? R.color.blue_alpha_90 : android.R.color.transparent;
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
            Chanel current = new Chanel(getCursor());
            getCursor().moveToPrevious();
            Chanel prev = new Chanel(getCursor());
            getCursor().moveToPosition(position);
            if (current.compare(prev)) {
                result = ROW;
            }
        }
        return result;
    }

    private class ViewHolder {
        View item;
        TextView name, type, link;
    }
}
