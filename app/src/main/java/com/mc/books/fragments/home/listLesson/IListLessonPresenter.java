package com.mc.books.fragments.home.listLesson;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.models.home.Chapter;
import com.mc.models.home.ChapterTemp;
import com.mc.models.home.Lesson;

import java.util.List;

public interface IListLessonPresenter<V extends MvpView> extends MvpPresenter<V> {
    void onSearchLesson(int bookId, String keyword, String code, List<Chapter> chapters);

    void onGetChapters(int bookId);

    void downloadAllLesson(List<Chapter> chapters, int bookId);

    void downloadChapter(ChapterTemp chapterTemp, int bookId);

    void downloadLesson(Lesson lesson, int bookId, int index);
}
