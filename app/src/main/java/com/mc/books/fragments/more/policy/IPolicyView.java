package com.mc.books.fragments.more.policy;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.more.FAQ;

public interface IPolicyView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onGetLegacySuccess(String content);
}
