package com.mc.books.fragments.more.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.mc.adapter.FAQAdapter;
import com.mc.books.R;
import com.mc.books.dialog.FAQDialog;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.more.Config;
import com.mc.models.more.FAQ;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SupportFragment extends BaseMvpFragment<ISupportView, ISupportPresenter<ISupportView>> implements ISupportView {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();
        SupportFragment fragment = new SupportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.txtPhoneNumber)
    ExtTextView txtPhoneNumber;
    @BindView(R.id.cvCall)
    CardView cvCall;
    @BindView(R.id.cvMesenger)
    CardView cvMesenger;
    @BindView(R.id.rvFAQ)
    RecyclerView rvFAQ;
    private FAQAdapter faqAdapter;
    private String url;

    @NonNull
    @Override
    public ISupportPresenter<ISupportView> createPresenter() {
        return new SupportPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initData();
        presenter.onGetFAQ();
        presenter.getConfig();
    }

    private void initData() {
        try {
            faqAdapter = new FAQAdapter(getContext(), this::showFAQ);
            rvFAQ.setLayoutManager(new LinearLayoutManager(getContext()));
            rvFAQ.setNestedScrollingEnabled(false);
            rvFAQ.setAdapter(faqAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFAQ(FAQ faq) {
        FAQDialog faqDialog = new FAQDialog(getContext(), faq);
        faqDialog.show();
    }

    @Override
    public int getResourceId() {
        return R.layout.support_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.support;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        supportActionBar.show();
    }

    @OnClick({R.id.cvCall, R.id.cvMesenger})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.cvCall:
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + txtPhoneNumber.getText().toString()));
                    startActivity(intent);
                    break;
                case R.id.cvMesenger:
                    Intent intentMessenger = new Intent();
                    intentMessenger.setAction(Intent.ACTION_VIEW);
                    intentMessenger.setData(Uri.parse(url));
                    startActivity(intentMessenger);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onGetFAQSuccess(List<FAQ> faqList) {
        faqAdapter.setDataList(faqList);
    }

    @Override
    public void onGetConfigSuccess(Config config) {
        txtPhoneNumber.setText(config.getPhone());
        this.url = config.getFacebook();
    }
}
