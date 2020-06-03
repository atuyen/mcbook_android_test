package com.mc.books.fragments.home.training;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ITrainingPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getListTraining(int bookId);

    void getLogTraining(int id);
}
