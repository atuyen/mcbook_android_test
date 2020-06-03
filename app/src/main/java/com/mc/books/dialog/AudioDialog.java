package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bon.util.StringUtils;
import com.bon.util.ToastUtils;
import com.mc.books.R;

import bg.player.com.playerbackground.module.AudioPlayerBG;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioDialog extends Dialog {
    private Context context;
    private String url;
    @BindView(R.id.audioPlayerBG)
    AudioPlayerBG audioPlayerBG;
    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    private String filename;

    public AudioDialog(@NonNull Context context, String url, String fileName, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.url = url;
        this.filename = fileName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimary));
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.audio_dialog);
            ButterKnife.bind(this);
            txtTitle.setText(filename);
            audioPlayerBG.setAutoPlay(true);
            audioPlayerBG.setStreamAudio(true);

            if (StringUtils.isEmpty(url))
                ToastUtils.showToast(context, context.getString(R.string.empty_media_url));
            else
                audioPlayerBG.setAudioFileUrl(url);

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imgClose)
    void onClick() {
        dismiss();
        audioPlayerBG.stopPlayer();
    }
}
