package ua.motofun.rss.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ua.motofun.rss.R;
import ua.motofun.rss.adapter.RssAdapter;
import ua.motofun.rss.controller.NotificationController;
import ua.motofun.rss.controller.ServiceApiController;
import ua.motofun.rss.http.HttpUtils;
import ua.motofun.rss.manager.ChanelManager;
import ua.motofun.rss.manager.RssManager;
import ua.motofun.rss.manager.SettingsManager;
import ua.motofun.rss.model.Chanel;
import ua.motofun.rss.model.Db;
import ua.motofun.rss.model.Rss;

/**
 * Экран списка новостей
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class RssListActivity extends AbstractAppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final int LOADER_ID = 1;
    private static final String EXTRA_FOR_VIEW = "extra_for_view";

    public static Intent getStartIntentForViewingRss(Context context) {
        Intent result = new Intent(context, RssListActivity.class);
        result.putExtra(EXTRA_FOR_VIEW, true);
        return result;
    }

    public static void startActivity(Context context) {
        Intent start = new Intent(context, RssListActivity.class);
        context.startActivity(start);
    }

    private SwipeRefreshLayout ptr;
    private TextView empty;
    private RssAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pull_to_refresh_list);
        setTitle();
        init();
        if (!isForViewing()) {
            loadRss();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Chanel chanel = SettingsManager.getSelectedChanel(this);
        return new CursorLoader(this,
                Db.URI_RSS,
                null,
                Db.Rss.CHANEL_ID + "=?",
                new String[] {String.valueOf(chanel.getId())},
                Db.Rss.PUB_DATE + " desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        refreshRss(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        refreshRss(null);
    }

    private boolean isForViewing() {
        return getIntent().getBooleanExtra(EXTRA_FOR_VIEW, false);
    }

    private void init() {
        ptr = (SwipeRefreshLayout) findViewById(R.id.ptr_layout);
        ptr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRss();
            }
        });

        ListView listView = (ListView) findViewById(android.R.id.list);
        empty = (TextView) findViewById(android.R.id.empty);
        empty.setText(R.string.empty_rss_new_list);
        listView.setEmptyView(empty);
        adapter = new RssAdapter(this, null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RssManager.setViewed(RssListActivity.this, l);
                Rss rss = RssManager.get(RssListActivity.this, l);
                RssDetailsActivity.startActivity(RssListActivity.this, rss);
            }
        });
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void refreshRss(Cursor cursor) {
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    private void setTitle() {
        Chanel chanel = ChanelManager.getCurrentChanel(this);
        actionBar.setTitle(String.format(getString(R.string.title_rss_list),chanel.getType(), chanel.getName()));
    }

    private void loadRss() {
        ptr.setRefreshing(true);
        showProgress(getString(R.string.refresh_news));
        ServiceApiController.RssListener listener = new ServiceApiController.RssListener() {
            @Override
            public void onLoaded(final List<Rss> loadedRss) {
                RssManager.RssSaveListener listener = new RssManager.RssSaveListener() {
                    @Override
                    public void onBefore() {
                        /**
                         * На вермя добавления в бд, отключаем лоадер
                         * чтобы устранить "подвисание" ui
                         */
                        getSupportLoaderManager().destroyLoader(LOADER_ID);
                        empty.setText(R.string.updating_news_list);
                    }

                    @Override
                    public void onAfter(int newRssCount) {
                        try {
                            if (newRssCount > 0) {
                                NotificationController.hasNewRss(RssListActivity.this, newRssCount);
                            }
                            hideProgress();
                            ptr.setRefreshing(false);
                            getSupportLoaderManager().initLoader(LOADER_ID, null, RssListActivity.this);
                        } catch (Exception ignored) {
                        }
                    }
                };
                RssManager.save(RssListActivity.this, loadedRss, listener);
            }

            @Override
            public void onError(HttpUtils.HttpResult result) {
                if (result != null) {
                    String message = String.format(getString(R.string.error_loading), result.getMessage());
                    Toast.makeText(RssListActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                hideProgress();
                ptr.setRefreshing(false);
                getSupportLoaderManager().restartLoader(LOADER_ID, null, RssListActivity.this);
            }
        };
        ServiceApiController.loadRss(this, listener);
    }
}
