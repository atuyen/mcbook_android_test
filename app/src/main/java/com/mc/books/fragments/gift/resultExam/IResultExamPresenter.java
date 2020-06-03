package com.mc.books.fragments.gift.resultExam;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.models.home.DetailQuestion;

import java.util.List;

public interface IResultExamPresenter<V extends MvpView> extends MvpPresenter<V> {
    void sendLogExam(int examId, String data, int giftId);
}
