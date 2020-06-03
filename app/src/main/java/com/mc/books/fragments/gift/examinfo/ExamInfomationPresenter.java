package com.mc.books.fragments.gift.examinfo;


import android.util.Log;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.gift.InfomationExam;
import com.mc.models.home.DetailQuestion;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExamInfomationPresenter<V extends IExamInfomationView> extends BaseDataPresenter<V> implements IExamInfomationPresenter<V> {
    protected ExamInfomationPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getListQuestionExam(int examId, int giftId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getListQuestionExam(examId, giftId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<DetailQuestion>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    v.getListQuestionExamError();
                    Log.e("getListQuestionExam err", e.toString());
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<DetailQuestion>> arrayResponseBaseResponse) {
                    v.onShowLoading(false);
                    v.getListQuestionExamSuccess(arrayResponseBaseResponse.getData().getRows());
                }
            });
        });
    }

    @Override
    public void showInfomationExam(int id, int giftId, int giftUserId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getInfomationExam(id, giftId, giftUserId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<InfomationExam>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                }

                @Override
                public void onNext(BaseResponse<InfomationExam> infomationExamBaseResponse) {
                    v.onShowLoading(false);
                    v.getInfomationExam(infomationExamBaseResponse.getData());
                }
            });
        });
    }
}
