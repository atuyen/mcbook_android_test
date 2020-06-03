package com.mc.books.fragments.home.lesson;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;

public interface ILessonView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onDownloadSuccess(int type);

    void onDownloadFail(String error);

    void onDownloadProgressing(int progress);
}
