package com.mc.books.fragments.home.dashboad;

import android.view.View;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.models.home.CategoryResponse;
import com.mc.models.home.DeleteBook;

import java.util.List;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface IDashboardPresenter<V extends MvpView> extends MvpPresenter<V> {
    void showContextMenu(boolean isShow, DeleteBook deleteBook, int position);

    void onSearchBook(String keyword, List<CategoryResponse> categoryResponseList);

    void onLoadCategoryBook();

    void onDeleteBook(int bookId);

    void getConfig();

    void getLogNotification();
}
