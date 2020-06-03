package com.mc.books.fragments.home.doTraining;

import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.jackson.JacksonUtils;
import com.bon.sharepreferences.AppPreferences;
import com.bon.util.DateTimeUtils;
import com.bon.util.ToastUtils;
import com.mc.adapter.SubTitlePracticeAdapter;
import com.mc.adapter.SubtitlesAdapter;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.books.dialog.ConfirmRecordDialog;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.books.fragments.home.doTraining.subtraining.ListRecordFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.customTab.CustomTab;
import com.mc.customizes.edittexts.TextInputApp;
import com.mc.customizes.subtitles.SubtitlesView;
import com.mc.events.BackgroundEvent;
import com.mc.events.DotrainingEvent;
import com.mc.models.home.DetailTraining;
import com.mc.models.home.ItemTrainingResponse;
import com.mc.models.home.RecordItem;
import com.mc.models.home.SubTitles;
import com.mc.models.home.TrainingAudio;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;
import com.mc.utilities.Mp4ParserWrapper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bg.player.com.playerbackground.module.AudioPlayerBG;
import bg.player.com.playerbackground.module.AudioPlayerInteface;
import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.StreamSupport;

public class DoTrainingFragment extends BaseMvpFragment<IDoTrainingView, IDoTrainingPresenter<IDoTrainingView>> implements IDoTrainingView {
    private static final String TAG = "DoTrainingFragment";

