package ua.motofun.rss.gui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ua.motofun.rss.R;

/**
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        start();
    }

    /**
     * Стартуем анимацию, продолжительностью 2 секунды
     * по окончании анимации закрываем стартуем
     * главный экран и закрываем текущий
     */
    private void start() {
        final View v = findViewById(R.id.title);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_fade_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                DashboardActivity.startActivity(SplashActivity.this);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        v.startAnimation(animation);
    }
}
