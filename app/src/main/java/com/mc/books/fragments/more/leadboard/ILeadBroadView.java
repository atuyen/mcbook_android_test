package com.mc.books.fragments.more.leadboard;

import com.mc.common.views.IBaseView;
import com.mc.models.more.BookLeadBoad;
import com.mc.models.more.BookLeadBoadUpdate;
import com.mc.models.more.BookRanking;
import com.mc.models.more.CategoryLeadBoad;
import com.mc.models.more.LeadBroad;
import com.mc.models.more.UserRanking;

import java.util.List;

public interface ILeadBroadView extends IBaseView {
    void onShowLoading(boolean isShow);

    void getLeadBroadMemberSuccess(List<UserRanking> leadMemberBroads);

    void getLeadBroadBookSuccess(List<BookRanking> leadBookBroads);

    //void getBookSearchSuccess(List<BookLeadBoad> bookSearchList);

    void getBookLeadBoadSuccess(List<BookLeadBoad> bookLeadBoads);

    void getCategoryLeadBoad(List<CategoryLeadBoad> categoryLeadBoads);

    void getBookLeadBoadUpdateSuccess(BookLeadBoadUpdate bookLeadBoadUpdates);


    //void getLeadBroadError(int message);
}
