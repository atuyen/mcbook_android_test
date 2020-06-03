package com.mc.books.fragments.more.leadboard;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.models.more.LeadBroad;

import java.util.List;

public interface ILeadBroadPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getLeadBroadMember(String filterBook, String filterUser, String filterTime);

    void getLeadBroadBook(String category, String filterBook, String filterTime);

    void getBookSearch(String start, String limit, String keyword);

    void getBookLeadBoad();

    void getCategoryLeadBoad();

    void getCategoryFillter(String keyword);

    void getBookLeadBoadUpdate(String type);
}
