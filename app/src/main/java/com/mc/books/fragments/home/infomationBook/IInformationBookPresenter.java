package com.mc.books.fragments.home.infomationBook;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IInformationBookPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getDetailBook(int bookId);
}
