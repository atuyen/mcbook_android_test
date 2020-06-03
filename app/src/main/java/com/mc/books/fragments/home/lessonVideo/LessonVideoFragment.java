package com.mc.books.fragments.home.lessonVideo;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bon.util.ToastUtils;
import com.halilibo.betteraudioplayer.BetterVideoCallback;
import com.halilibo.betteraudioplayer.BetterVideoPlayer;
import com.halilibo.betteraudioplayer.subtitle.CaptionsView;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.gift.Gift;
import com.mc.models.home.Lesson;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.File;

import bg.player.com.playerbackground.module.AudioPlayerBG;
import bg.player.com.playerbackground.module.AudioPlayerInteface;
import butterknife.BindView;

import static com.mc.utilities.Constant.BOOK_ID;
import static com.mc.utilities.Constant.KEY_GIFT;
import static com.mc.utilities.Constant.KEY_LESSON;

public class LessonVideoFragment extends BaseMvpFragment<ILessonVideoView, ILessonVideoPresenter<ILessonVideoView>> implements ILessonVideoView {
    public static LessonVideoFragment newInstance(Lesson lesson, Gift gift, int bookId) {
        Bundle args = new Bundle();
        LessonVideoFragment fragment = new LessonVideoFragment();
        args.putSerializable(KEY_LESSON, lesson);
        args.putSerializable(KEY_GIFT, gift);
        args.putInt(BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.audioPlayerBG)
    AudioPlayerBG audioPlayerBG;
    @BindView(R.id.videoPlayer)
    BetterVideoPlayer betterVideoPlayer;
    @BindView(R.id.llroot)
    FrameLayout llroot;
    private Lesson lesson;
    private Gift gift;
    private String fileName = "";
    private String fileUrl = "";
    private static final int KEY_TYPE_LESSON = 1;
    private static final int KEY_TYPE_GIFT = 2;
    private int bookId;
    private String folderDownload;
    private int folderId;
    private int fileId;
    private String name;
    MenuItem miDownloadDisable, miDownload;

    @NonNull
    @Override
    public ILessonVideoPresenter<ILessonVideoView> createPresenter() {
        return new LessonVideoPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        setHasOptionsMenu(true);
        bookId = getArguments().getInt(BOOK_ID);
        lesson = (Lesson) getArguments().getSerializable(KEY_LESSON);
        gift = (Gift) getArguments().getSerializable(KEY_GIFT);
        folderDownload = lesson != null ? Constant.FOLDER_BOOK : Constant.FOLDER_GIFT;
        folderId = bookId != -1 ? bookId : gift.getId();
        initData();
    }

    private void checkFileExist(String fileName, String typeFile, int type) {
        File file = new File(AppUtils.getFilePath(getContext(), folderDownload, folderId, fileName));
        if (typeFile.equals(Constant.KEY_AUDIO)) {
            initAudio(type, file.exists() ? file : null);
        } else {
            initVideo(type, file.exists() ? file : null);
        }
    }

    private void initData() {
        try {
            String typeFile = "";
            if (lesson != null) {
                typeFile = lesson.getType();
                fileUrl = lesson.getMedia();
                fileId = lesson.getId();
                name = lesson.getName();
            } else if (gift != null) {
                typeFile = gift.getType();
                fileUrl = gift.getContentUri();
                fileId = gift.getId();
                name = gift.getName();
            }


            if (lesson != null) {
                if (lesson.getMedia() == null) return;
                fileName = AppUtils.getFileName(lesson.getMedia());
            } else if (gift != null) {
                if (gift.getContentUri() == null) return;
                fileName = AppUtils.getFileName(gift.getContentUri());
            }

            switch (typeFile) {
                case Constant.KEY_AUDIO:
                    if (lesson != null)
                        checkFileExist(fileName, Constant.KEY_AUDIO, KEY_TYPE_LESSON);
                    else if (gift != null)
                        checkFileExist(fileName, Constant.KEY_AUDIO, KEY_TYPE_GIFT);
                    break;
                case Constant.KEY_VIDEO:
                    if (lesson != null)
                        checkFileExist(fileName, Constant.KEY_VIDEO, KEY_TYPE_LESSON);
                    else if (gift != null)
                        checkFileExist(fileName, Constant.KEY_VIDEO, KEY_TYPE_GIFT);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.onShowBottomBar(false);
    }

    private void initAudio(int type, File file) {
        betterVideoPlayer.setVisibility(View.GONE);
        audioPlayerBG.setVisibility(View.VISIBLE);
        audioPlayerBG.setAudioPlayerInteface(new AudioPlayerInteface() {
            @Override
            public void onComplete() {
                if (lesson != null)
                    presenter.sendLogLesson(lesson.getId(), bookId);
            }

            @Override
            public void onNext() {
            }

            @Override
            public void onPrevious() {
            }

            @Override
            public void onDuration(long dur) {

            }

            @Override
            public void onEventTraining() {

            }

        });
        audioPlayerBG.setAutoPlay(true);
        audioPlayerBG.setStreamAudio(true);
        String imgThumb;
        if (lesson != null)
            imgThumb = lesson.getThumbnail();
        else
            imgThumb = gift.getCoverUri();

        audioPlayerBG.setImgContent(imgThumb);
        if (type == KEY_TYPE_LESSON) {
            if (file == null)
                audioPlayerBG.setAudioFileUrl(lesson.getMedia());
            else
                audioPlayerBG.setAudioFileUri(Uri.fromFile(file));
        } else {
            if (file == null)
                audioPlayerBG.setAudioFileUrl(gift.getContentUri());
            else
                audioPlayerBG.setAudioFileUri(Uri.fromFile(file));
        }
    }

    private void initVideo(int type, File file) {
        audioPlayerBG.setVisibility(View.GONE);
        betterVideoPlayer.setVisibility(View.VISIBLE);
        betterVideoPlayer.setAutoPlay(true);
        betterVideoPlayer.setBackground();
        if (lesson != null) {
            if (lesson.getSubtitle() != null) {
                File subFile = new File(AppUtils.getFilePath(getContext(), folderDownload, folderId, lesson.getSubtitle()));
                if (!subFile.exists())
                    presenter.onDownloadSub(lesson.getSubFile(), AppUtils.getFileName(lesson.getSubtitle()), getContext(), folderDownload, folderId, fileId, "");
                betterVideoPlayer.setCaptions(subFile.exists() ? Uri.fromFile(subFile) : Uri.parse(lesson.getSubtitle()), CaptionsView.CMime.SUBRIP);
            }
        } else if (gift != null) {
            if (gift.getSubsUri() != null) {
                File subFile = new File(AppUtils.getFilePath(getContext(), folderDownload, folderId, lesson.getSubtitle()));
                if (!subFile.exists())
                    presenter.onDownloadSub(gift.getSubsUri(), AppUtils.getFileName(gift.getSubsUri()), getContext(), folderDownload, folderId, fileId, "");
                betterVideoPlayer.setCaptions(subFile.exists() ? Uri.fromFile(subFile) : Uri.parse(gift.getSubsUri()), CaptionsView.CMime.SUBRIP);
            }
        }
        betterVideoPlayer.setBottomProgressBarVisibility(false);
        betterVideoPlayer.setCallback(new BetterVideoCallback() {
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
                if (lesson != null)
                    presenter.sendLogLesson(lesson.getId(), bookId);
            }

            @Override
            public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {

            }

            @Override
            public void onFullScreen(boolean isFullScreen) {
                try {
                    if (!isFullScreen) {
                        mActivity.getSupportActionBar().hide();
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        betterVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    } else {
                        resetFullScreenView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext() {

            }

            @Override
            public void onPrevious() {

            }
        });

        if (type == KEY_TYPE_LESSON) {
            betterVideoPlayer.setSource(file == null ? Uri.parse(lesson.getMedia()) : Uri.fromFile(file), AppContext.getHeader());
        } else {
            betterVideoPlayer.setSource(file == null ? Uri.parse(gift.getContentUri()) : Uri.fromFile(file), AppContext.getHeader());
        }
    }

    private void resetFullScreenView() {
        betterVideoPlayer.setFullScreenOff();
        betterVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        mActivity.getSupportActionBar().show();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.download, menu);
        miDownloadDisable = menu.findItem(R.id.action_download_disable);
        miDownload = menu.findItem(R.id.action_download);
        miDownload.setVisible(false);
        miDownloadDisable.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                presenter.onDownloadFile(fileUrl, fileName, getAppContext(), folderDownload, folderId, fileId, name);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public int getResourceId() {
        return R.layout.lesson_video_fragment;
    }

    @Override
    public String getTitleString() {
        if (lesson != null)
            return lesson.getName();
        else if (gift != null)
            return gift.getName();
        return "";
    }

    @Override
    public void onDestroyView() {
        mMainActivity.onShowBottomBar(true);
        resetFullScreenView();
        if (betterVideoPlayer != null)
            betterVideoPlayer.release();
        super.onDestroyView();
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onDownloadSuccess() {
        ToastUtils.showToast(getAppContext(), getString(R.string.download_sucess));
    }

    @Override
    public void onDownloadFail(String error) {
        ToastUtils.showToast(getAppContext(), error);
    }

    @Override
    public void onDownloadProgressing(int progress) {
    }
}