    public static DoTrainingFragment newInstance(ItemTrainingResponse itemTrainingResponse, int bookId, int chapterId) {
        Bundle args = new Bundle();
        args.putSerializable(TAG, itemTrainingResponse);
        args.putInt(Constant.BOOK_ID, bookId);
        args.putInt(Constant.TRAINING_CHAPTER_ID, chapterId);
        DoTrainingFragment fragment = new DoTrainingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final int TAB_LISTEN = 0;
    private static final int TAB_PRACTICE = 1;
    private static final int TAB_RECORD = 2;
    private int defaulttab = TAB_LISTEN;

    @BindView(R.id.tabListen)
    CustomTab tabListen;
    @BindView(R.id.tabPractice)
    CustomTab tabPractice;
    @BindView(R.id.tabRecord)
    CustomTab tabRecord;
    @BindView(R.id.llRole)
    LinearLayout llRole;
    @BindView(R.id.etRole)
    TextInputApp etRole;
    @BindView(R.id.audioBG)
    AudioPlayerBG audioBG;
    @BindView(R.id.llTabRecord)
    LinearLayout llTabRecord;
    @BindView(R.id.llrecord)
    LinearLayout llrecord;
    @BindView(R.id.imgRecord)
    ImageView imgRecord;
    @BindView(R.id.llListRecord)
    LinearLayout llListRecord;
    @BindView(R.id.llRoot)
    LinearLayout llRoot;
    @BindView(R.id.recySubTitle)
    RecyclerView recySubTitle;
    @BindView(R.id.recySubTitlePractice)
    RecyclerView recySubTitlePractice;
    @BindView(R.id.emptyTraining)
    ExtTextView emptyTraining;
    SubtitlesAdapter subTitlesAdapter;
    SubTitlePracticeAdapter subTitlePracticeAdapter;
    private SubtitlesView srt;
    private ArrayList<SubTitles> subTitlesList = new ArrayList<>();
    private boolean isDefaultRole = false;
    private ExtKeyValuePairDialogFragment extKeyValuePairDialogFragment;
    private MediaRecorder mRecorder = null;
    private List<ExtKeyValuePair> roles = new ArrayList<>();
    private DetailTraining detailTraining;
    private ItemTrainingResponse itemTrainingResponse;
    String mFileName = "";
    String role;
    private boolean isCheckRecord = false;
    private int bookId, chapterId;
    private boolean isStartRecord = false;
    private ConfirmRecordDialog confirmRecordDialog;
    private File mFile, mFileTemp;
    private String filePath, filePathTemp;
    private static final int SAVE_BUTTON = 1;
    private static final int CONTINUE_BUTTON = 2;
    private static final int CANCEL_BUTTON = 3;
    private boolean isComplete = false;
    private int positionScroll = -1;

    @Override
    public IDoTrainingPresenter<IDoTrainingView> createPresenter() {
        return new DoTrainingPresenter<>(getAppComponent());
    }

    @Override
    public String getTitleString() {
        return itemTrainingResponse.getName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemTrainingResponse = (ItemTrainingResponse) getArguments().getSerializable(TAG);
    }

    @Override
    public int getResourceId() {
        return R.layout.do_training_fragment;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        presenter.getDetailTraining(itemTrainingResponse.getId());
        subTitlesAdapter = new SubtitlesAdapter(getAppContext());
        recySubTitle.setLayoutManager(new LinearLayoutManager(getAppContext()));
        recySubTitle.setAdapter(subTitlesAdapter);
        subTitlePracticeAdapter = new SubTitlePracticeAdapter(getAppContext());
        recySubTitlePractice.setLayoutManager(new LinearLayoutManager(getAppContext()));
        recySubTitlePractice.setAdapter(subTitlePracticeAdapter);
        srt = new SubtitlesView(getAppContext());
        bookId = (getArguments().getInt(Constant.BOOK_ID));
        chapterId = itemTrainingResponse.getId();
        processBackRecoder();
        isStartRecord = false;
        isComplete = false;
        AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_RECORDER, false);
        AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_RESET_AUDIO, false);
        registerEventBackgroundApp();
        if (!AppPreferences.getInstance(getAppContext()).getBoolean(Constant.KEY_BACK_TRAINING)) {
            isCheckRecord = false;
            changeTab(TAB_LISTEN);
        } else {
            isCheckRecord = true;
            AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_RESET_AUDIO, true);
            changeTab(TAB_RECORD);
        }

    }

    private void eventShowDialog() {
        if (audioBG != null && audioBG.isPlaying()) audioBG.pauseAudio();
        if (mRecorder != null) stopRecording();
    }

    //back when recoder show dialog
    private void processBackRecoder() {
        bus.subscribe(this, DotrainingEvent.class, dotrainingEvent -> {
            if (dotrainingEvent.isShowDialog()) {
                if (isStartRecord) eventShowDialog();
            }
        });
    }

    // show dialog when app run background
    public void registerEventBackgroundApp() {
        AppContext.setAppLycicleTraining(bus);
        bus.subscribe(this, BackgroundEvent.class, backgroundEvent -> {
            if (backgroundEvent.getMessage().equals(Constant.KEY_BACKGROUND_TRAINING)) {
                if (isStartRecord) eventShowDialog();
            }
        });
    }

    private void initView() {
        isDefaultRole = false;
        srt.getSubtitleUri(Uri.parse(itemTrainingResponse.getSubtitle()),
                detailTraining.getTrainingAudios(), roles);
        srt.setSubTitlesCallback(subTitles -> {
            subTitlesList.clear();
            subTitlesList.addAll(subTitles);
            subTitlesAdapter.setDataList(subTitlesList);
            subTitlePracticeAdapter.setDataList(subTitlesList);
            checkRole("");
        });
    }

    private void resetFileAudioLesson() {
        if (audioBG != null) {
            audioBG.stopPlayer();
            audioBG.setAudioFileUrl(detailTraining.getAudioLesson());
        }
        recySubTitle.smoothScrollToPosition(0);
        subTitlesAdapter.setSelectedItem(-1);
    }

    private void resetAudio() {
        if (audioBG != null) {
            audioBG.stopPlayer();
            audioBG.setAudioFileUrl(detailTraining.getTrainingAudios().get(0).getAudioFile());
        }
    }

    private void cleanFile() {
        mFileTemp = null;
        mFile = null;
        filePathTemp = null;
        filePath = null;
    }


    @OnClick({R.id.tabListen, R.id.tabPractice, R.id.tabRecord})
    public void onViewClicked(View view) {
        if (isStartRecord) {
            eventShowDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.tabListen:
                AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_RECORDER, false);
                AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_RESET_AUDIO, false);
                cleanFile();
                isCheckRecord = false;
                changeTab(TAB_LISTEN);
                resetFileAudioLesson();
                break;
            case R.id.tabPractice:
                cleanFile();
                AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_RECORDER, false);
                AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_RESET_AUDIO, false);
                isCheckRecord = true;
                changeTab(TAB_PRACTICE);
                resetFileSub();
                checkRole(detailTraining.getTrainingAudios().get(0).getName());
                etRole.setContent(detailTraining.getTrainingAudios().get(0).getName());
                break;
            case R.id.tabRecord:
                filePath = "";
                filePathTemp = "";
                AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_TRAINING, true);
                AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_RECORDER, false);
                AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_RESET_AUDIO, true);
                resetFileSub();
                checkRole(detailTraining.getTrainingAudios().get(0).getName());
                isCheckRecord = true;
                changeTab(TAB_RECORD);
                etRole.setContent(detailTraining.getTrainingAudios().get(0).getName());
                break;
        }
    }

    private void changeTab(int index) {
        tabListen.setActiveMode(false);
        tabPractice.setActiveMode(false);
        tabRecord.setActiveMode(false);
        defaulttab = index;
        llRole.setVisibility(View.VISIBLE);
        audioBG.setVisibility(View.VISIBLE);
        llTabRecord.setVisibility(View.GONE);
        switch (index) {
            case TAB_LISTEN:
                tabListen.setActiveMode(true);
                recySubTitlePractice.setVisibility(View.GONE);
                recySubTitle.setVisibility(View.VISIBLE);
                llRole.setVisibility(View.GONE);
                break;
            case TAB_PRACTICE:
                recySubTitle.setVisibility(View.GONE);
                recySubTitlePractice.setVisibility(View.VISIBLE);
                tabPractice.setActiveMode(true);
                break;
            case TAB_RECORD:
                tabRecord.setActiveMode(true);
                audioBG.setVisibility(View.GONE);
                llTabRecord.setVisibility(View.VISIBLE);
                recySubTitle.setVisibility(View.GONE);
                recySubTitlePractice.setVisibility(View.VISIBLE);
                break;
        }
    }

    private ExtKeyValuePair keyValuePair;

    @OnClick(R.id.etRole)
    void chooseRole() {
        extKeyValuePairDialogFragment = new ExtKeyValuePairDialogFragment();
        extKeyValuePairDialogFragment.setExtKeyValuePairs(roles)
                .setOnSelectedConsumer(keyValuePair -> {
                    isDefaultRole = true;
                    this.keyValuePair = keyValuePair;
                    etRole.setContent(keyValuePair.getValue());
                    role = keyValuePair.getValue();
                    checkRole(keyValuePair.getValue());
                    resetFileRecorder();
                });
        extKeyValuePairDialogFragment.show(getFragmentManager(), null);
    }

    private void checkRole(String role) {
        String roleDefault = etRole.getContent();
        if (srt != null) {
            for (int i = 0; i < subTitlesList.size(); i++) {
                String roleName = subTitlesList.get(i).getItemSub().split(Constant.KEY_DOT)[0];
                SubTitles subTitles = subTitlePracticeAdapter.getItem(i);
                if (subTitles != null) {
                    if (isDefaultRole) subTitles.setSelected(role.equals(roleName));
                    else subTitles.setSelected(roleDefault.equals(roleName));
                }
            }
            subTitlePracticeAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onShowLoading(boolean loading) {
        showProgress(loading);
    }

    @Override
    public void getDetailTrainingSuccess(DetailTraining detailTraining) {
        this.detailTraining = detailTraining;
        if (detailTraining != null && detailTraining.getTrainingAudios().size() > 0) {
            emptyTraining.setVisibility(View.GONE);
            llRoot.setVisibility(View.VISIBLE);
            etRole.setContent(detailTraining.getTrainingAudios().get(0).getName());
        } else {
            llRoot.setVisibility(View.GONE);
            emptyTraining.setVisibility(View.VISIBLE);
        }
        List<ExtKeyValuePair> listRole = new ArrayList<>();
        StreamSupport.stream(detailTraining.getTrainingAudios()).forEach(n -> {
            ExtKeyValuePair extKeyValuePair = new ExtKeyValuePair("", n.getName());
            listRole.add(extKeyValuePair);
        });
        roles.clear();
        roles.addAll(listRole);
        initView();
        initPlayer();
    }

    @Override
    public void getDetailTrainingError(String message) {
        llRoot.setVisibility(View.GONE);
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getResources().getString(R.string.error_message_training));
        errorBoxDialog.show();
    }

    private void initPlayer() {
        audioBG.setAudioPlayerInteface(new AudioPlayerInteface() {
            @Override
            public void onComplete() {
                isComplete = true;
            }

            @Override
            public void onNext() {
            }

            @Override
            public void onPrevious() {
            }

            @Override
            public void onDuration(long dur) {
                startSubTitle(dur, subTitlesList);
                checkRole(role);
            }

            @Override
            public void onEventTraining() {
                if (AppPreferences.getInstance(getAppContext()).getBoolean(Constant.KEY_RESET_AUDIO)) {
                    if (confirmRecordDialog != null && confirmRecordDialog.isShowing())
                        confirmRecordDialog.dismiss();
                    if (mRecorder != null)
                        stopRecording();
                } else {
                    audioBG.resetPlayButton();
                }
            }
        });
        audioBG.setAutoPlay(false);
        audioBG.setStreamAudio(true);
        audioBG.setAudioFileUrl(detailTraining.getAudioLesson());
        Log.e("AudioLesson", detailTraining.getAudioLesson());
    }

    private void startAudioFile(List<TrainingAudio> trainingAudioList, String role) {
        for (int i = 0; i < trainingAudioList.size(); i++) {
            if (trainingAudioList.get(i).getName().contains(role)) {
                Log.e("startAudioFile", trainingAudioList.get(i).getAudioFile());
                audioBG.setAudioFileUrl(trainingAudioList.get(i).getAudioFile());
                break;
            }
        }
    }

    private void startSubTitle(long dur, ArrayList<SubTitles> listSubTitle) {
        if (srt != null) {
            for (int i = 0; i < listSubTitle.size(); i++) {
                SubTitles subTitles = listSubTitle.get(i);
                long timeStart = subTitles.getTimeStart();
                long timeEnd = subTitles.getTimeEnd();
                if (timeEnd >= dur && timeStart <= dur) {
                    if (positionScroll != i) {
                        positionScroll = i;
                        if (!isCheckRecord) {
                            recySubTitle.smoothScrollToPosition(i);
                            subTitlesAdapter.setSelectedItem(i);
                        } else {
                            recySubTitlePractice.smoothScrollToPosition(i);
                            subTitlePracticeAdapter.setSelectedItem(i);
                        }
                        Log.e("ScrollSubTitle", "Position:: " + i);
                        return;
                    }
                }
            }
        }
    }

    @OnClick(R.id.llrecord)
    void onRecord() {
        audioBG.renderPlayBtn();
        onRecord(audioBG.isPlaying());
        renderButtonRecord();
    }

    private void onRecord(boolean start) {
        if (start) {
            if (!isComplete) startRecording();
            else eventShowDialog();
        } else stopRecording();
    }

    private File setFile(boolean isFile, String folder) {
        int count = 0;
        File folderPath = new File(folder);
        if (folderPath.exists() && folderPath.isDirectory()) {
            for (File file : folderPath.listFiles()) {
                if (file.getName().contains(Constant.FILE_AUDIO)) count++;
            }
        }
        if (!folderPath.exists())
            folderPath.mkdirs();
        String timeStamp = DateTimeUtils.getStringDatetime();
        if (isFile) {
            mFileName = String.format(getResources().getString(R.string.date_time_training), timeStamp, count + 1);
            filePath = folderPath + "/" + mFileName + Constant.FILE_AUDIO;
            return new File(filePath);
        } else {
            String fileName = String.format(getResources().getString(R.string.date_time_training_temp), timeStamp, count + 1);
            filePathTemp = folderPath + "/" + fileName + Constant.FILE_AUDIO;
            return new File(filePathTemp);
        }
    }

    private void resetMediaRecoder() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }


    private void startRecording() {
        resetMediaRecoder();
        AppUtils.enableDisableView(etRole, false);
        mFile = mFile == null ? setFile(true, AppUtils.getFilePathRecoder(getAppContext(), bookId, chapterId)) : mFile;
        AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_RECORDER, true);
        isStartRecord = true;
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setOutputFile(mFileTemp == null ? filePath : filePathTemp);
            mRecorder.setOnErrorListener(errorListener);
            mRecorder.setOnInfoListener(infoListener);
            mRecorder.prepare();
            Thread.sleep(500);
        } catch (IllegalStateException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        mRecorder.start();
    }

    private MediaRecorder.OnErrorListener errorListener = (mr, what, extra) -> Log.e(TAG, "Error: " + what + ", " + extra);

    private MediaRecorder.OnInfoListener infoListener = (mr, what, extra) -> Log.e(TAG, "Info: " + what + ", " + extra);

    private Long getTimeFile(String file) {
        long duration = 0;
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(file);
            String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            duration = Long.parseLong(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

    private void resetFileSub() {
        resetAudio();
        recySubTitlePractice.smoothScrollToPosition(0);
        subTitlePracticeAdapter.setSelectedItem(-1);
    }

    private void addListRecordToJson() {
        String newData = AppUtils.readFromFileRecoder(getContext(), bookId, chapterId, Constant.DO_TRAINING);
        List<RecordItem> listNewRecord = JacksonUtils.convertJsonToListObject(newData, RecordItem.class);
        if (listNewRecord == null)
            listNewRecord = new ArrayList<>();
        listNewRecord.add(new RecordItem(mFileName, filePath, getTimeFile(filePath), System.currentTimeMillis(), isDefaultRole ? role : etRole.getContent()));
        AppUtils.writeToFileRecoder(AppContext.getInstance(), JacksonUtils.writeValueToString(listNewRecord),
                Constant.DO_TRAINING, bookId, chapterId);
    }

    private void renderButtonRecord() {
        if (audioBG.isPlaying())
            imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_record));
        else imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_record));
    }

    private void resetFileRecorder() {
        if (audioBG != null)
            audioBG.stopPlayer();
        if (mRecorder != null) mRecorder.release();
        mRecorder = null;
        if (etRole.getContent().contains(detailTraining.getTrainingAudios().get(0).getName())) {
            startAudioFile(detailTraining.getTrainingAudios(), etRole.getContent());
            Log.e("resetFileRecorder", etRole.getContent());
        } else {
            if (keyValuePair != null)
                startAudioFile(detailTraining.getTrainingAudios(), keyValuePair.getValue());
        }
        recySubTitlePractice.smoothScrollToPosition(0);
        subTitlePracticeAdapter.setSelectedItem(-1);
    }

    private void saveButton() {
        if (mFileTemp != null) Mp4ParserWrapper.append(filePath, filePathTemp);
        addListRecordToJson();
        renderButtonRecord();
        resetFileRecorder();
        cleanFile();
        AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_RECORDER, false);
        AppUtils.enableDisableView(etRole, true);
        isStartRecord = false;
        isComplete = false;
        ToastUtils.showToast(getContext(), String.format(getString(R.string.message_save_training), mFileName));
    }

    private void continueButton() {
        if (mFileTemp == null)
            mFileTemp = setFile(false, AppUtils.getFilePathTempRecoder(getAppContext(), bookId, chapterId));
        else {
            if (Mp4ParserWrapper.append(filePath, filePathTemp)) Log.e("append", "append successs");
            else Log.e("append", "append false");
        }
        isStartRecord = true;
        if (!isComplete) onRecord();
        else recordContinue(isComplete);
    }


    public void recordContinue(boolean isComplete) {
        if (isComplete) startRecording();
        else stopRecording();
        if (isComplete)
            imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_record));
        else imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_record));
    }

    private void cancelButton() {
        resetFile();
        isStartRecord = false;
        isComplete = false;
        resetFileRecorder();
        renderButtonRecord();
        AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_RECORDER, false);
        AppUtils.enableDisableView(etRole, true);
    }

    private void setButtonDialog(int pos) {
        switch (pos) {
            case SAVE_BUTTON:
                saveButton();
                break;
            case CONTINUE_BUTTON:
                continueButton();
                break;
            case CANCEL_BUTTON:
                cancelButton();
                break;
        }
    }

    private void showDialogRecord() {
        confirmRecordDialog = new ConfirmRecordDialog(getContext(), android.R.style.Theme_Light, s -> setButtonDialog(SAVE_BUTTON),
                mFileName, s -> setButtonDialog(CONTINUE_BUTTON),
                s -> setButtonDialog(CANCEL_BUTTON));
        confirmRecordDialog.show();
    }

    private void resetFile() {
        if (mFile != null && mFile.exists())
            mFile.delete();
        if (mFileTemp != null && mFileTemp.exists())
            mFileTemp.delete();
        cleanFile();
    }

    private void stopRecording() {
        try {
            if (mRecorder != null) {
                mRecorder.release();
                isStartRecord = false;
                showDialogRecord();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetMediaRecoder();
        if (confirmRecordDialog != null && confirmRecordDialog.isShowing()) {
            confirmRecordDialog.dismiss();
            confirmRecordDialog = null;
        }
    }

    @OnClick(R.id.llListRecord)
    void listRecord() {
        if (isStartRecord) {
            eventShowDialog();
            return;
        }
        FragmentUtils.replaceFragment(getActivity(), ListRecordFragment.newInstance(itemTrainingResponse.getSubtitle(),
                detailTraining.getTrainingAudios(), roles, bookId, chapterId, itemTrainingResponse.getName()),
                fragment -> mMainActivity.fragments.add(fragment));

    }

}