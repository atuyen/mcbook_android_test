package com.mc.books.fragments.home.lesson;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bon.util.ToastUtils;
import com.github.barteksc.pdfviewer.PDFView;
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
import java.io.Serializable;
import java.util.List;

import bg.player.com.playerbackground.module.AudioPlayerBG;
import bg.player.com.playerbackground.module.AudioPlayerInteface;
import butterknife.BindView;

import static com.mc.utilities.Constant.BOOK_ID;
import static com.mc.utilities.Constant.GIFT_ID;
import static com.mc.utilities.Constant.KEY_INDEX;
import static com.mc.utilities.Constant.KEY_LIST_GIFT;
import static com.mc.utilities.Constant.KEY_LIST_LESSON;

public class LessonFragment extends BaseMvpFragment<ILessonView, ILessonPresenter<ILessonView>> implements ILessonView {

    public static LessonFragment newInstance(List<Lesson> lessons, List<Gift> gifts, int index, int bookId, int giftId) {
        Bundle args = new Bundle();
        LessonFragment fragment = new LessonFragment();
        args.putSerializable(KEY_LIST_LESSON, (Serializable) lessons);
        args.putSerializable(KEY_LIST_GIFT, (Serializable) gifts);
        args.putSerializable(KEY_INDEX, index);
        args.putSerializable(BOOK_ID, bookId);
        args.putSerializable(GIFT_ID, giftId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.audioPlayerBG)
    AudioPlayerBG audioPlayerBG;
    @BindView(R.id.videoPlayer)
    BetterVideoPlayer betterVideoPlayer;
    @BindView(R.id.llroot)
    FrameLayout llroot;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.llRootPdf)
    LinearLayout llRootPdf;

    private List<Lesson> lessons;
    private List<Gift> gifts;
    private String fileName = "";
    private String fileUrl = "";
    private static final int KEY_TYPE_LESSON = 1;
    private static final int KEY_TYPE_GIFT = 2;
    private static final int DOWNLOAD_MEDIA = 0;
    private static final int DOWNLOAD_PDF = 1;
    private int bookId = -1;
    private int giftId = -1;
    private int index;
    private Menu menu;
    private MenuInflater menuInflater;
    private int maxIndex;
    private String folderDownload;
    private int folderId;
    private int fileId;
    private String name;
    MenuItem miDownload, miDownloadDisable, miDownloadFake;
    File file;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        try {
            setHasOptionsMenu(true);
            lessons = (List<Lesson>) getArguments().getSerializable(KEY_LIST_LESSON);
            gifts = (List<Gift>) getArguments().getSerializable(KEY_LIST_GIFT);
            index = getArguments().getInt(KEY_INDEX);
            bookId = getArguments().getInt(BOOK_ID);
            giftId = getArguments().getInt(GIFT_ID);
            llRootPdf.setVisibility(View.GONE);
            llroot.setVisibility(View.GONE);
            folderDownload = lessons != null ? Constant.FOLDER_BOOK : Constant.FOLDER_GIFT;
            folderId = bookId != -1 ? bookId : giftId;
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.onShowBottomBar(false);
    }

    @Override
    public int getResourceId() {
        return R.layout.lesson;
    }

