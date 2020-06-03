package com.mc.books.fragments.more.information;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.models.more.FAQ;

public interface IInformationPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getInfomation();
}
