package bg.player.com.playerbackground.module;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bg.player.com.playerbackground.R;
import bg.player.com.playerbackground.Util;
import bg.player.com.playerbackground.event.CompleteEvent;
import bg.player.com.playerbackground.event.MessageEvent;
import bg.player.com.playerbackground.event.PreparAudioEvent;
import bg.player.com.playerbackground.event.UpdateCounterEvent;
import bg.player.com.playerbackground.service.MediaBindService;
import bg.player.com.playerbackground.service.MediaService;

public class AudioPlayerBG extends RelativeLayout implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    LinearLayout llController;
    LinearLayout llController1;
    TextView position;
    SeekBar seeker;
    SeekBar seeker1;
    TextView duration;
    TextView duration1;
    ImageButton btnPlay;
    ImageButton btnPlay1;
    ImageButton btnForward;
    ImageButton btnbackward;
    ImageButton btnReplay;
    ImageButton btnSub;
    RelativeLayout rlRoot;
    Context context;
    ImageView imgContent;
    private boolean autoPlay;
    private AudioPlayerInteface audioPlayerInteface;
    private boolean streamAudio;
    private String audioFileUrl = "";
    private Uri audioFileUri;
    private LinearLayout llImageContent;
    int type;
    private static final int HIDE_CONTROLLER_TIME = 5000;
    private Handler mHandler = new Handler();
    private boolean isShowController = true;
    private boolean isReplay;
    private boolean isPlayerTraining = false;
    private static final int UI_FULL_MODE = 0;
    private static final int UI_LITE_MODE = 1;

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void setStreamAudio(boolean streamAudio) {
        this.streamAudio = streamAudio;
    }

    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
        if (mediaBindService != null) {
            if (!audioFileUrl.isEmpty()) {
                mediaBindService.setAudioFileUrl(audioFileUrl);
            } else {
                mediaBindService.setAudioFileUri(audioFileUri);
            }
            mediaBindService.initAudioPlayer();
        }
    }

    public void setImgContent(String img) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.ic_img_book_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(context).applyDefaultRequestOptions(options).load(img).into(imgContent);
    }

    public void setAudioFileUri(Uri audioFileUri) {
        this.audioFileUri = audioFileUri;
        if (mediaBindService != null) {
            if (!audioFileUrl.isEmpty()) {
                mediaBindService.setAudioFileUrl(audioFileUrl);
            } else {
                mediaBindService.setAudioFileUri(audioFileUri);
            }
            mediaBindService.initAudioPlayer();
        }
    }

    public void setAudioPlayerInteface(AudioPlayerInteface audioPlayerInteface) {
        this.audioPlayerInteface = audioPlayerInteface;
    }

    private MediaBindService mediaBindService = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                mediaBindService = (MediaBindService) iBinder;
                mediaBindService.setAutoPlay(autoPlay);
                mediaBindService.setStreamAudio(streamAudio);
                mediaBindService.setContext(context);
                if (!audioFileUrl.isEmpty()) {
                    mediaBindService.setAudioFileUrl(audioFileUrl);
                    mediaBindService.initAudioPlayer();
                } else if (audioFileUri != null) {
                    mediaBindService.setAudioFileUri(audioFileUri);
                    mediaBindService.initAudioPlayer();
                    seeker1.setEnabled(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (!isPlayerTraining) {
            isPlayerTraining = true;
            if (event.getMessage().equals("onIncomingCallReceived")) {
                if (audioPlayerInteface != null)
                    audioPlayerInteface.onEventTraining();
            }
        } else {
            isPlayerTraining = false;
            switch (event.getMessage()) {
                case "onIncomingCallEnded":
                case "onOutgoingCallEnded":
                case "onMissedCall":
//                mediaBindService.start();
                    break;
                case "onIncomingCallReceived":
                    mediaBindService.stop();
                    break;
                default:
                    mediaBindService.pause();
                    if (isFullMode()) btnPlay.setImageResource(R.drawable.ic_play);
                    else btnPlay1.setImageResource(R.drawable.ic_play);
                    break;
            }
        }
    }

    public void resetUI() {
        seeker1.setProgress(0);
        duration1.setText("0:00");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgressAudioEvent(PreparAudioEvent event) {
        Log.e("PreparAudioEvent", "PreparAudioEvent");
        if (isFullMode()) {
            this.seeker.setProgress(0);
            this.seeker.setMax(event.getDuration());
            duration.setText(Util.getDurationString(event.getDuration(), false));
            btnPlay.setImageResource(autoPlay ? R.drawable.ic_pause : R.drawable.ic_play);
        } else {
            this.seeker1.setProgress(0);
            this.seeker1.setMax(event.getDuration());
            duration1.setText(Util.getDurationString(event.getDuration(), false));
            btnPlay1.setImageResource(autoPlay ? R.drawable.ic_pause : R.drawable.ic_play);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateCounterEvent(UpdateCounterEvent event) {
        if (isFullMode()) {
            duration.setText(event.getPos());
            seeker.setProgress(event.getPosition());
            seeker.setMax(event.getDuration());
        } else {
            duration1.setText(event.getPos());
            seeker1.setProgress(event.getPosition());
            seeker1.setMax(event.getDuration());
        }
        if (audioPlayerInteface != null)
            audioPlayerInteface.onDuration(event.getTime());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteEvent(CompleteEvent event) {
        seeker1.setEnabled(false);
        if (audioPlayerInteface != null)
            audioPlayerInteface.onComplete();

        if (isFullMode()) showControll();
        if (isReplay) {
            if (isFullMode()) btnPlay.setImageResource(R.drawable.ic_pause);
            else btnPlay1.setImageResource(R.drawable.ic_pause);
        } else {
            if (isFullMode()) btnPlay.setImageResource(R.drawable.ic_play);
            else btnPlay1.setImageResource(R.drawable.ic_play);
        }
    }

    public boolean isPlaying() {
        return mediaBindService.isPlaying();
    }

    private void bindService() {
        if (mediaBindService == null) {
            Intent i = new Intent(context, MediaService.class);
            context.bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unBindService() {
        if (mediaBindService != null) {
            context.unbindService(serviceConnection);
        }
    }

    public AudioPlayerBG(Context context) {
        super(context);
        this.init(context, null);
    }

    public AudioPlayerBG(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public AudioPlayerBG(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    public AudioPlayerBG(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.audio_player_bg, this);

        llController = view.findViewById(R.id.llController);
        llController1 = view.findViewById(R.id.llController1);
        position = view.findViewById(R.id.position);
        seeker = view.findViewById(R.id.seeker);
        seeker1 = view.findViewById(R.id.seeker1);
        duration = view.findViewById(R.id.duration);
        duration1 = view.findViewById(R.id.duration1);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnPlay1 = view.findViewById(R.id.btnPlay1);
        btnForward = view.findViewById(R.id.btnForward);
        btnbackward = view.findViewById(R.id.btnbackward);
        btnReplay = view.findViewById(R.id.btnReplay);
        btnSub = view.findViewById(R.id.btnSub);
        rlRoot = view.findViewById(R.id.rlRoot);
        imgContent = view.findViewById(R.id.imgContent);
        llImageContent = view.findViewById(R.id.llImageContent);


        btnReplay.setOnClickListener(this);
        btnbackward.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPlay1.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        rlRoot.setOnClickListener(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AudioPlayerBG);
        autoPlay = typedArray.getBoolean(R.styleable.AudioPlayerBG_apbg_autoplay, true);
        type = typedArray.getInt(R.styleable.AudioPlayerBG_apbg_UItype, UI_FULL_MODE);
        typedArray.recycle();
        EventBus.getDefault().register(this);
        seeker.setOnSeekBarChangeListener(this);
        seeker1.setOnSeekBarChangeListener(this);
        seeker1.setEnabled(false);
        bindService();
        mHandler.postDelayed(hideControlsRunnable, 1500);

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int result = am.requestAudioFocus(focusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

        }

        if (!isFullMode()) {
            rlRoot.setBackground(null);
            rlRoot.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llImageContent.setVisibility(GONE);
            llController.setVisibility(GONE);
            llController1.setVisibility(VISIBLE);
        }
    }

    private void showControll() {
        if (!isShowController) {
            isShowController = true;
            llController.animate().cancel();
            llController.setAlpha(0f);
            llController.setVisibility(View.VISIBLE);
            llController.animate().alpha(1f).translationY(0).setListener(null)
                    .setInterpolator(new DecelerateInterpolator()).start();
        }

        mHandler.removeCallbacks(hideControlsRunnable);
        mHandler.postDelayed(hideControlsRunnable, HIDE_CONTROLLER_TIME);
    }

    private void hideControll() {
        if (isShowController) {
            isShowController = false;
            llController.animate().cancel();
            llController.setAlpha(1f);
            llController.setTranslationY(0f);
            llController.setVisibility(View.VISIBLE);
            llController.animate()
                    .alpha(0f)
                    .translationY(llController.getHeight())
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (llController != null)
                                llController.setVisibility(View.GONE);
                        }
                    }).start();
        }
    }

    public void setEnableBtn(boolean enableBtn) {
        btnPlay1.setEnabled(enableBtn);
    }

    public void resetPlayButton() {
        if (mediaBindService.isPlaying()) {
            if (isFullMode()) btnPlay.setImageResource(R.drawable.ic_play);
            else btnPlay1.setImageResource(R.drawable.ic_play);
            mediaBindService.pause();
            mediaBindService.seekTo(0);
            seeker1.setEnabled(false);
        } else {
            if (isFullMode()) btnPlay.setImageResource(R.drawable.ic_pause);
            else btnPlay1.setImageResource(R.drawable.ic_pause);
            mediaBindService.start();
            seeker1.setEnabled(true);
        }
    }

    public void renderPlayBtn() {
        if (mediaBindService.isPlaying()) {
            if (isFullMode()) btnPlay.setImageResource(R.drawable.ic_play);
            else btnPlay1.setImageResource(R.drawable.ic_play);
            mediaBindService.pause();
            seeker1.setEnabled(false);
        } else {
            if (isFullMode()) btnPlay.setImageResource(R.drawable.ic_pause);
            else btnPlay1.setImageResource(R.drawable.ic_pause);
            mediaBindService.start();
            seeker1.setEnabled(true);
        }
    }

    public void startAudio() {
        mediaBindService.start();
    }

    public void pauseAudio() {
        mediaBindService.pause();
    }

    private Runnable hideControlsRunnable = new Runnable() {
        @Override
        public void run() {
            if (isFullMode())
                hideControll();
        }
    };

    private void renderReplayBtn() {
        isReplay = !isReplay;
        if (isReplay) {
            btnReplay.setImageResource(R.drawable.ic_replay_active);
        } else {
            btnReplay.setImageResource(R.drawable.ic_replay);
        }
        mediaBindService.setReplay(isReplay);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e("onDetachedFromWindow", "onDetachedFromWindow");
        if (mediaBindService != null)
            mediaBindService.stop();
        unBindService();
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mediaBindService.seekTo(progress);
            if (isFullMode()) btnPlay.setImageResource(R.drawable.ic_pause);
            else btnPlay1.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaBindService.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaBindService.start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnReplay) {
            renderReplayBtn();
        } else if (view.getId() == R.id.btnbackward) {
            if (audioPlayerInteface != null)
                audioPlayerInteface.onPrevious();
        } else if (view.getId() == R.id.btnPlay || view.getId() == R.id.btnPlay1) {
            renderPlayBtn();
        } else if (view.getId() == R.id.btnForward) {
            if (audioPlayerInteface != null)
                audioPlayerInteface.onNext();
        } else if (view.getId() == R.id.btnSub) {

        } else if (view.getId() == R.id.rlRoot) {
            if (isFullMode()) showControll();
        }
    }

    private boolean isFullMode() {
        return type == UI_FULL_MODE;
    }

    public void stopPlayer() {
        mediaBindService.stop();
    }

    public void resetReplay() {
        btnReplay.setImageResource(R.drawable.ic_replay);
        mediaBindService.setReplay(false);
    }

    private AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    switch (focusChange) {

                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                            if (mediaBindService == null) return;
                            mediaBindService.pause();
                            btnPlay.setImageResource(R.drawable.ic_play);
                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS):
                            if (mediaBindService == null) return;
                            mediaBindService.pause();
                            btnPlay.setImageResource(R.drawable.ic_play);
                            break;
                        case (AudioManager.AUDIOFOCUS_GAIN):
                            break;
                        default:
                            break;
                    }
                }
            };
}
