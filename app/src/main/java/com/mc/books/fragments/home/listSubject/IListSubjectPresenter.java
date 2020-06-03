package com.mc.books.fragments.home.listSubject;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IListSubjectPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getListQuestion(int bookId);

    void onResetQuestion(int bookId);
}
