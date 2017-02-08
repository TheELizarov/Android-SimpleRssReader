package ua.motofun.rss.gui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import ua.motofun.rss.R;
import ua.motofun.rss.adapter.ChanelAdapter;
import ua.motofun.rss.manager.ChanelManager;
import ua.motofun.rss.manager.SettingsManager;
import ua.motofun.rss.model.Db;

/**
 * Экран настроек приложения
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class SettingsActivity extends AbstractAppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int LOADER_ID = 1;

    public static void startActivity(Context context) {
        Intent start = new Intent(context, SettingsActivity.class);
        context.startActivity(start);
    }

    private EditText syncInterval;
    private ChanelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, Db.URI_RSS_CHANEL, null, null, null, Db.Chanel.TYPE + " asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        refreshChannels(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        refreshChannels(null);
    }

    @Override
    public void onBackPressed() {
        int value = 0;
        try {
            value = Integer.parseInt(syncInterval.getText().toString().trim());
        } catch (NumberFormatException ignored) {
        }
        if (value < SettingsManager.DEFAULT_SYNC_INTERVAL) {
            showWarningSyncIntervalDialog();
        } else {
            SettingsManager.setSyncInterval(this, value);
            super.onBackPressed();
        }
    }

    private void refreshChannels(Cursor cursor) {
        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    private void init() {
        syncInterval = (EditText) findViewById(R.id.syncInterval);
        int syncIntervalValue = SettingsManager.getSyncIntervalInMinutes(this);
        if (syncIntervalValue < SettingsManager.DEFAULT_SYNC_INTERVAL) {
            syncIntervalValue = SettingsManager.DEFAULT_SYNC_INTERVAL;
        }
        syncInterval.setText(String.valueOf(syncIntervalValue));

        ListView channels = (ListView) findViewById(android.R.id.list);
        channels.setEmptyView(findViewById(android.R.id.empty));
        adapter = new ChanelAdapter(this, null);
        channels.setAdapter(adapter);
        channels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long chanelId) {
                ChanelManager.setCurrentChanel(SettingsActivity.this, chanelId);
            }
        });
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void showWarningSyncIntervalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = String.format(getString(R.string.wraning_wrong_sync_inteval),
                SettingsManager.DEFAULT_SYNC_INTERVAL);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingsManager.setSyncInterval(SettingsActivity.this,
                        SettingsManager.DEFAULT_SYNC_INTERVAL);
                finish();
            }
        });
        builder.setNegativeButton(R.string.change, null);
        builder.show();
    }
}
