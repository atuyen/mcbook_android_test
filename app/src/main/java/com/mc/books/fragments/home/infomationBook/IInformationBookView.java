package com.mc.books.fragments.home.infomationBook;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.home.BookResponse;

public interface IInformationBookView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onGetDetailBookSuccess(BookResponse bookResponse);
}
