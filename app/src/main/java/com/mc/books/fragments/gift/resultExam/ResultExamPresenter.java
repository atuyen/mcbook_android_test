package com.mc.books.fragments.gift.resultExam;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.gift.Point;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ResultExamPresenter<V extends IResultExamView> extends BaseDataPresenter<V> implements IResultExamPresenter<V> {

    protected ResultExamPresenter(AppComponent appComponent) {
        super(appComponent);
    }


    @Override
    public void sendLogExam(int examId, String data, int giftId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            v.onShowLoading(true);
            apiService.sendLogExam(examId, data, giftId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<Point>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    v.sendLogExamError();
                }

                @Override
                public void onNext(BaseResponse<Point> pointBaseResponseoint) {
                    v.sendLogExamSuccess(pointBaseResponseoint.getData().getPoint());
                }
            });
        });
    }
}
