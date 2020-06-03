package com.mc.books.fragments.more.information;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.ContentResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InformationPresenter<V extends IInformationView> extends BaseDataPresenter<V> implements IInformationPresenter<V> {

    protected InformationPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getInfomation() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getPostInfo().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ContentResponse>>() {
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
                    v.onGetIntroSuccess(contentResponseBaseResponse.getData().getContent());
                }
            });
        });
    }
}
