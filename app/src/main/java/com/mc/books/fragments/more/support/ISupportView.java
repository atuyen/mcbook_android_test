package com.mc.books.fragments.more.support;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.more.Config;
import com.mc.models.more.FAQ;

import java.util.List;

public interface ISupportView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onGetFAQSuccess(List<FAQ> faqList);

    void onGetConfigSuccess(Config config);
}
