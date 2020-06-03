package com.mc.books.fragments.notification.detailnotification;

import com.mc.common.views.IBaseView;
import com.mc.models.notification.DetailNotificationAdmin;

public interface IDetailNotificationView extends IBaseView {
    void onShowLoading(boolean isShow);

    void showDetailNotification(DetailNotificationAdmin detailNotification);
}
