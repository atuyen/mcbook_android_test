package com.mc.books.activity;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

import com.bon.util.ActivityUtils;
import com.mc.books.R;
import com.mc.common.activities.BaseAppCompatActivity;

import butterknife.BindView;
import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends BaseAppCompatActivity {
    @BindView(R.id.progress)
    GifImageView progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        //go to signin
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                ActivityUtils.startActivity(SignInActivity.class);
                finish();
            },  1000);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.splash_activity;
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return null;
    }
}
