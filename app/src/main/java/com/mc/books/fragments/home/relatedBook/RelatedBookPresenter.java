package com.mc.books.fragments.home.relatedBook;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;

public class RelatedBookPresenter<V extends IRelatedBookView> extends BaseDataPresenter<V> implements IRelatedBookPresenter<V> {

    protected RelatedBookPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
