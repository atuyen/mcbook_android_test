package com.mc.books.fragments.home.listRelatedBook;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.home.RelatedBook;

import java.util.List;

public interface IListRelatedBookView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onLoadListRelatedBookSuccesss(List<RelatedBook> relatedBookList);
}
