package com.mc.books.fragments.home.doSubject;

import android.util.Log;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.home.DetailQuestion;
import com.mc.models.home.SendLog;
import com.mc.utilities.AppUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DoSubjectPresenter<V extends IDoSubjectView> extends BaseDataPresenter<V> implements IDoSubjectPresenter<V> {

    protected DoSubjectPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getDetailQuestion(int bookId, int questionId, int childrenId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getDetailQuestion(bookId, questionId, childrenId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<DetailQuestion>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    v.onErrorDetailQuestion(AppUtils.getErrorMessage(e));
                    Log.e("getDetailQuestion err", e.getMessage());
                }

                @Override
                public void onNext(BaseResponse<DetailQuestion> detailQuestionBaseResponse) {
                    v.onLoadLessonSuccess(detailQuestionBaseResponse.getData());
                }
            });
        });
    }

    @Override
    public void setLogQuestion(int questionId, boolean answer, int bookId, String submitAnswer, String type, int childId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

//            v.onShowLoading(true);
            apiService.sendLogQuestion(questionId, answer, bookId, submitAnswer, type, childId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<SendLog>>() {
                @Override
                public void onCompleted() {
//                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
//                    v.onShowLoading(false);
                }

                @Override
                public void onNext(BaseResponse<SendLog> sendLogBaseResponse) {

                }
            });
        });

    }
}

