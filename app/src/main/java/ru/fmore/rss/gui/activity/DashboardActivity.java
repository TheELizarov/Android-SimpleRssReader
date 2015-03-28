package ru.fmore.rss.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import ru.fmore.rss.R;
import ru.fmore.rss.model.NavigationItem;
import ru.fmore.rss.receiver.RefreshRssReceiver;

/**
 * Экран навигации внутри приложения, реализуется ui dashboard для навигации
 * между экранами, реализация поддерживает динамическое добавление/удаление
 * пунктов навигации {@link ru.fmore.rss.model.NavigationItem}
 */
public class DashboardActivity extends AbstractActivity {
    public static void startActivity(Context context) {
        Intent start = new Intent(context, DashboardActivity.class);
        context.startActivity(start);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        RefreshRssReceiver.start(this);
        displayHome(false);
        findViews();
    }

    private void findViews() {
        GridView grid = (GridView) findViewById(android.R.id.list);
        grid.setEmptyView(findViewById(android.R.id.empty));
        grid.setAdapter(NavigationItem.getAdapter(this));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                switch ((int)id) {
                    case NavigationItem.RSS_LIST_ID:
                        RssListActivity.startActivity(DashboardActivity.this);
                        break;
                    case NavigationItem.SETTINGS_ID:
                        SettingsActivity.startActivity(DashboardActivity.this);
                        break;
                }
            }
        });
    }
}
