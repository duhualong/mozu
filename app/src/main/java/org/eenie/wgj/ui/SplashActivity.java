package org.eenie.wgj.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import org.eenie.wgj.R;
import org.eenie.wgj.ui.login.LoginActivity;
import org.eenie.wgj.util.RxUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Single;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splish_pager);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hideSystemUI();
        }

        mProgressBar.setVisibility(View.VISIBLE);
        Single.just("").delay(3, TimeUnit.SECONDS).compose(RxUtils.applySchedulers()).subscribe(s -> {
            mProgressBar.setVisibility(View.GONE);
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            SplashActivity.this.finish();
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT) private void hideSystemUI() {
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}