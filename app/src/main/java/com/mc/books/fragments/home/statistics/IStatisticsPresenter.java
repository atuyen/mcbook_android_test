package com.mc.books.fragments.home.statistics;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IStatisticsPresenter<V extends MvpView> extends MvpPresenter<V> {
    void onLoadBookStatistic();
}
