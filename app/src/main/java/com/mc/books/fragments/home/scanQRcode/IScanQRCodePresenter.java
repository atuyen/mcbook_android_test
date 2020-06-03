package com.mc.books.fragments.home.scanQRcode;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IScanQRCodePresenter<V extends MvpView> extends MvpPresenter<V> {
    void onCreateBook(String code);

    void onSearchLesson(int bookId, String keyword, String code);
}
