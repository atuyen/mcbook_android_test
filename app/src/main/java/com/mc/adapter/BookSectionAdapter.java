package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.models.home.Author;
import com.mc.models.home.BookResponse;
import com.mc.models.home.DeleteBook;
import com.mc.utilities.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import java8.util.function.Consumer;

public class BookSectionAdapter extends StatelessSection {

    private String categoryBook;
    private List<BookResponse> bookResponseList;
    private Consumer<DeleteBook> imgMoreConsumer;
    private Consumer<BookResponse> cvBookConsumer;
    private Context context;

    public BookSectionAdapter(Context context, String categoryBook, List<BookResponse> bookResponseList, Consumer<DeleteBook> imgMoreConsumer, Consumer<BookResponse> cvBookConsumer) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_book_content)
                .headerResourceId(R.layout.item_book_header)
                .build());
        this.context = context;
        this.categoryBook = categoryBook;
        this.bookResponseList = bookResponseList;
        this.imgMoreConsumer = imgMoreConsumer;
        this.cvBookConsumer = cvBookConsumer;
    }

    @Override
    public int getContentItemsTotal() {
        return bookResponseList == null ? 0 : bookResponseList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            if (bookResponseList.size() > 0) {
                BookResponse bookResponse = bookResponseList.get(position);
                itemViewHolder.customProgress.setProgress(50);
                itemViewHolder.txtBookName.setText(bookResponse.getName());
                if (bookResponse.getAuthors().size() > 0) {
                    StringBuilder auth = new StringBuilder();
                    for (Author author : bookResponse.getAuthors()) {
                        auth.append(author.getName()).append(", ");
                    }
                    auth.delete(auth.length() - 2, auth.length());
                    itemViewHolder.txtBookAuthor.setText(auth);
                }

                AppUtils.setImageGlide(context, bookResponse.getAvatar(), R.drawable.ic_default_book, itemViewHolder.imgBook);
                DeleteBook deleteBook = new DeleteBook(bookResponse, itemViewHolder.imgMore);
                itemViewHolder.imgMore.setOnClickListener(view -> imgMoreConsumer.accept(deleteBook));
                itemViewHolder.cvBook.setOnClickListener(view -> cvBookConsumer.accept(bookResponse));
                itemViewHolder.imgNew.setVisibility(bookResponse.isNew() ? View.VISIBLE : View.INVISIBLE);
                itemViewHolder.customProgress.setProgress(bookResponse.getProcess());
                itemViewHolder.txtProgress.setText(bookResponse.getProcess() + "%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.txtHeaderBook.setText(categoryBook);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgMore)
        ImageView imgMore;
        @BindView(R.id.txtBookName)
        ExtTextView txtBookName;
        @BindView(R.id.txtBookAuthor)
        ExtTextView txtBookAuthor;
        @BindView(R.id.customProgress)
        ProgressBar customProgress;
        @BindView(R.id.txtProgress)
        ExtTextView txtProgress;
        @BindView(R.id.txtReadmore)
        ExtTextView txtReadmore;
        @BindView(R.id.imgNext)
        ImageView imgNext;
        @BindView(R.id.cvBook)
        CardView cvBook;
        @BindView(R.id.imgBook)
        ImageView imgBook;
        @BindView(R.id.imgNew)
        ImageView imgNew;

        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtHeaderBook)
        ExtTextView txtHeaderBook;

        private HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