    private void initData() {
        try {
            String typeFile = "";
            if (lessons != null) {
                maxIndex = lessons.size();
                typeFile = lessons.get(index).getType();
                fileUrl = lessons.get(index).getMedia();
                fileId = lessons.get(index).getId();
                name = lessons.get(index).getName();
            } else if (gifts != null) {
                maxIndex = gifts.size();
                typeFile = gifts.get(index).getType();
                fileUrl = gifts.get(index).getContentUri();
                fileId = gifts.get(index).getId();
                name = gifts.get(index).getName();
            }

            if (lessons != null) {
                if (lessons.get(index).getMedia() == null) return;
                fileName = AppUtils.getFileName(lessons.get(index).getMedia());
            } else if (gifts != null) {
                if (gifts.get(index).getContentUri() == null) return;
                fileName = AppUtils.getFileName(gifts.get(index).getContentUri());
            }

            switch (typeFile) {
                case Constant.KEY_AUDIO:
                    llroot.setVisibility(View.VISIBLE);
                    llRootPdf.setVisibility(View.GONE);
                    renderMenuMedia();
                    if (lessons != null) {
                        mActivity.setToolbarTitle(lessons.get(index).getName());
                        checkFileExist(fileName, Constant.KEY_AUDIO, KEY_TYPE_LESSON);
                    } else if (gifts != null) {
                        mActivity.setToolbarTitle(gifts.get(index).getName());
                        checkFileExist(fileName, Constant.KEY_AUDIO, KEY_TYPE_GIFT);
                    }
                    break;
                case Constant.KEY_VIDEO:
                    renderMenuMedia();
                    llroot.setVisibility(View.VISIBLE);
                    llRootPdf.setVisibility(View.GONE);
                    if (lessons != null) {
                        mActivity.setToolbarTitle(lessons.get(index).getName());
                        checkFileExist(fileName, Constant.KEY_VIDEO, KEY_TYPE_LESSON);
                    } else if (gifts != null) {
                        mActivity.setToolbarTitle(gifts.get(index).getName());
                        checkFileExist(fileName, Constant.KEY_VIDEO, KEY_TYPE_GIFT);
                    }
                    break;
                case Constant.KEY_PDF:
                    llRootPdf.setVisibility(View.VISIBLE);
                    llroot.setVisibility(View.GONE);
                    renderMenuPdf();
                    if (lessons != null) {
                        mActivity.setToolbarTitle(lessons.get(index).getName());
                        checkFileExist(fileName, Constant.KEY_PDF, KEY_TYPE_LESSON);
                    } else if (gifts != null) {
                        mActivity.setToolbarTitle(gifts.get(index).getName());
                        checkFileExist(fileName, Constant.KEY_PDF, KEY_TYPE_GIFT);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderMenuPdf() {
        if (menu != null) {
            menu.clear();
            menuInflater.inflate(R.menu.pdf, menu);
            showMenuDownload();
        }
    }

    private void renderMenuMedia() {
        if (menu != null) {
            menu.clear();
            menuInflater.inflate(R.menu.download, menu);
            showMenuDownload();
        }
    }

    private void checkFileExist(String fileName, String typeFile, int type) {
        file = new File(AppUtils.getFilePath(getContext(), folderDownload, folderId, fileName));
        switch (typeFile) {
            case Constant.KEY_AUDIO:
                initAudioBG(type, file.exists() ? file : null);
                break;
            case Constant.KEY_VIDEO:
                initVideo(type, file.exists() ? file : null);
                break;
            default:
                //download PDF
                if (file.exists()) {
                    if (isValidNetwork()) {
                        file.delete();
                        presenter.onDownloadFile(fileUrl, fileName, getContext(), DOWNLOAD_PDF, folderDownload, folderId, fileId, name);
                    } else {
                        //load pdf offline
                        pdfView.fromUri(Uri.fromFile(file))
                                .swipeHorizontal(true)
                                .enableDoubletap(true)
                                .defaultPage(0)
                                .enableAnnotationRendering(false)
                                .password(null)
                                .scrollHandle(null)
                                .enableAntialiasing(true)
                                .spacing(0)
                                .load();
                    }
                } else
                    presenter.onDownloadFile(fileUrl, fileName, getContext(), DOWNLOAD_PDF, folderDownload, folderId, fileId, name);
                break;
        }
    }

    private void initAudioBG(int type, File file) {
        audioPlayerBG.setVisibility(View.VISIBLE);
        audioPlayerBG.setAutoPlay(true);
        audioPlayerBG.setStreamAudio(true);
        String imgThumb;
        if (lessons != null)
            imgThumb = lessons.get(index).getThumbnail();
        else
            imgThumb = gifts.get(index).getCoverUri();

        audioPlayerBG.setImgContent(imgThumb);
        if (type == KEY_TYPE_LESSON) {
            if (file == null)
                audioPlayerBG.setAudioFileUrl(lessons.get(index).getMedia());
            else
                audioPlayerBG.setAudioFileUri(Uri.fromFile(file));

        } else {
            if (file == null)
                audioPlayerBG.setAudioFileUrl(gifts.get(index).getContentUri());
            else
                audioPlayerBG.setAudioFileUri(Uri.fromFile(file));

        }

        audioPlayerBG.setAudioPlayerInteface(new AudioPlayerInteface() {
            @Override
            public void onComplete() {
                if (lessons != null)
                    presenter.sendLogLesson(fileId, bookId);
            }

            @Override
            public void onNext() {
                if (lessons != null)
                    onNextFile();
            }

            @Override
            public void onPrevious() {
                if (lessons != null)
                    onPreviousFile();
            }

            @Override
            public void onDuration(long dur) {

            }

            @Override
            public void onEventTraining() {

            }

        });
    }


    private void initVideo(int type, File file) {
        betterVideoPlayer.setVisibility(View.VISIBLE);
        betterVideoPlayer.setAutoPlay(true);
        betterVideoPlayer.setBackground();

        if (lessons != null) {
            if (lessons.get(index).getSubtitle() != null) {
                File subFile = new File(AppUtils.getFilePath(getContext(), folderDownload, folderId, lessons.get(index).getSubtitle()));
                if (!subFile.exists())
                    presenter.onDownloadSub(lessons.get(index).getSubtitle(), lessons.get(index).getSubtitle(), getContext(), folderDownload, folderId, fileId, "");

                betterVideoPlayer.setCaptions(subFile.exists() ? Uri.fromFile(subFile) : Uri.parse(lessons.get(index).getSubtitle()), CaptionsView.CMime.SUBRIP);
            }
        } else if (gifts != null) {
            if (gifts.get(index).getSubsUri() != null) {
                File subFile = new File(AppUtils.getFilePath(getContext(), folderDownload, folderId, gifts.get(index).getSubsUri()));
                if (!subFile.exists())
                    presenter.onDownloadSub(gifts.get(index).getSubsUri(), gifts.get(index).getSubsUri(), getContext(), folderDownload, folderId, fileId, "");

                betterVideoPlayer.setCaptions(subFile.exists() ? Uri.fromFile(subFile) : Uri.parse(gifts.get(index).getSubsUri()), CaptionsView.CMime.SUBRIP);
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
                if (lessons != null)
                    presenter.sendLogLesson(fileId, bookId);
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
                    } else resetFullScreenView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext() {
                resetFullScreenView();
                if (lessons != null)
                    onNextFile();
            }

            @Override
            public void onPrevious() {
                resetFullScreenView();
                if (lessons != null)
                    onPreviousFile();
            }
        });

        if (type == KEY_TYPE_LESSON) {
            betterVideoPlayer.setSource(file == null ? Uri.parse(lessons.get(index).getMedia()) : Uri.fromFile(file), AppContext.getHeader());
        } else {
            betterVideoPlayer.setSource(file == null ? Uri.parse(gifts.get(index).getContentUri()) : Uri.fromFile(file), AppContext.getHeader());
        }
    }

    private void resetFullScreenView() {
        betterVideoPlayer.setFullScreenOff();
        betterVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        mActivity.getSupportActionBar().show();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void onNextFile() {
//        if (!isValidNetwork()) return;
        if (index + 1 >= maxIndex)
            ToastUtils.showToast(getContext(), getString(R.string.last_lesson));
        else {
            betterVideoPlayer.setCallback(null);
            audioPlayerBG.setAudioPlayerInteface(null);
            audioPlayerBG.setVisibility(View.GONE);
            betterVideoPlayer.setVisibility(View.GONE);
            index++;
            resetPlayer();
            initData();
        }
    }

    private void onPreviousFile() {
//        if (!isValidNetwork()) return;
        if (index - 1 < 0) ToastUtils.showToast(getContext(), getString(R.string.first_lesson));
        else {
            betterVideoPlayer.setCallback(null);
            audioPlayerBG.setAudioPlayerInteface(null);
            audioPlayerBG.setVisibility(View.GONE);
            betterVideoPlayer.setVisibility(View.GONE);
            index--;
            resetPlayer();
            initData();
        }
    }

    private void resetPlayer() {
        if (betterVideoPlayer != null) {
            betterVideoPlayer.pause();
            betterVideoPlayer.stop();
            betterVideoPlayer.reset();
            betterVideoPlayer.showLoading();
        }
        if (audioPlayerBG != null) {
            audioPlayerBG.stopPlayer();
            audioPlayerBG.resetReplay();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        this.menuInflater = inflater;
        menu.clear();
        String fileType;
        if (lessons != null)
            fileType = lessons.get(index).getType();
        else
            fileType = gifts.get(index).getType();

        switch (fileType) {
            case Constant.KEY_AUDIO:
            case Constant.KEY_VIDEO:
                if (lessons != null)
                    inflater.inflate(R.menu.download, menu);
                break;
            case Constant.KEY_PDF:
                if (lessons != null)
                    inflater.inflate(R.menu.pdf, menu);
                break;
        }
        showMenuDownload();
    }

    void showMenuDownload() {
        miDownload = menu.findItem(R.id.action_download);
        miDownloadDisable = menu.findItem(R.id.action_download_disable);
        miDownloadFake = menu.findItem(R.id.action_download_fake);
//        if (isValidNetwork()) {
//            if (miDownload != null)
//                miDownload.setVisible(true);
//            if (miDownloadFake != null)
//                miDownloadFake.setVisible(true);
//            if (miDownloadDisable != null)
//                miDownloadDisable.setVisible(false);
//        } else {
//            if (miDownload != null)
//                miDownload.setVisible(false);
//            if (miDownloadFake != null)
//                miDownloadFake.setVisible(false);
//            if (miDownloadDisable != null)
//                miDownloadDisable.setVisible(true);
//        }

        if (isValidNetwork()) {
            if (miDownload != null)
                miDownload.setVisible(false);
            if (miDownloadFake != null)
                miDownloadFake.setVisible(false);
            if (miDownloadDisable != null)
                miDownloadDisable.setVisible(false);
        } else {
            if (miDownload != null)
                miDownload.setVisible(false);
            if (miDownloadFake != null)
                miDownloadFake.setVisible(false);
            if (miDownloadDisable != null)
                miDownloadDisable.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                if (isValidNetwork())
                    presenter.onDownloadFile(fileUrl, fileName, getAppContext(), DOWNLOAD_MEDIA, folderDownload, folderId, fileId, name);
                else showNetworkRequire();
                break;
            case R.id.action_previous:
                onPreviousFile();
                break;
            case R.id.action_next:
                onNextFile();
                break;
            case R.id.action_download_fake:
                showProgress(true);
                new Handler().postDelayed(() -> {
                    showProgress(false);
                    ToastUtils.showToast(getAppContext(), getString(R.string.download_success));
                }, 3000);
                break;
            default:
                return false;
        }
        return true;
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
    public void onStop() {
        betterVideoPlayer.pause();
        super.onStop();
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @NonNull
    @Override
    public ILessonPresenter<ILessonView> createPresenter() {
        return new LessonPresenter<>(getAppComponent());
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onDownloadSuccess(int type) {
        if (type == DOWNLOAD_MEDIA)
            ToastUtils.showToast(getAppContext(), getString(R.string.download_success));
        else {
            File file = new File(AppUtils.getFilePath(getContext(), folderDownload, folderId, fileName));
            if (lessons != null)
                presenter.sendLogLesson(lessons.get(index).getId(), bookId);
            pdfView.fromUri(Uri.fromFile(file))
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true)
                    .spacing(0)
                    .load();
        }
    }

    @Override
    public void onDownloadFail(String error) {
        ToastUtils.showToast(getAppContext(), getString(R.string.error_dowload));
    }

    @Override
    public void onDownloadProgressing(int progress) {

    }

    @Override
    public String getTitleString() {
        if (lessons != null)
            return lessons.get(index).getName();
        else if (gifts != null)
            return gifts.get(index).getName();
        return "";
    }
}
