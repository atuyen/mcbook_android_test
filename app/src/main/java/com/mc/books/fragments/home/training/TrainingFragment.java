package com.mc.books.fragments.home.training;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.sharepreferences.AppPreferences;
import com.mc.adapter.TrainingAdapter;
import com.mc.books.R;
import com.mc.books.fragments.home.doTraining.DoTrainingFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.ItemTrainingResponse;
import com.mc.models.home.TrainingResponse;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;

import java.util.List;

import butterknife.BindView;

import static com.mc.utilities.Constant.BOOK_ID;

public class TrainingFragment extends BaseMvpFragment<ITrainingView, ITrainingPresenter<ITrainingView>> implements ITrainingView {
    public static TrainingFragment newInstance(int bookId, String bookName) {
        Bundle args = new Bundle();
        args.putInt(BOOK_ID, bookId);
        args.putString(Constant.BOOK_NAME, bookName);
        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public ITrainingPresenter<ITrainingView> createPresenter() {
        return new TrainingPresenter<>(getAppComponent());
    }

    @BindView(R.id.rvTraining)
    RecyclerView rvTraining;
    @BindView(R.id.emptyTraining)
    ExtTextView emptyTraining;
    private TrainingAdapter trainingAdapter;

    @Override
    public int getResourceId() {
        return R.layout.training_fragment;
    }

    @Override
    public String getTitleString() {
        String bookName = getArguments().getString(Constant.BOOK_NAME);
        return String.format(getResources().getString(R.string.title_training), getResources().getString(R.string.training), bookName);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        presenter.getListTraining(getArguments().getInt(BOOK_ID));
        mMainActivity.onShowBottomBar(false);
    }

    private void initView() {
        trainingAdapter = new TrainingAdapter(getContext(), this::goToTraining);
        rvTraining.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTraining.setAdapter(trainingAdapter);

    }

    private void goToTraining(ItemTrainingResponse itemTrainingResponse) {
        presenter.getLogTraining(itemTrainingResponse.getId());
        int bookId = getArguments().getInt(Constant.BOOK_ID);
        AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_BACK_TRAINING, false);
        FragmentUtils.replaceFragment(getActivity(), DoTrainingFragment.newInstance(itemTrainingResponse, bookId,
                itemTrainingResponse.getIdTraining()), frag -> mActivity.fragments.add(frag));
    }

    @Override
    public void onShowLoading(boolean loading) {
        showProgress(loading);
    }

    @Override
    public void getListTrainingSuccess(List<TrainingResponse> trainingResponses) {
        if (trainingResponses.size() > 0) {
            rvTraining.setVisibility(View.VISIBLE);
            emptyTraining.setVisibility(View.GONE);
            trainingAdapter.setDataList(trainingResponses);
        } else {
            rvTraining.setVisibility(View.GONE);
            emptyTraining.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getLogTrainingSuccess(ItemTrainingResponse trainingResponse) {
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }
}
