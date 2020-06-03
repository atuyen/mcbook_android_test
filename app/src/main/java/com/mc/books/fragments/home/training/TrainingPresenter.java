package com.mc.books.fragments.home.training;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.home.AddBook;
import com.mc.models.home.ItemTrainingResponse;
import com.mc.models.home.TrainingResponse;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TrainingPresenter<V extends ITrainingView> extends BaseDataPresenter<V> implements ITrainingPresenter<V> {
    protected TrainingPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getListTraining(int bookId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            v.onShowLoading(true);
            Map<String, String> maps = new HashMap<>();
            maps.put("type", "app");
            maps.put("filter", "[{\"operator\":\"eq\",\"value\":" + bookId + ",\"property\":\"bookId\"}]");
            maps.put("sort", "[{\"property\":\"createdAt\",\"direction\":\"ASC\"}]");
            apiService.getListTraining(maps).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<TrainingResponse>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<TrainingResponse>> arrayResponseBaseResponse) {
                    v.getListTrainingSuccess(arrayResponseBaseResponse.getData().getRows());
                }
            });
        });
    }

    @Override
    public void getLogTraining(int id) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            Map<String, Boolean> maps = new HashMap<>();
            maps.put("isRead", true);
            apiService.sendLogTraining(id, maps).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ItemTrainingResponse>>() {
                @Override
                public void onCompleted() {
                    //v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    //v.onShowLoading(false);
                }

                @Override
                public void onNext(BaseResponse<ItemTrainingResponse> itemTrainingResponse) {
                    v.getLogTrainingSuccess(itemTrainingResponse.getData());
                }
            });
        });
    }


}
