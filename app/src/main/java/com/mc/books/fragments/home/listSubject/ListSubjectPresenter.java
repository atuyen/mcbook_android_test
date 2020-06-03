package com.mc.books.fragments.home.listSubject;

import android.util.Log;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.MessageResponse;
import com.mc.models.home.Question;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListSubjectPresenter<V extends IListSubjectView> extends BaseDataPresenter<V> implements IListSubjectPresenter<V> {
    protected ListSubjectPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getListQuestion(int bookId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getListQuestion(bookId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<Question>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("getListSubject err", e.toString());
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<Question>> arrayResponseBaseResponse) {
                    v.getListQuestionSuccess(arrayResponseBaseResponse.getData().getPoint(), arrayResponseBaseResponse.getData().getRows());
                }
            });
        });

    }

    @Override
    public void onResetQuestion(int bookId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.resetQuestion(bookId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<MessageResponse>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                }

                @Override
                public void onNext(MessageResponse messageResponse) {
                    v.resetQuestionSuccess();
                }
            });
        });
    }
}
