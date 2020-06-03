package com.mc.books.fragments.notification;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.notification.Notification;
import com.mc.models.notification.UnReadNotification;

import java.util.List;

public interface INotificationView extends IBaseView {
    void onShowLoading(boolean isShow);

    void showNotification(List<Notification> notifications);

    void showUnReadNoti(UnReadNotification unReadNotification);
}
