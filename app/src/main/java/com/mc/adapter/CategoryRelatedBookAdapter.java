package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.mc.books.R;
import com.mc.models.home.SnapRelatedBook;


import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class CategoryRelatedBookAdapter extends RecyclerView.Adapter<CategoryRelatedBookAdapter.RelatedBookViewHolder> implements GravitySnapHelper.SnapListener {
    private Context context;
    private ArrayList<SnapRelatedBook> mSnaps;
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;
    private ImageRelatedBookAdapter imageRelatedBookAdapter;
    private Consumer<String> relatedBookConsumer;
    private Consumer<SnapRelatedBook> moreConsumer;

    public CategoryRelatedBookAdapter(Context context, Consumer<String> relatedBookConsumer, Consumer<SnapRelatedBook> moreConsumer) {
        mSnaps = new ArrayList<>();
        this.context = context;
        this.relatedBookConsumer = relatedBookConsumer;
        this.moreConsumer = moreConsumer;
    }

    public void addSnap(SnapRelatedBook snap) {
        mSnaps.add(snap);
    }

    @NonNull
    @Override
    public RelatedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_category_related_book, parent, false);
        return new RelatedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedBookViewHolder holder, int position) {
        try {
            SnapRelatedBook snapRelatedBook = mSnaps.get(position);
            holder.txtCategory.setText(snapRelatedBook.getCategoryName());
            holder.rvRelatedBook.setLayoutManager(new LinearLayoutManager(holder
                    .rvRelatedBook.getContext(), LinearLayoutManager.HORIZONTAL, false));
            new GravitySnapHelper(snapRelatedBook.getGravity(), false, this).attachToRecyclerView(holder.rvRelatedBook);
            imageRelatedBookAdapter = new ImageRelatedBookAdapter(context, uri -> relatedBookConsumer.accept(uri), string -> moreConsumer.accept(snapRelatedBook));
            holder.rvRelatedBook.setAdapter(imageRelatedBookAdapter);
            imageRelatedBookAdapter.setDataList(snapRelatedBook.getApps());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return HORIZONTAL;
    }

    @Override
    public int getItemCount() {
        return mSnaps.size();
    }

    @Override
    public void onSnap(int position) {

    }

    class RelatedBookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtCategory)
        ExtTextView txtCategory;
        @BindView(R.id.rvRelatedBook)
        RecyclerView rvRelatedBook;

        private RelatedBookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
