package ua.motofun.rss.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import ua.motofun.rss.R;
import ua.motofun.rss.model.NavigationItem;
import ua.motofun.rss.receiver.RefreshRssReceiver;

/**
 * Экран навигации внутри приложения, реализуется ui dashboard для навигации
 * между экранами, реализация поддерживает динамическое добавление/удаление
 * пунктов навигации {@link ua.motofun.rss.model.NavigationItem}
 */
public class DashboardActivity extends AbstractAppCompatActivity {
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
        actionBar.setTitle("MOTOFUN.COM.UA");
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
                        //SettingsActivity.startActivity(DashboardActivity.this);
                        try {
                            double latitude = 49.9910582;
                            double longitude = 36.2321177;
                            String label = "Наш  магазин";
                            String uriBegin = "geo:" + latitude + "," + longitude;
                            String query = latitude + "," + longitude + "(" + label + ")";
                            String encodedQuery = Uri.encode(query);
                            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                            Uri uri = Uri.parse(uriString);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                            startActivity(intent);
                        } catch (Exception ignored) {
                        }
                        break;
                }
            }
        });
    }
}
