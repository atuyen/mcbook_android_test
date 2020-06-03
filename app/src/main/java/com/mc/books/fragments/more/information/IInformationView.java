package com.mc.books.fragments.more.information;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.more.FAQ;

public interface IInformationView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onGetIntroSuccess(String content);
}
