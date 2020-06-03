package com.mc.books.fragments.more.listfriend;

import com.mc.common.views.IBaseView;
import com.mc.models.more.Friend;
import com.mc.models.more.Members;

import java.util.List;

public interface IListFriendView extends IBaseView {
    void onShowLoading(boolean isShow);

    void getListMemberSuccess(List<Members> listMembers, String keyword);

    void getMyFriendListSuccess(List<Friend> listFriends);

    void showUnFriendSuccess();

    void showFollowSucces();
}
