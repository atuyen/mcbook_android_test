package com.mc.books.fragments.gift.noteExam;


import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;

public class NoteExamPresenter<V extends INoteExamView> extends BaseDataPresenter<V> implements INoteExamPresenter<V> {
    protected NoteExamPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
