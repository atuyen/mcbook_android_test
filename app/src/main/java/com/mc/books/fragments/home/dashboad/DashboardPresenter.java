package com.mc.books.fragments.home.dashboad;

import android.util.Log;

import com.bon.jackson.JacksonUtils;
import com.mc.application.AppContext;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.home.CategoryResponse;
import com.mc.models.home.DeleteBook;
import com.mc.models.more.Config;
import com.mc.models.notification.NotificationLog;
import com.mc.models.realm.LessonRealm;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DashboardPresenter<V extends IDashboardView> extends BaseDataPresenter<V> implements IDashboardPresenter<V> {
    protected DashboardPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void showContextMenu(boolean isShow, DeleteBook deleteBook, int position) {
        getOptView().doIfPresent(v -> v.showContextMenuSuccess(isShow, deleteBook, position));
    }

    @Override
    public void onSearchBook(String keyword, List<CategoryResponse> categoryResponseList) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) {
                List<CategoryResponse> filter = StreamSupport.stream(new ArrayList<>(categoryResponseList))
                        .map(cate -> new CategoryResponse(cate.getId(), cate.getName(), StreamSupport.stream(cate.getBooks())
                                .filter(n -> n.getName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList())))
                        .filter(cate -> cate.getBooks().size() > 0).collect(Collectors.toList());
                v.onGetCategorySuccess(filter);
            } else {
                v.onShowLoading(true);
                String filter = AppUtils.getFilter(keyword);
                apiService.getSearchBook(filter).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<BaseResponse<ArrayResponse<CategoryResponse>>>() {
                            @Override
                            public void onCompleted() {
                                v.onShowLoading(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                v.onShowLoading(false);
                                Log.e("onSearchBook err", "" + e.toString());
                            }

                            @Override
                            public void onNext(BaseResponse<ArrayResponse<CategoryResponse>> arrayResponseBaseResponse) {
                                v.onGetCategorySuccess(arrayResponseBaseResponse.getData().getRows());
                            }
                        });
            }


        });
    }

    @Override
    public void onLoadCategoryBook() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getCategory().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<CategoryResponse>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    v.onGetCategoryError(AppUtils.getErrorMessage(e));
                    Log.e("onLoadCategoryBook err", "" + e.toString());
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<CategoryResponse>> arrayResponseBaseResponse) {
                    //write json file
                    AppUtils.writeToFile(AppContext.getInstance(), JacksonUtils.writeValueToString(arrayResponseBaseResponse.getData().getRows()), Constant.MY_BOOK);
                    v.onGetCategorySuccess(arrayResponseBaseResponse.getData().getRows());
                }
            });
        });
    }

    @Override
    public void onDeleteBook(int bookId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.deleteBook(bookId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse>() {
                @Override
                public void onCompleted() {
                    //remove book db
                    AppContext.getRealm().executeTransaction(realm -> {
                        RealmResults<LessonRealm> result = AppContext.getRealm().where(LessonRealm.class).equalTo(LessonRealm.BOOK_ID, bookId).equalTo(LessonRealm.USER_ID, AppContext.getUserId()).findAll();
                        result.deleteAllFromRealm();
                    });

                    //remove file in book folder
                    File file = new File(AppUtils.getFolderPath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId));
                    if (file.exists() && file.isDirectory()) {
                        for (File child : file.listFiles()) {
                            child.delete();
                        }
                    }

                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("onDeleteBook err", e.toString());
                }

                @Override
                public void onNext(BaseResponse baseResponse) {
                    v.deleteBookSuccess(baseResponse.getMessage());
                }
            });
        });
    }

    @Override
    public void getConfig() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getConfig().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<Config>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("getConfig err", e.getMessage());
                }

                @Override
                public void onNext(BaseResponse<Config> configBaseResponse) {
                    v.onGetConfigSuccess(configBaseResponse.getData());
                }
            });

        });
    }

    @Override
    public void getLogNotification() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.updateLogNoti().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<NotificationLog>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("updateLogNoti err", e.getMessage());
                }

                @Override
                public void onNext(BaseResponse<NotificationLog> notificationLog) {
                    v.onShowLogNotification(notificationLog.getData());
                }
            });

        });
    }
}
