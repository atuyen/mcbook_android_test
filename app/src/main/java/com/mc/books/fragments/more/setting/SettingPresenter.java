package com.mc.books.fragments.more.setting;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;

public class SettingPresenter<V extends ISettingView> extends BaseDataPresenter<V> implements ISettingPresenter<V> {
    protected SettingPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
