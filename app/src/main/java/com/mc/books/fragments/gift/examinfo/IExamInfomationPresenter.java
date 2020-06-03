package com.mc.books.fragments.gift.examinfo;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IExamInfomationPresenter<V extends MvpView> extends MvpPresenter<V> {

    void getListQuestionExam(int examId, int giftId);

    void showInfomationExam(int id, int giftId, int giftUserId);

}
