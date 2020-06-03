package com.mc.books.fragments.more.policy;

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

public class PolicyFragment extends BaseMvpFragment<IPolicyView, IPolicyPresenter<IPolicyView>> implements IPolicyView {
    public static PolicyFragment newInstance() {
        Bundle args = new Bundle();
        PolicyFragment fragment = new PolicyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.txtContent)
    ExtTextView txtContent;

    @NonNull
    @Override
    public IPolicyPresenter<IPolicyView> createPresenter() {
        return new PolicyPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        presenter.onGetLegacy();
    }

    private void initData(String content) {
        try {
            txtContent.setText(Html.fromHtml(content));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getResourceId() {
        return R.layout.policy_fragment;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        supportActionBar.show();
    }

    @Override
    public int getTitleId() {
        return R.string.legal;
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onGetLegacySuccess(String content) {
        initData(content);
    }
}
