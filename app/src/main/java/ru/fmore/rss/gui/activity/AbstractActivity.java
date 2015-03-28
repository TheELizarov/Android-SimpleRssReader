package ru.fmore.rss.gui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;

/**
 * Инкапсулирует общие методы для всех экранов
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public abstract class AbstractActivity extends ActionBarActivity {
    protected ActionBar actionBar;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        displayHome(true);
    }

    protected void displayHome(boolean enable) {
        actionBar.setHomeButtonEnabled(enable);
        actionBar.setDisplayHomeAsUpEnabled(enable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showProgress() {
        showProgress(null);
    }

    protected void showProgress(String message) {
        progressDialog = new ProgressDialog(this);
        if (!TextUtils.isEmpty(message)) {
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    protected void hideProgress() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception ignored) {
        }
    }
}
