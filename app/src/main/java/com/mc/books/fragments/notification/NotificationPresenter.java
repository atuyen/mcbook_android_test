package com.mc.books.fragments.notification;

import android.util.Log;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.notification.NotificationResponse;
import com.mc.models.notification.UnReadNotification;
import com.mc.utilities.Constant;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationPresenter<V extends INotificationView> extends BaseDataPresenter<V> implements INotificationPresenter<V> {

    protected NotificationPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getListNotification(int start) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            Map<String, String> maps = new HashMap<>();
            maps.put("start", String.valueOf(start));
            maps.put("limit", String.valueOf(Constant.LIMIT_PAGING));
            v.onShowLoading(true);
            apiService.getNotificationList(maps).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<NotificationResponse>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("tuan", e.getMessage());
                    v.onShowLoading(false);
                }

                @Override
                public void onNext(BaseResponse<NotificationResponse> notificationResponse) {
                    v.showNotification(notificationResponse.getData().getNotificationList());
                }
            });
        });
    }

    @Override
    public void getUnReadNoti(int id) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            v.onShowLoading(true);
            Map<String, Boolean> map = new HashMap<>();
            map.put("isRead", true);
            apiService.unReadNoti(id, map).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<UnReadNotification>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("tuan", e.getMessage());
                    v.onShowLoading(false);
                }

                @Override
                public void onNext(BaseResponse<UnReadNotification> unReadNotification) {
                    v.showUnReadNoti(unReadNotification.getData());
                }

            });
        });
    }
}
