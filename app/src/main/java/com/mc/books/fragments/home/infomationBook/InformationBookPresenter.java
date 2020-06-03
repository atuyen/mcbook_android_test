package com.mc.books.fragments.home.infomationBook;

import android.util.Log;

import com.bon.jackson.JacksonUtils;
import com.mc.application.AppContext;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.home.BookResponse;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InformationBookPresenter<V extends IInformationBookView> extends BaseDataPresenter<V> implements IInformationBookPresenter<V> {

    protected InformationBookPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getDetailBook(int bookId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getDetailBook(bookId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<BookResponse>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("getDetailBook error", e.toString());
                }

                @Override
                public void onNext(BaseResponse<BookResponse> response) {
                    //write json file
                    AppUtils.writeToFile(AppContext.getInstance(), JacksonUtils.writeValueToString(response.getData()), Constant.INFO_BOOK + "_" + bookId);
                    v.onGetDetailBookSuccess(response.getData());
                }
            });
        });
    }
}
