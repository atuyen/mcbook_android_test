package com.mc.books.fragments.gift.doExam;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;

public class DoExamPresenter<V extends IDoExamView> extends BaseDataPresenter<V> implements IDoExamPresenter<V> {
    protected DoExamPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
