package com.mc.books.fragments.more.listfriend;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IListFriendPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getListMember(int start, String keyword);

    void getMyFriend(int start);

    void unFollowFriend(String userId, String userFriendId);

    void followFriend(String userId, String userFriendId);
}
