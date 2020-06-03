package com.mc.common.views;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IBaseView extends MvpView {
    boolean isValidNetwork();

    void showNetworkRequire();
}
