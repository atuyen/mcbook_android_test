package com.mc.books.fragments.more.policy;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.ContentResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PolicyPresenter<V extends IPolicyView> extends BaseDataPresenter<V> implements IPolicyPresenter<V> {

    protected PolicyPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void onGetLegacy() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getPostPolicy().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ContentResponse>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                }

                @Override
                public void onNext(BaseResponse<ContentResponse> contentResponseBaseResponse) {
                    v.onGetLegacySuccess(contentResponseBaseResponse.getData().getContent());
                }
            });
        });
    }
}
