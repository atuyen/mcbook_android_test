package com.mc.customizes.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bon.collection.CollectionUtils;
import com.mc.books.R;

import java.util.ArrayList;
import java.util.List;

public abstract class ExtRecyclerViewAdapter<T, VH extends ExtRecyclerViewHolder> extends RecyclerView.Adapter<ExtRecyclerViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    protected final Context context;
    protected final LayoutInflater layoutInflater;

    protected List<T> items;
    private int loadingId;

    /**
     * @param context
     * @param items
     */
    public ExtRecyclerViewAdapter(Context context, List<T> items) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.items = items;
    }

    /**
     * @param viewType
     * @return
     */
    protected abstract int getLayoutId(int viewType);

    /**
     * @param view
     * @param viewType
     * @return
     */
    protected abstract VH onCreateHolder(View view, int viewType);

    /**
     * @param holder
     * @param data
     * @param position
     */
    protected abstract void onBindViewHolder(@NonNull VH holder, T data, int position);

    /**
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? TYPE_LOADING : TYPE_ITEM;
    }

    @NonNull
    @Override
    public ExtRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = layoutInflater.inflate(getLayoutId(viewType), parent, false);
            return onCreateHolder(view, viewType);
        } else {
            View view = layoutInflater.inflate(loadingId == 0 ? R.layout.paging_item_loading : loadingId, parent, false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ExtRecyclerViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM && getItem(position) != null) {
            onBindViewHolder((VH) holder, getItem(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    /**
     * @param loadingId
     */
    public ExtRecyclerViewAdapter<T, VH> setLoadingLayout(int loadingId) {
        this.loadingId = loadingId;
        return this;
    }

    /**
     * get items
     *
     * @return
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * @param its
     */
    public ExtRecyclerViewAdapter<T, VH> setItems(List<T> its) {
        if (CollectionUtils.isNullOrEmpty(its)) return this;
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        items.clear();
        items.addAll(its);
        notifyDataSetChanged();
        return this;
    }

    /**
     * @param position
     * @return
     */
    public T getItem(int position) {
        return items == null ? null : items.get(position);
    }

    /**
     * @param item
     */
    public ExtRecyclerViewAdapter<T, VH> addItem(T item) {
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        items.add(item);
        notifyItemInserted(getItemCount());
        return this;
    }

    /**
     * @param index
     * @param item
     */
    public ExtRecyclerViewAdapter<T, VH> addItem(int index, T item) {
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        items.add(index, item);
        notifyItemInserted(index);
        return this;
    }

    /**
     * @param its
     */
    public ExtRecyclerViewAdapter<T, VH> addItems(List<T> its) {
        if (CollectionUtils.isNullOrEmpty(its)) return this;
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        items.addAll(its);
        notifyItemRangeInserted(getItemCount(), its.size());
        return this;
    }

    /**
     * @param index
     * @param item
     */
    public ExtRecyclerViewAdapter<T, VH> update(int index, T item) {
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        items.set(index, item);
        notifyItemChanged(index);
        return this;
    }

    /**
     * @param item
     */
    public ExtRecyclerViewAdapter<T, VH> removeItem(T item) {
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        removeItem(items.indexOf(item));
        return this;
    }

    /**
     * @param index
     */
    public ExtRecyclerViewAdapter<T, VH> removeItem(int index) {
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        if (index != -1) {
            items.remove(index);
            notifyItemRemoved(index);
        }
        return this;
    }

    /**
     * clear data
     */
    public ExtRecyclerViewAdapter<T, VH> clear() {
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        items.clear();
        notifyDataSetChanged();
        return this;
    }

    /**
     * is loading view
     *
     * @return
     */
    public boolean isLoading() {
        if (CollectionUtils.isNullOrEmpty(items)) return false;
        if (items.contains(null)) return true;
        return false;
    }

    /**
     * show loading
     */
    public ExtRecyclerViewAdapter<T, VH> showLoading() {
        if (CollectionUtils.isNullOrEmpty(items)) {
            items = new ArrayList<>();
        }

        if (!items.contains(null)) {
            addItem(null);
        }
        return this;
    }

    /**
     * hide loading
     */
    public ExtRecyclerViewAdapter<T, VH> hideLoading() {
        removeItem(null);
        return this;
    }

    /**
     * loading holder
     */
    static class LoadingHolder extends ExtRecyclerViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }
}

