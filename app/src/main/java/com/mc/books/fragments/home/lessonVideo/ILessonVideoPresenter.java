package com.mc.books.fragments.home.lessonVideo;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ILessonVideoPresenter<V extends MvpView> extends MvpPresenter<V> {
    void onDownloadFile(String url, String fileName, Context context, String folderDownload, int folderId, int fileId, String name);

    void onDownloadSub(String url, String fileName, Context context, String folderDownload, int folderId, int fileId, String name);

    void sendLogLesson(int lessonId, int bookId);
}
