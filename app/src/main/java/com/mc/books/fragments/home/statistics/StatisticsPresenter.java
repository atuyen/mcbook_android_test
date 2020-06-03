package com.mc.books.fragments.home.statistics;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.home.BookProcess;
import com.mc.models.home.BookProcessResponse;
import com.mc.models.home.BookStatistic;
import com.mc.models.home.BookStatisticResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StatisticsPresenter<V extends IStatisticsView> extends BaseDataPresenter<V> implements IStatisticsPresenter<V> {

    protected StatisticsPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void onLoadBookStatistic() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            v.onShowLoading(true);
            apiService.getBookProcess()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BookProcessResponse<BookProcess>>() {
                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onNext(BookProcessResponse<BookProcess> bookProcessResponse) {
                            v.getListBookProcess(bookProcessResponse.getData());
                        }

                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                            apiService.getBookStatistic()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<BookStatisticResponse<BookStatistic>>() {
                                        @Override
                                        public void onError(Throwable e) {
                                            v.onShowLoading(false);
                                        }

                                        @Override
                                        public void onNext(BookStatisticResponse<BookStatistic> bookStatisticResponse) {
                                            v.getListBookStatistic(bookStatisticResponse.getData());
                                        }

                                        @Override
                                        public void onCompleted() {
                                            v.onShowLoading(false);

                                        }

                                    });
                        }
                    });

        });
    }

}
