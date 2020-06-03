package com.mc.books.fragments.more.support;

import android.util.Log;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.more.Config;
import com.mc.models.more.FAQ;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SupportPresenter<V extends ISupportView> extends BaseDataPresenter<V> implements ISupportPresenter<V> {

    protected SupportPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void onGetFAQ() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getFAQ().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<FAQ>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("onGetFAQ err", e.getMessage());
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<FAQ>> arrayResponseBaseResponse) {
                    v.onGetFAQSuccess(arrayResponseBaseResponse.getData().getRows());
                }
            });

        });
    }

    @Override
    public void getConfig() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getConfig().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<Config>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("getConfig err", e.getMessage());
                }

                @Override
                public void onNext(BaseResponse<Config> configBaseResponse) {
                    v.onGetConfigSuccess(configBaseResponse.getData());
                }
            });

        });
    }
}
