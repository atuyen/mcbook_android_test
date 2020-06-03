package com.mc.books.fragments.home.lessonPdf;

import com.mc.common.views.IBaseView;

public interface ILessonPdfView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onDownloadSuccess();

    void onDownloadFail(String error);

    void onDownloadProgressing(int progress);
}
