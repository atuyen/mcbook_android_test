package com.mc.books.fragments.home.listRelatedBook;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IListRelatedBookPresenter<V extends MvpView> extends MvpPresenter<V> {
    void onLoadListRelatedBook(int bookId);
}
