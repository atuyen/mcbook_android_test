package com.mc.books.fragments.more.listfriend;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.more.ListFriendResponse;
import com.mc.models.more.ListMemberResponse;
import com.mc.utilities.Constant;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListFriendPresenter<V extends IListFriendView> extends BaseDataPresenter<V> implements IListFriendPresenter<V> {
    protected ListFriendPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMyFriend(int start) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            Map<String, String> maps = new HashMap<>();
            maps.put("start", String.valueOf(start));
            maps.put("limit", String.valueOf(Constant.LIMIT_PAGING));
            v.onShowLoading(true);
            apiService.getMyFriendList(maps).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<ListFriendResponse>>() {
                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onNext(BaseResponse<ListFriendResponse> listFriendResponse) {
                            v.getMyFriendListSuccess(listFriendResponse.getData().getListFriendList());
                        }

                    });
        });
    }

    @Override
    public void unFollowFriend(String userId, String userFriendId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);

            apiService.unfollowFriend(userId, userFriendId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                            v.showUnFriendSuccess();
                        }

                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onNext(String s) {

                        }

                    });
        });
    }

    @Override
    public void followFriend(String userId, String userFriendId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);

            apiService.followFriend(userId, userFriendId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                            v.showFollowSucces();

                        }

                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onNext(String s) {

                        }

                    });
        });
    }

    @Override
    public void getListMember(int start, String keyword) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            Map<String, String> maps = new HashMap<>();
            maps.put("start", String.valueOf(start));
            maps.put("limit", String.valueOf(Constant.LIMIT_PAGING));
            maps.put("value", keyword);
            v.onShowLoading(true);
            apiService.getListMember(maps).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<ListMemberResponse>>() {
                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onNext(BaseResponse<ListMemberResponse> listMemberResponse) {
                            v.getListMemberSuccess(listMemberResponse.getData().getMemberList(), keyword);
                        }
                    });
        });
    }

}
