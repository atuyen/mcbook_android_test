package com.mc.books.fragments.more.policy;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IPolicyPresenter<V extends MvpView> extends MvpPresenter<V> {
    void onGetLegacy();
}
