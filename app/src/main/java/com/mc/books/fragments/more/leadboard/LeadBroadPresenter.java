package com.mc.books.fragments.more.leadboard;

import com.bon.util.StringUtils;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.more.BookLeadBoadResponse;
import com.mc.models.more.BookLeadBoadUpdate;
import com.mc.models.more.BookRankingResponse;
import com.mc.models.more.CategoryLeadBoadResponse;
import com.mc.models.more.UserRankingResponse;
import com.mc.utilities.Constant;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LeadBroadPresenter<V extends ILeadBroadView> extends BaseDataPresenter<V> implements ILeadBroadPresenter<V> {

    protected LeadBroadPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getLeadBroadMember(String filterBook, String filterUser, String filterTime) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            Map<String, String> memberFilter = new HashMap<>();
            if (!StringUtils.isEmpty(filterBook)) memberFilter.put("filterBook", filterBook);
            if (!StringUtils.isEmpty(filterUser)) memberFilter.put("filterUser", filterUser);
            if (!StringUtils.isEmpty(filterTime)) memberFilter.put("filterTime", filterTime);

            apiService.getMemberLeadBroad(memberFilter).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<UserRankingResponse>>() {
                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                            //v.getLeadBroadError(AppUtils.getErrorMessage(e));
                        }

                        @Override
                        public void onNext(BaseResponse<UserRankingResponse> memberResponse) {
                            v.getLeadBroadMemberSuccess(memberResponse.getData().getMembers());
                        }
                    });
        });
    }

    @Override
    public void getLeadBroadBook(String category, String filterBook, String filterTime) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            Map<String, String> memberFilter = new HashMap<>();
            if (!StringUtils.isEmpty(category)) memberFilter.put("category", category);
            if (!StringUtils.isEmpty(filterBook)) memberFilter.put("filterBook", filterBook);
            if (!StringUtils.isEmpty(filterTime)) memberFilter.put("filterTime", filterTime);

            apiService.getBookLeadBroad(memberFilter).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<BookRankingResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BaseResponse<BookRankingResponse> bookRankingResponse) {
                            v.getLeadBroadBookSuccess(bookRankingResponse.getData().getBookRankings());
                        }

                    });
        });
    }

    @Override
    public void getBookSearch(String start, String limit, String keyword) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            String fillter = "[{\"operator\":\"iLike\",\"value\":\"" + keyword.toLowerCase() + "\",\"property\":\"name\"}]";
            apiService.searchBookFillter(start, limit, fillter).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<BookLeadBoadResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BaseResponse<BookLeadBoadResponse> bookLeadBoad) {
                            v.getBookLeadBoadSuccess(bookLeadBoad.getData().getBookLeadBoads());
                        }

                    });
        });
    }

    @Override
    public void getBookLeadBoad() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            v.onShowLoading(true);
            apiService.getListBookLeadBoad(String.valueOf(Constant.LIMIT_START), "").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<BookLeadBoadResponse>>() {
                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onNext(BaseResponse<BookLeadBoadResponse> bookLeadBoad) {
                            v.onShowLoading(false);
                            v.getBookLeadBoadSuccess(bookLeadBoad.getData().getBookLeadBoads());
                        }

                    });
        });
    }

    @Override
    public void getCategoryLeadBoad() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            String fillter = "[{\"operator\":\"eq\",\"value\":1,\"property\":\"active\"}]";
            apiService.getListCategoryLeadBoad(fillter).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<CategoryLeadBoadResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BaseResponse<CategoryLeadBoadResponse> categoryLeadBoad) {
                            v.getCategoryLeadBoad(categoryLeadBoad.getData().getCategoryLeadBoads());
                        }

                    });
        });
    }

    @Override
    public void getCategoryFillter(String keyword) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            String fillter = "[{\"operator\":\"iLike\",\"value\":\"" + keyword.toLowerCase() + "\",\"property\":\"name\"},{\"operator\":\"eq\",\"value\":1,\"property\":\"active\"}]";
            apiService.searchCategoryFillter(fillter).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<CategoryLeadBoadResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BaseResponse<CategoryLeadBoadResponse> categoryLeadBoad) {
                            v.getCategoryLeadBoad(categoryLeadBoad.getData().getCategoryLeadBoads());
                        }

                    });
        });
    }

    @Override
    public void getBookLeadBoadUpdate(String type) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            apiService.getBookLeadBoadUpdate(type).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<BookLeadBoadUpdate>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BaseResponse<BookLeadBoadUpdate> bookLeadBoadUpdateResponse) {
                            v.getBookLeadBoadUpdateSuccess(bookLeadBoadUpdateResponse.getData());
                        }

                    });
        });
    }

}
