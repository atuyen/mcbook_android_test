package com.mc.common.fragments;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.mc.application.AppContext;
import com.mc.di.AppComponent;

/**
 * Created by dangpp on 2/21/2018.
 */

public interface IBaseFragment {
    AppContext getAppContext();

    AppComponent getAppComponent();

    void bindButterKnife(View view);

    int getTitleId();

    String getTitleString();

    void initToolbar(@NonNull ActionBar supportActionBar);

    void showProgress(boolean show);


}
