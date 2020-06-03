package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bon.util.StringUtils;
import com.bon.util.ToastUtils;
import com.halilibo.betteraudioplayer.BetterVideoCallback;
import com.halilibo.betteraudioplayer.BetterVideoPlayer;
import com.mc.application.AppContext;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoDialog extends Dialog {
    private Context context;
    private String url;
    @BindView(R.id.videoPlayer)
    BetterVideoPlayer videoPlayer;
    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.llActionBar)
    AppBarLayout llActionBar;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    private FragmentActivity fragmentActivity;
    private String fileName;

    public VideoDialog(@NonNull Context context, String url, String fileName, int themeResId, FragmentActivity fragmentActivity) {
        super(context, themeResId);
        this.context = context;
        this.url = url;
        this.fileName = fileName;
        this.fragmentActivity = fragmentActivity;
    }

    public void pauseVideo() {
        if (videoPlayer != null) {
            videoPlayer.pause();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimary));
            setContentView(R.layout.video_dialog);
            ButterKnife.bind(this);
            txtTitle.setText(fileName);
            videoPlayer.setAutoPlay(true);

            setOnKeyListener((dialogInterface, i, keyEvent) -> {
                switch (keyEvent.getAction()) {
                    case KeyEvent.KEYCODE_HOME:
                        videoPlayer.pause();
                        return true;
                }
                return false;

            });
            if (StringUtils.isEmpty(url))
                ToastUtils.showToast(context, context.getString(R.string.empty_media_url));
             else
                videoPlayer.setSource(Uri.parse(url), AppContext.getHeader());

            videoPlayer.setBackground();
            videoPlayer.setBottomProgressBarVisibility(false);
            videoPlayer.setCallback(new BetterVideoCallback() {
                @Override
                public void onStarted(BetterVideoPlayer player) {

                }

                @Override
                public void onPaused(BetterVideoPlayer player) {

                }

                @Override
                public void onPreparing(BetterVideoPlayer player) {

                }

                @Override
                public void onPrepared(BetterVideoPlayer player) {

                }

                @Override
                public void onBuffering(int percent) {

                }

                @Override
                public void onError(BetterVideoPlayer player, Exception e) {

                }

                @Override
                public void onCompletion(BetterVideoPlayer player) {

                }

                @Override
                public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {

                }

                @Override
                public void onFullScreen(boolean isFullScreen) {
                    showFullScreen(isFullScreen);
                }

                @Override
                public void onNext() {

                }

                @Override
                public void onPrevious() {

                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showFullScreen(boolean isFullScreen) {
        try {
            if (!isFullScreen) {
                llActionBar.setVisibility(View.GONE);
                fragmentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                videoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            } else {
                fragmentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                llActionBar.setVisibility(View.VISIBLE);
                videoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        fragmentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        llActionBar.setVisibility(View.VISIBLE);
        videoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }


    @OnClick(R.id.imgClose)
    void onClick() {
        dismiss();
        videoPlayer.stop();
        videoPlayer.release();
    }

    @Override
    protected void onStop() {
        videoPlayer.pause();
        super.onStop();
    }
}
