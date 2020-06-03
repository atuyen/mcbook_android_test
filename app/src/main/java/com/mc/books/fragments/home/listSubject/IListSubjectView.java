package com.mc.books.fragments.home.listSubject;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.home.Question;

import java.util.List;

public interface IListSubjectView extends IBaseView {
    void onShowLoading(boolean isShow);

    void getListQuestionSuccess(String point, List<Question> subjects);

    void resetQuestionSuccess();
}
