package com.mc.books.fragments.home.doTraining;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.home.DetailTraining;
import com.mc.models.home.RecordItem;
import com.mc.utilities.AppUtils;
import java.io.File;
import java.util.ArrayList;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DoTrainingPresenter<V extends IDoTrainingView> extends BaseDataPresenter<V> implements IDoTrainingPresenter<V> {
    protected DoTrainingPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getDetailTraining(int id) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getDetailTraining(id, "app").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<DetailTraining>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    v.getDetailTrainingError(e.getMessage());
                }

                @Override
                public void onNext(BaseResponse<DetailTraining> response) {
                    v.getDetailTrainingSuccess(response.getData());
                }
            });
        });
    }
}
