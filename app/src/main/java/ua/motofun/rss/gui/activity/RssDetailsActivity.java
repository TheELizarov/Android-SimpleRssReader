package ua.motofun.rss.gui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import ua.motofun.rss.R;
import ua.motofun.rss.model.Phone;
import ua.motofun.rss.model.Rss;

/**
 * Экран детальной информации о новости
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class RssDetailsActivity extends AbstractAppCompatActivity {
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
        actionBar.setTitle(rss.getTitle());

        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + rss.getDescription(), "text/html", "utf-8", null);;

        findViewById(R.id.browser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = rss.getLink().replace("\t", "").replace("\n", "");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                try {
                    startActivity(intent);
                } catch (Exception ignored) {
                }
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone();
            }
        });
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, rss.getTitle() + ". Подробности можно узнать тут " + rss.getLink());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void phone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(Phone.getAdapter(this), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Phone phone = Phone.getPhones().get(i);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone.getNumber()));
                    startActivity(intent);
                }  catch (Exception ignored) {
                    Toast.makeText(RssDetailsActivity.this, "Не удается позвонить", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
}
