package com.mc.books.fragments.more.changepass;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.utilities.AppUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePassPresenter<V extends IChangePassView> extends BaseDataPresenter<V> implements IChangePassPresenter<V> {
    protected ChangePassPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void changePass(String pass) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.changePass(pass).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<Object>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    v.onError(AppUtils.getErrorMessage(e));
                }

                @Override
                public void onNext(BaseResponse<Object> obj) {
                    v.onChangePassSuccess();
                }
            });
        });
    }
}
