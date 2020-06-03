package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.models.home.DetailQuestion;
import com.mc.utilities.AppUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.timfreiheit.mathjax.android.MathJaxView;

import static com.mc.utilities.Constant.AUDIO;
import static com.mc.utilities.Constant.IMAGE;
import static com.mc.utilities.Constant.VIDEO;

public class NoteAnswerDialog extends Dialog implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.llNoteAnswer)
    LinearLayout llNoteAnswer;
    @BindView(R.id.imgNoteAnswerContent)
    ImageView imgNoteAnswerContent;
    @BindView(R.id.imgNoteAnswerMedia)
    ImageView imgNoteAnswerMedia;
    @BindView(R.id.txtNoteAnswerContent)
    ExtTextView txtNoteAnswerContent;
    @BindView(R.id.llNoteAnswerRoot)
    LinearLayout llNoteAnswerRoot;
    @BindView(R.id.noteMathjax)
    MathJaxView noteMathjax;
    private DetailQuestion detailQuestion;
    private FragmentActivity fragmentActivity;
    MediaPlayer mediaPlayer;
    Context context;

    public NoteAnswerDialog(@NonNull Context context, int themeResId, DetailQuestion detailQuestion, FragmentActivity fragmentActivity) {
        super(context, themeResId);
        this.context = context;
        this.detailQuestion = detailQuestion;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.note_answer_dialog);
            ButterKnife.bind(this);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            imgNoteAnswerContent.setVisibility(View.GONE);
            imgNoteAnswerMedia.setVisibility(View.GONE);
            if (StringUtils.isEmpty(detailQuestion.getNote()))
                llNoteAnswerRoot.setVisibility(View.GONE);
            else {
                if (AppUtils.isMathJax(detailQuestion.getNote())) {
                    txtNoteAnswerContent.setVisibility(View.GONE);
                    noteMathjax.setVisibility(View.VISIBLE);
                    noteMathjax.setInputText(detailQuestion.getNote());
                } else {
                    txtNoteAnswerContent.setVisibility(View.VISIBLE);
                    noteMathjax.setVisibility(View.GONE);
                    txtNoteAnswerContent.setText(Html.fromHtml(detailQuestion.getNote()));
                }
            }

            if (detailQuestion.getNoteMediaType() != null) {
                switch (detailQuestion.getNoteMediaType()) {
                    case VIDEO:
                        imgNoteAnswerMedia.setVisibility(View.VISIBLE);
                        imgNoteAnswerMedia.setImageResource(R.drawable.ic_media_play);
                        break;
                    case AUDIO:
                        imgNoteAnswerMedia.setVisibility(View.VISIBLE);
                        imgNoteAnswerMedia.setImageResource(R.drawable.ic_audio_play);
                        break;
                    case IMAGE:
                        imgNoteAnswerContent.setVisibility(View.VISIBLE);
                        AppUtils.setImageGlide(getContext(), detailQuestion.getNoteMediaUri(), R.drawable.ic_img_book_default, imgNoteAnswerContent);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.imgClose, R.id.imgNoteAnswerContent, R.id.imgNoteAnswerMedia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                dismiss();
                break;
            case R.id.imgNoteAnswerMedia:
                if (detailQuestion.getNoteMediaType().equals(AUDIO)) playMedia();
                else if (detailQuestion.getNoteMediaType().equals(VIDEO))
                    showVideoDialog(detailQuestion.getNoteMediaUri());
                break;
        }
    }

    private void playMedia() {
        if (mediaPlayer.isPlaying()) {
            imgNoteAnswerMedia.setImageResource(R.drawable.ic_audio_play);
            mediaPlayer.stop();
        } else {
            try {
                mediaPlayer.reset();
                imgNoteAnswerMedia.setImageResource(R.drawable.ic_pause_3);
                mediaPlayer.setDataSource(context, Uri.parse(detailQuestion.getNoteMediaUri()), AppContext.getHeader());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void showVideoDialog(String url) {
        VideoDialog videoDialog = new VideoDialog(getContext(), url, detailQuestion.getName(), android.R.style.Theme_Light, fragmentActivity);
        videoDialog.show();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e("onCompletion", "onCompletion");
        imgNoteAnswerMedia.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_audio_play));
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e("onCompletion", "onCompletion");
    }

    @Override
    public void onDetachedFromWindow() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDetachedFromWindow();
    }
}
