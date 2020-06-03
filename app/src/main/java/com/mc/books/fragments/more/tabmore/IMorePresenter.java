package com.mc.books.fragments.more.tabmore;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface IMorePresenter<V extends MvpView> extends MvpPresenter<V> {
      void logout(Context context);
}
