package com.mc.books.fragments.more.support;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ISupportPresenter<V extends MvpView> extends MvpPresenter<V> {
    void onGetFAQ();

void getConfig();
}
