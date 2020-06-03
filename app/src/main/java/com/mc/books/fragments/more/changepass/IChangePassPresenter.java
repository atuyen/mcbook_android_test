package com.mc.books.fragments.more.changepass;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IChangePassPresenter<V extends MvpView> extends MvpPresenter<V> {
    void changePass(String pass);
}
