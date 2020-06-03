package com.mc.books.fragments.home.doSubject;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.home.DetailQuestion;
import com.mc.models.home.Question;

public interface IDoSubjectView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onLoadLessonSuccess(DetailQuestion detailQuestion);

    void onErrorDetailQuestion(int error);
}
