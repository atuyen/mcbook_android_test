package com.mc.books.fragments.more.tabmore;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface IMoreView extends IBaseView {
   void onShowLoading(boolean isShow);

   void logoutSuccess();
}
