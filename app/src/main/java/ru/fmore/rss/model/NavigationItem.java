package ru.fmore.rss.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.fmore.rss.R;

/**
 * Структура данных, описывающая пункты для навигации внутри приложении
 * Каждый пункт может быть описан так:
 * 1) id - для однозначной идентификации пункта меню;
 * 2) идентификатор иконки (если задать -1, то будет использована ионка по умолчанию) из ресурсов
 * 3) идентификатор заголовока пункта из ресурсов
 * Массив ITEMS - список пунктов по умолчанию, сюда можно добавить любые пункты,
 * они диначиески отрисуются на экране
 * Данную структуру удобно исопльзовать для
 * различных ui паттернов андроида (dashboard или navigation drawer, или др.)
 * Используется на экране {@link ru.fmore.rss.gui.activity.DashboardActivity}
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class NavigationItem {
    public static final int RSS_LIST_ID = 0;
    public static final int SETTINGS_ID = 1;

    public static NavigationItem[] ITEMS = new NavigationItem[] {
        new NavigationItem(RSS_LIST_ID, R.drawable.news, R.string.title_dashboard_rss_list),
        new NavigationItem(SETTINGS_ID, R.drawable.settings, R.string.title_settings)
    };

    /**
     * Адаптер списка пунктов по умолчанию
     * @param context
     * @return
     */
    public static ArrayAdapter getAdapter(Context context) {
        return new Adapter(context, ITEMS);
    }

    private int id;
    private int icon;
    private int title;

    public NavigationItem(int id, int icon, int title) {
        this.id = id;
        this.icon = icon;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    /**
     * Для многих паттернов UI в андроиде (dashboard или navigation drawer)
     * потребуется объект адаптера, поэтому он икапсулирован в данном классе
     */
    private static class Adapter extends ArrayAdapter<NavigationItem> {
        public Adapter(Context context, NavigationItem[] objects) {
            super(context, -1, objects);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_dashboard, null);
            }
            NavigationItem navigationItem = getItem(position);
            displayNavigationTitle(convertView, navigationItem);
            displayNavigationIcon(convertView, navigationItem);
            return convertView;
        }

        private void displayNavigationTitle(View v, NavigationItem navigationItem) {
            TextView title = (TextView) v.findViewById(android.R.id.title);
            title.setText(navigationItem.getTitle());
        }

        private void displayNavigationIcon(View v, NavigationItem navigationItem) {
            ImageView icon = (ImageView) v.findViewById(android.R.id.icon);
            if (navigationItem.getIcon() != -1) {
                icon.setImageResource(navigationItem.getIcon());
            }
        }
    }
}
