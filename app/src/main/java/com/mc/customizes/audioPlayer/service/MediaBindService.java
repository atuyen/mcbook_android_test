package com.mc.customizes.audioPlayer.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.Log;

import com.mc.customizes.audioPlayer.event.CompleteEvent;
import com.mc.customizes.audioPlayer.event.PreparAudioEvent;
import com.mc.customizes.audioPlayer.event.UpdateCounterEvent;
import com.mc.utilities.AppUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class MediaBindService extends Binder implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer audioPlayer = null;
    private Context context = null;
    private Uri audioFileUri = null;
    private String audioFileUrl = "";
    private boolean streamAudio;
    private boolean autoPlay;
    private Handler mHandler = new Handler();
    private boolean isReplay;

    public boolean isReplay() {
        return isReplay;
    }

    public void setReplay(boolean replay) {
        isReplay = replay;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Uri getAudioFileUri() {
        return audioFileUri;
    }

    public void setAudioFileUri(Uri audioFileUri) {
        this.audioFileUri = audioFileUri;
    }

    public String getAudioFileUrl() {
        return audioFileUrl;
    }

    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    public boolean isPlaying() {
        return audioPlayer != null && audioPlayer.isPlaying();
    }

    public void setStreamAudio(boolean streamAudio) {
        this.streamAudio = streamAudio;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void initAudioPlayer() {
        try {
            if (audioPlayer == null) {
                audioPlayer = new MediaPlayer();
                audioPlayer.setOnPreparedListener(this);
                audioPlayer.setOnCompletionListener(this);
                if (!TextUtils.isEmpty(getAudioFileUrl())) {
                    if (streamAudio) {
                        audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    }
                    audioPlayer.setDataSource(getAudioFileUrl());
                } else {
                    audioPlayer.setDataSource(getContext(), getAudioFileUri());
                }

                audioPlayer.prepareAsync();
            }
        } catch (IOException e) {
            Log.e("initAudioPlayer", e.toString());
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (autoPlay) {
            start();
        }
        EventBus.getDefault().post(new PreparAudioEvent(audioPlayer.getDuration()));
    }

    public void seekTo(@IntRange(from = 0, to = Integer.MAX_VALUE) int pos) {
        audioPlayer.seekTo(pos);
    }

    public void start() {
        if (audioPlayer != null) {
            audioPlayer.start();
        }
        mHandler.post(mUpdateCounters);
    }

    public void pause() {
        if (audioPlayer != null) {
            audioPlayer.pause();
        }
        if (mHandler == null) {
            return;
        }
        mHandler.removeCallbacks(mUpdateCounters);
    }

    public void stop() {
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
        if (mHandler == null) {
            return;
        }
        mHandler.removeCallbacks(mUpdateCounters);
    }

    private final Runnable mUpdateCounters = new Runnable() {
        @Override
        public void run() {
            if (mHandler == null || audioPlayer == null)
                return;
            long pos = audioPlayer.getCurrentPosition();
            final long dur = audioPlayer.getDuration();
            if (pos > dur) pos = dur;

            int position = (int) pos;
            int duration = (int) dur;

            EventBus.getDefault().post(new UpdateCounterEvent(AppUtils.getDurationString(dur - pos, true), position, duration));

            if (mHandler != null)
                mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (isReplay) {
            mediaPlayer.start();
        } else {
            mHandler.removeCallbacks(mUpdateCounters);
        }

        EventBus.getDefault().post(new CompleteEvent(""));
    }
}
