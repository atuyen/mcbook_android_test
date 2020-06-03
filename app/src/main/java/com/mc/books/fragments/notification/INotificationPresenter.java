package com.mc.books.fragments.notification;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface INotificationPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getListNotification(int start);

    void getUnReadNoti(int id);
}
