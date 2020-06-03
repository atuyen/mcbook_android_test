package com.mc.books.fragments.home.listRelatedBook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mc.adapter.RelatedBookAdapter;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.BookResponse;
import com.mc.models.home.RelatedBook;

import java.util.List;

import butterknife.BindView;

import static com.mc.utilities.Constant.KEY_LIST_RELATED_BOOK;

public class ListRelatedBookFragment extends BaseMvpFragment<IListRelatedBookView, IListRelatedBookPresenter<IListRelatedBookView>> implements IListRelatedBookView {
    public static ListRelatedBookFragment newInstance(BookResponse bookResponse) {
        Bundle args = new Bundle();
        ListRelatedBookFragment fragment = new ListRelatedBookFragment();
        args.putSerializable(KEY_LIST_RELATED_BOOK, bookResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rvRelatedBook)
    RecyclerView rvRelatedBook;
    private RelatedBookAdapter relatedBookAdapter;
    private BookResponse bookResponse;

    @NonNull
    @Override
    public IListRelatedBookPresenter<IListRelatedBookView> createPresenter() {
        return new ListRelatedBookPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        try {
            initData();
            bookResponse = (BookResponse) getArguments().getSerializable(KEY_LIST_RELATED_BOOK);
            if (bookResponse != null) {
                presenter.onLoadListRelatedBook(bookResponse.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        relatedBookAdapter = new RelatedBookAdapter(getAppContext(), this::goToWebView, this::goToDetailRelatedBook);
        rvRelatedBook.setLayoutManager(new LinearLayoutManager(getAppContext()));
        rvRelatedBook.setAdapter(relatedBookAdapter);
        rvRelatedBook.setNestedScrollingEnabled(false);
    }

    private void goToWebView(String uri) {
        if (uri != null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    private void goToDetailRelatedBook(RelatedBook relatedBook) {
        if (relatedBook.getUri() != null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(relatedBook.getUri())));
    }

    @Override
    public int getResourceId() {
        return R.layout.list_related_book_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.related_book;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onLoadListRelatedBookSuccesss(List<RelatedBook> relatedBookList) {
        relatedBookAdapter.setDataList(relatedBookList);
    }
}
