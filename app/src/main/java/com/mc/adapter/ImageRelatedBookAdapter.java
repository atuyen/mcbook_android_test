package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.RelatedBook;
import com.mc.utilities.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class ImageRelatedBookAdapter extends BaseRecycleAdapter<RelatedBook, RecyclerView.ViewHolder> {
    private Context context;
    private Consumer<String> relatedBookConsumer;
    private Consumer<String> moreConsumer;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 0;

    public ImageRelatedBookAdapter(Context context, Consumer<String> relatedBookConsumer, Consumer<String> moreConsumer) {
        this.context = context;
        this.relatedBookConsumer = relatedBookConsumer;
        this.moreConsumer = moreConsumer;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_categoryrelated_book_footer, parent, false);
            return new ImageRelatedBookAdapter.RelatedBookFooterViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_categoryrelated_book, parent, false);
            return new ImageRelatedBookAdapter.RelatedBookViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof RelatedBookViewHolder) {
                RelatedBook relatedBook = listItems.get(position);
                AppUtils.setImageGlide(context, relatedBook.getPicture(), R.drawable.ic_default_book, ((RelatedBookViewHolder) holder).imgBook);
                ((RelatedBookViewHolder) holder).imgBook.setOnClickListener(v -> relatedBookConsumer.accept(relatedBook.getUri()));
            } else if (holder instanceof RelatedBookFooterViewHolder) {
                ((RelatedBookFooterViewHolder) holder).cvMore.setOnClickListener(v -> moreConsumer.accept(""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == listItems.size();
    }

    @Override
    public int getItemCount() {
        return listItems.size() >= 10 ? listItems.size() + 1 : listItems.size();
    }

    class RelatedBookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgBook)
        ImageView imgBook;

        private RelatedBookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RelatedBookFooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvMore)
        CardView cvMore;

        private RelatedBookFooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
