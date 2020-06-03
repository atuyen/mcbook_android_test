package com.mc.books.fragments.notification.detailnotification;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IDetailNotificationPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getDetailNotification(int id);
}
