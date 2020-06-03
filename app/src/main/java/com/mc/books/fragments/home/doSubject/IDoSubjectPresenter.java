package com.mc.books.fragments.home.doSubject;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IDoSubjectPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getDetailQuestion(int bookId, int questionId, int childrenId);

    void setLogQuestion(int questionId, boolean answer, int bookId, String submitAnswer, String type, int childId);
}
