package com.mc.books.fragments.home.doTraining.subtraining;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.collection.CollectionUtils;
import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.bon.jackson.JacksonUtils;
import com.bon.sharepreferences.AppPreferences;
import com.mc.adapter.RecordAdapter;
import com.mc.books.R;
import com.mc.books.fragments.home.doTraining.detailrecord.DetailRecordTrainingFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.RecordItem;
import com.mc.models.home.TrainingAudio;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ListRecordFragment extends BaseMvpFragment<IListRecordView, IListRecordPresenter<IListRecordView>> implements IListRecordView {
    private static final String TAG = "ListRecordFragment";
    private List<RecordItem> recordItemList = new ArrayList<>();
    @BindView(R.id.recyleRecord)
    RecyclerView recyleRecord;
    @BindView(R.id.txtEmpty)
    ExtTextView txtEmpty;
    private RecordAdapter recordAdapter;

    public static ListRecordFragment newInstance(String subtitle, List<TrainingAudio> trainingAudios,
                                                 List<ExtKeyValuePair> roles, int bookId, int chapterId, String conversation) {
        Bundle args = new Bundle();
        args.putString(Constant.KEY_SUBTITLE, subtitle);
        args.putInt(Constant.BOOK_ID, bookId);
        args.putInt(Constant.TRAINING_CHAPTER_ID, chapterId);
        args.putString(Constant.KEY_CONVERSATION, conversation);
        args.putSerializable(Constant.KEY_TRAININGAUDIO, (Serializable) trainingAudios);
        args.putSerializable(Constant.KEY_LIST_ROLE, (Serializable) roles);
        ListRecordFragment fragment = new ListRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.layout_record;
    }

    @Override
    public IListRecordPresenter<IListRecordView> createPresenter() {
        return new ListRecordPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    private void initView() {
        int bookId = getArguments().getInt(Constant.BOOK_ID);
        int chapterId = getArguments().getInt(Constant.TRAINING_CHAPTER_ID);
        AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_RESET_AUDIO, false);
        String data = AppUtils.readFromFileRecoder(getContext(), bookId, chapterId, Constant.DO_TRAINING);
        String subtitle = getArguments().getString(Constant.KEY_SUBTITLE);
        List<ExtKeyValuePair> roles = (List<ExtKeyValuePair>) getArguments().getSerializable(Constant.KEY_LIST_ROLE);
        List<TrainingAudio> trainingAudios = (List<TrainingAudio>) getArguments().getSerializable(Constant.KEY_TRAININGAUDIO);
        recordItemList = JacksonUtils.convertJsonToListObject(data, RecordItem.class);
        if (CollectionUtils.isNotNullOrEmpty(recordItemList)) {
            recordAdapter = new RecordAdapter(getAppContext(), recordItem ->
                    FragmentUtils.replaceFragment(getActivity(), DetailRecordTrainingFragment.newInstance(subtitle, trainingAudios, recordItem, roles),
                            fragment -> mMainActivity.fragments.add(fragment)));
            recyleRecord.setLayoutManager(new LinearLayoutManager(getAppContext()));
            AppUtils.sortlistbyTimeRecord(recordItemList);
            recyleRecord.setAdapter(recordAdapter);
            recordAdapter.setDataList(recordItemList);
            txtEmpty.setVisibility(View.GONE);
            recyleRecord.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
            recyleRecord.setVisibility(View.GONE);
        }
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public String getTitleString() {
        String conversation = getArguments().getString(Constant.KEY_CONVERSATION);
        return String.format(getResources().getString(R.string.title_training), getResources().getString(R.string.title_record), conversation);
    }
}
