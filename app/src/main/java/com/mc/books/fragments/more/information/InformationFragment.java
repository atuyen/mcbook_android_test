package com.mc.books.fragments.more.information;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;

import butterknife.BindView;

public class InformationFragment extends BaseMvpFragment<IInformationView, IInformationPresenter<IInformationView>> implements IInformationView {
    public static InformationFragment newInstance() {
        Bundle args = new Bundle();
        InformationFragment fragment = new InformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.txtContent)
    ExtTextView txtContent;

    @NonNull
    @Override
    public IInformationPresenter<IInformationView> createPresenter() {
        return new InformationPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.information_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.information;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        supportActionBar.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        presenter.getInfomation();
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onGetIntroSuccess(String content) {
        txtContent.setText(Html.fromHtml(content));
    }
}
