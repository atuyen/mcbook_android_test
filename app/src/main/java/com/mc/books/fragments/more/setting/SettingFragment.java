package com.mc.books.fragments.more.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Switch;

import com.bon.sharepreferences.AppPreferences;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.utilities.Constant;
import com.onesignal.OneSignal;

import butterknife.BindView;

public class SettingFragment extends BaseMvpFragment<ISettingView, ISettingPresenter<ISettingView>> implements ISettingView {
    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.swReceive)
    Switch swReceive;
    @BindView(R.id.swSound)
    Switch swSound;

    @Override
    public ISettingPresenter<ISettingView> createPresenter() {
        return new SettingPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.setting_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    private void initView() {
        swReceive.setOnCheckedChangeListener((compoundButton, isCheck) -> {
            AppPreferences.getInstance(getContext()).putBoolean(Constant.DISABLE_RECEIVE_NOTI, !isCheck);
            OneSignal.setSubscription(isCheck);
        });

        swSound.setOnCheckedChangeListener((compoundButton, isCheck) -> {
            AppPreferences.getInstance(getContext()).putBoolean(Constant.DISABLE_SOUND_NOTI, !isCheck);
            OneSignal.enableSound(isCheck);
        });
    }

    @Override
    public int getTitleId() {
        return R.string.setting;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        supportActionBar.show();
    }
}
