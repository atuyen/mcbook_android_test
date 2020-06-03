package com.mc.books.fragments.home.doTraining;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.io.File;

public interface IDoTrainingPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getDetailTraining(int id);
}
