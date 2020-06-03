package com.mc.books.fragments.home.booktab;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;

public class BookTabPresenter<V extends IBookTabView> extends BaseDataPresenter<V> implements IBookTabPresenter<V> {
    protected BookTabPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
