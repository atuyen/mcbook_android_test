package com.mc.books.fragments.home.listLesson;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.home.Chapter;

import java.util.List;

public interface IListLessonView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onSearchLesson(List<Chapter> chapters);

    void onGetChapters(List<Chapter> chapters);
}
