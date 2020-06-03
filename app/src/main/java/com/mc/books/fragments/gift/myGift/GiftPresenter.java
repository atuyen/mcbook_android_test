package com.mc.books.fragments.gift.myGift;

import android.util.Log;
import android.view.View;

import com.bon.jackson.JacksonUtils;
import com.mc.application.AppContext;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.gift.AddGift;
import com.mc.models.gift.CategoryGift;
import com.mc.models.home.SendLog;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GiftPresenter<V extends IGiftView> extends BaseDataPresenter<V> implements IGiftPresenter<V> {

    protected GiftPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void onSearchGift(String keyword, List<CategoryGift> categoryGiftList) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) {
                List<CategoryGift> filter = StreamSupport.stream(new ArrayList<>(categoryGiftList))
                        .map(cate -> new CategoryGift(cate.getId(), cate.getName(), StreamSupport.stream(cate.getGifts())
                                .filter(n -> n.getName().contains(keyword)).collect(Collectors.toList())))
                        .filter(cate -> cate.getGifts().size() > 0).collect(Collectors.toList());
                v.onGetCategoryGiftSuccess(filter);
            } else {
                v.onShowLoading(true);
                apiService.searchCategoryGift(keyword.trim()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<CategoryGift>>>() {
                    @Override
                    public void onCompleted() {
                        v.onShowLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        v.onShowLoading(false);
                        Log.e("onGetCategoryGift err", e.toString());
                    }

                    @Override
                    public void onNext(BaseResponse<ArrayResponse<CategoryGift>> arrayResponseBaseResponse) {
                        v.onGetCategoryGiftSuccess(arrayResponseBaseResponse.getData().getRows());
                    }
                });
            }
        });
    }

    @Override
    public void onShowDialogDelete(boolean isShow, View view, int position) {
        getOptView().doIfPresent(v -> v.onShowDialogDelete(isShow, view, position));
    }

    @Override
    public void onGetCategoryGift() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getCategoryGift().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<CategoryGift>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("onGetCategoryGift err", e.toString());
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<CategoryGift>> arrayResponseBaseResponse) {
                    AppUtils.writeToFile(AppContext.getInstance(), JacksonUtils.writeValueToString(arrayResponseBaseResponse.getData().getRows()), Constant.MY_GIFT);
                    v.onGetCategoryGiftSuccess(arrayResponseBaseResponse.getData().getRows());
                }
            });
        });
    }

    @Override
    public void onCreateGiftCode(String code) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.createGift(code).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<AddGift>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("onCreateGiftCode", e.toString());
                    v.onCreateGiftFail(AppUtils.getErrorMessage(e));
                }

                @Override
                public void onNext(BaseResponse<AddGift> arrayResponseBaseResponse) {
                    v.onCreateGiftSuccess(arrayResponseBaseResponse.getData());
                }
            });

        });
    }

    @Override
    public void deleteGift(int id) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.deleteGift(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<SendLog>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("deleteGift", e.toString());
//                    v.onCreateGiftFail(AppUtils.getErrorMessage(e));
                }

                @Override
                public void onNext(BaseResponse<SendLog> arrayResponseBaseResponse) {
                    v.onDeleteGiftSuccess();
                }
            });

        });
    }
}
