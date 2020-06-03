package com.mc.books.fragments.home.lessonVideo;


import com.mc.common.views.IBaseView;

public interface ILessonVideoView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onDownloadSuccess();

    void onDownloadFail(String error);

    void onDownloadProgressing(int progress);
}
