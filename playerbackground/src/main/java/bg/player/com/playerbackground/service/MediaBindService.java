package bg.player.com.playerbackground.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bg.player.com.playerbackground.Util;
import bg.player.com.playerbackground.event.CompleteEvent;
import bg.player.com.playerbackground.event.PreparAudioEvent;
import bg.player.com.playerbackground.event.UpdateCounterEvent;

public class MediaBindService extends Binder implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private MediaPlayer audioPlayer = null;
    private Context context = null;
    private Uri audioFileUri = null;
    private String audioFileUrl = "";
    private boolean streamAudio;
    private boolean autoPlay;
    private Handler mHandler = new Handler();
    private boolean isReplay;

    Map<String, String> headers = new HashMap<>();

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
        headers.put("Device", "android");

        try {
            if (audioPlayer != null) {
                audioPlayer.release();
                audioPlayer = null;
            }
            audioPlayer = new MediaPlayer();
            audioPlayer.setOnPreparedListener(this);
            audioPlayer.setOnErrorListener(this);
            audioPlayer.setOnCompletionListener(this);
            audioPlayer.reset();
            if (!TextUtils.isEmpty(getAudioFileUrl())) {
                if (streamAudio) {
                    audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                audioPlayer.setDataSource(getContext(), Uri.parse(getAudioFileUrl()), headers);
            } else {
                if (getAudioFileUri() != null)
                    audioPlayer.setDataSource(getContext(), getAudioFileUri());
            }
            audioPlayer.prepareAsync();

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
        try {
            if (audioPlayer != null) {
                audioPlayer.stop();
                audioPlayer.release();
                audioPlayer = null;
            }
            if (mHandler == null) {
                return;
            }
            mHandler.removeCallbacks(mUpdateCounters);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            EventBus.getDefault().post(new UpdateCounterEvent(Util.getDurationString(dur - pos, true), position, duration, pos));

            if (mHandler != null)
                mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (isReplay) {
            mediaPlayer.start();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(mUpdateCounters);
                }
            }, 500);
        }
        EventBus.getDefault().post(new CompleteEvent(""));
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }
}
