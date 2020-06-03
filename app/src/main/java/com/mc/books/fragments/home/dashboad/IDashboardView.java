package com.mc.books.fragments.home.dashboad;

import android.view.View;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.home.CategoryResponse;
import com.mc.models.home.DeleteBook;
import com.mc.models.more.Config;
import com.mc.models.notification.Notification;
import com.mc.models.notification.NotificationLog;
import com.mc.models.notification.NotificationResponse;

import java.util.List;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface IDashboardView extends IBaseView {
    void onShowLoading(boolean isShow);

    void showContextMenuSuccess(boolean isShow, DeleteBook deleteBook, int position);

    void deleteBookSuccess(String message);

    void onGetCategorySuccess(List<CategoryResponse> categoryResponseList);

    void onGetCategoryError(int message);

    void onGetConfigSuccess(Config config);

    void onShowLogNotification(NotificationLog notificationLog);
}
