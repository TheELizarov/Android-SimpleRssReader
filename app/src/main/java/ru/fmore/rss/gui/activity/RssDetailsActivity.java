package ru.fmore.rss.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.fmore.rss.R;
import ru.fmore.rss.model.Rss;

/**
 * Экран детальной информации о новости
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class RssDetailsActivity extends AbstractActivity {
    private static final String EXTRA_RSS = "extra_rss";

    public static void startActivity(Context context, Rss rss) {
        Intent start = new Intent(context, RssDetailsActivity.class);
        start.putExtra(EXTRA_RSS, rss);
        context.startActivity(start);
    }

    private Rss rss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_details);

        rss = (Rss) getIntent().getSerializableExtra(EXTRA_RSS);

        final WebView webView = (WebView) findViewById(R.id.webView);
        actionBar.setTitle(rss.getTitle());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return false;
            }
        });
        webView.loadUrl(rss.getLink());
    }
}
