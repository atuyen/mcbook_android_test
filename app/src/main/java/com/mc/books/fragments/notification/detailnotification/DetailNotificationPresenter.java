package com.mc.books.fragments.notification.detailnotification;

import android.util.Log;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.notification.DetailNotificationAdmin;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailNotificationPresenter<V extends IDetailNotificationView> extends BaseDataPresenter<V> implements IDetailNotificationPresenter<V> {
    /**
     * @param appComponent
     */
    protected DetailNotificationPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getDetailNotification(int id) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getDetailNotification(id).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<DetailNotificationAdmin>>() {
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
                public void onNext(BaseResponse<DetailNotificationAdmin> detailNotificatione) {
                    v.showDetailNotification(detailNotificatione.getData());
                }
            });
        });
    }
}
