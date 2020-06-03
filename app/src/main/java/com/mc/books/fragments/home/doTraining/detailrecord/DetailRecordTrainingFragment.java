package com.mc.books.fragments.home.doTraining.detailrecord;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.bon.sharepreferences.AppPreferences;
import com.mc.adapter.SubTitlePracticeAdapter;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.subtitles.SubtitlesView;
import com.mc.models.home.RecordItem;
import com.mc.models.home.SubTitles;
import com.mc.models.home.TrainingAudio;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bg.player.com.playerbackground.module.AudioPlayerBG;
import bg.player.com.playerbackground.module.AudioPlayerInteface;
import butterknife.BindView;

public class DetailRecordTrainingFragment extends BaseMvpFragment<IDetailRecordView, IDetailRecordPresenter<IDetailRecordView>> implements IDetailRecordView {

    @BindView(R.id.audioBG)
    AudioPlayerBG audioBG;
    @BindView(R.id.txtRole)
    ExtTextView txtRole;
    private RecordItem recordItem;
    @BindView(R.id.recySubTitle)
    RecyclerView recySubTitle;
    SubTitlePracticeAdapter subTitlePracticeAdapter;
    private SubtitlesView srt;
    private ArrayList<SubTitles> subTitlesList = new ArrayList<>();

    public static DetailRecordTrainingFragment newInstance(String subtitle, List<TrainingAudio> trainingAudios, RecordItem recordItem, List<ExtKeyValuePair> roles) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.KEY_RECORD_DETAIL, recordItem);
        args.putString(Constant.KEY_SUBTITLE, subtitle);
        args.putSerializable(Constant.KEY_TRAININGAUDIO, (Serializable) trainingAudios);
        args.putSerializable(Constant.KEY_LIST_ROLE, (Serializable) roles);
        DetailRecordTrainingFragment fragment = new DetailRecordTrainingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public IDetailRecordPresenter<IDetailRecordView> createPresenter() {
        return new DetailRecordTrainingPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.layout_detail_record;
    }

    @Override
    public String getTitleString() {
        return AppUtils.removeEndPath(recordItem.getName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        initPlayer();
    }

    private void initView() {
        String subtitle = getArguments().getString(Constant.KEY_SUBTITLE);
        List<TrainingAudio> trainingAudios = (List<TrainingAudio>) getArguments().getSerializable(Constant.KEY_TRAININGAUDIO);
        List<ExtKeyValuePair> roles = (List<ExtKeyValuePair>) getArguments().getSerializable(Constant.KEY_LIST_ROLE);
        recordItem = (RecordItem) getArguments().getSerializable(Constant.KEY_RECORD_DETAIL);
        txtRole.setText(recordItem.getRole());
        subTitlePracticeAdapter = new SubTitlePracticeAdapter(getAppContext());
        recySubTitle.setLayoutManager(new LinearLayoutManager(getAppContext()));
        recySubTitle.setAdapter(subTitlePracticeAdapter);
        srt = new SubtitlesView(getAppContext());
        srt.getSubtitleUri(Uri.parse(subtitle),
                trainingAudios, roles);
        srt.setSubTitlesCallback(subTitles -> {
            subTitlesList.clear();
            subTitlesList.addAll(subTitles);
            subTitlePracticeAdapter.setDataList(subTitles);
            checkRole(recordItem.getRole());
        });
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    private void initPlayer() {
        audioBG.setAudioPlayerInteface(new AudioPlayerInteface() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onNext() {
            }

            @Override
            public void onPrevious() {
            }

            @Override
            public void onDuration(long dur) {
                startSubTitle(dur);
                Log.e("dur", dur + "");
            }

            @Override
            public void onEventTraining() {
                if (!AppPreferences.getInstance(getAppContext()).getBoolean(Constant.KEY_RESET_AUDIO))
                    audioBG.resetPlayButton();
            }

        });
        audioBG.setAutoPlay(true);
        audioBG.setStreamAudio(true);
        audioBG.setAudioFileUri(Uri.fromFile(new File(recordItem.getPath())));
    }

    private void checkRole(String role) {
        if (srt != null) {
            for (int i = 0; i < subTitlesList.size(); i++) {
                String roleName = subTitlesList.get(i).getItemSub().split(Constant.KEY_DOT)[0];
                SubTitles subTitles = subTitlePracticeAdapter.getItem(i);
                if (subTitles != null) {
                    subTitles.setSelected(role.equals(roleName));
                }
            }
            subTitlePracticeAdapter.notifyDataSetChanged();
        }
    }

    int positionScroll = -1;

    private void startSubTitle(long dur) {
        if (srt != null) {
            for (int i = 0; i < subTitlesList.size(); i++) {
                SubTitles subTitles = subTitlesList.get(i);
                long timeStart = subTitles.getTimeStart();
                long timeEnd = subTitles.getTimeEnd();
                if (timeEnd >= dur && timeStart <= dur) {
                    if (positionScroll != i) {
                        positionScroll = i;
                        recySubTitle.smoothScrollToPosition(i);
                        subTitlePracticeAdapter.setSelectedItem(i);
                        Log.e("ScrollSubTitle", "Position:: " + i);
                        return;
                    }
                }
            }
        }
    }
}
