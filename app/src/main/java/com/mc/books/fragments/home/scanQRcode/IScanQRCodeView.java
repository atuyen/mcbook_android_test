package com.mc.books.fragments.home.scanQRcode;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.home.AddBook;
import com.mc.models.home.Chapter;
import com.mc.models.home.Lesson;

import java.util.List;

public interface IScanQRCodeView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onCreateBookSuccess(AddBook addBook);

    void onCreateBookError(int stringError);

    void onSearchLessonSuccess(Lesson lesson);

    void onSearchLessonError(int stringError);


}
