package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;

import com.mc.models.home.RelatedBook;
import com.mc.utilities.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class RelatedBookAdapter extends BaseRecycleAdapter<RelatedBook, RelatedBookAdapter.ReletedBookHolder> {

    private Context context;
    private Consumer<String> buyBookConsumer;
    private Consumer<RelatedBook> reletedBookConsumer;

    public RelatedBookAdapter(Context context, Consumer<String> buyBookConsumer, Consumer<RelatedBook> reletedBookConsumer) {
        this.context = context;
        this.buyBookConsumer = buyBookConsumer;
        this.reletedBookConsumer = reletedBookConsumer;
    }

    @NonNull
    @Override
    public RelatedBookAdapter.ReletedBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_book, parent, false);
        return new RelatedBookAdapter.ReletedBookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedBookAdapter.ReletedBookHolder holder, int position) {
        try {
            RelatedBook relatedBook = listItems.get(position);
            AppUtils.setImageGlide(context, relatedBook.getPicture(), R.drawable.ic_default_book, holder.bookAvatar);
            holder.bookName.setText(relatedBook.getName());
            holder.bookPrice.setText(AppUtils.formatPrice(relatedBook.getPrice() == null ? 0 : relatedBook.getPrice()));
            holder.bookUri.setOnClickListener(view -> buyBookConsumer.accept(relatedBook.getUri()));
            holder.rlRelatedBook.setOnClickListener(view -> reletedBookConsumer.accept(relatedBook));
            if (position == listItems.size() - 1)
                holder.viewLine.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ReletedBookHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bookAvatar)
        AppCompatImageView bookAvatar;
        @BindView(R.id.bookName)
        ExtTextView bookName;
        @BindView(R.id.bookPrice)
        ExtTextView bookPrice;
        @BindView(R.id.bookUri)
        ExtButton bookUri;
        @BindView(R.id.rlRelatedBook)
        LinearLayout rlRelatedBook;
        @BindView(R.id.viewLine)
        View viewLine;

        private ReletedBookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}