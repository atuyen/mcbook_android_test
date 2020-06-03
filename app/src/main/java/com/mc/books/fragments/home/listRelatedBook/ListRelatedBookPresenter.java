package com.mc.books.fragments.home.listRelatedBook;

import android.util.Log;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.home.RelatedBook;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListRelatedBookPresenter<V extends IListRelatedBookView> extends BaseDataPresenter<V> implements IListRelatedBookPresenter<V> {
    protected ListRelatedBookPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void onLoadListRelatedBook(int bookId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getListRelatedBook(bookId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<RelatedBook>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("listRelatedBook err", e.toString());
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<RelatedBook>> arrayResponseBaseResponse) {
                    v.onLoadListRelatedBookSuccesss(arrayResponseBaseResponse.getData().getRows());
                }
            });
        });
    }
}
