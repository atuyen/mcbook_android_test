package com.mc.customizes.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


import com.bon.collection.CollectionUtils;
import com.bon.customview.listview.listener.ExtLoadMoreListener;
import com.bon.customview.listview.listener.ExtRefreshListener;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.StringUtils;
import com.mc.books.R;

import java.util.List;

public class ExtRecyclerView<T, VH extends ExtRecyclerViewHolder> extends LinearLayout {
    // view
    SwipeRefreshLayout swSwipeRefreshLayout;
    RecyclerView rvRecyclerView;
    ExtTextView tvMessage;

    ExtRecyclerViewAdapter<T, VH> mAdapter;
    ExtRecyclerViewEndlessScrollListener endlessScrollListener;

    @LayoutRes
    int loadingLayout;

    // listener
    ExtLoadMoreListener onExtLoadMoreListener = null;
    ExtRefreshListener onExtRefreshListener = null;

    // layout manger
    RecyclerView.LayoutManager layoutManager;

    public static int numberPerPage;
    boolean hasFixedSize;

    public ExtRecyclerView(@NonNull Context context) {
        this(context, null, 0);
        initView(context, null);
    }

    public ExtRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context, attrs);
    }

    public ExtRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.paging_recyclerview, this);

        // default layout manager
        layoutManager = new LinearLayoutManager(context, LinearLayout.VERTICAL, false);

        // refresh view
        swSwipeRefreshLayout = view.findViewById(R.id.swSwipeRefreshLayout);

        // rvRecyclerView
        rvRecyclerView = view.findViewById(R.id.rvRecyclerView);

        //data not found
        tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setVisibility(GONE);

        //attribute config
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtRecyclerView);
        // swipe refresh enable
        swSwipeRefreshLayout.setEnabled(typedArray.getBoolean(R.styleable.ExtRecyclerView_rvSwipeRefreshEnable, true));

        // swipe refresh indicator color
        swSwipeRefreshLayout.setColorSchemeColors(typedArray.getColor(R.styleable.ExtRecyclerView_rvSwipeRefreshIndicatorColor,
                context.getResources().getColor(R.color.colorAccent)));

        // scroll bar visibility
        rvRecyclerView.setVerticalScrollBarEnabled(typedArray.getBoolean(R.styleable.ExtRecyclerView_rvScrollbarVisible, false));

        // divider color
        ExtRecyclerViewDivider divider = new ExtRecyclerViewDivider();
        divider.setDivider(new ColorDrawable(typedArray.getColor(R.styleable.ExtRecyclerView_rvDividerColor, 0)));

        // divider height
        divider.setHeight(typedArray.getDimensionPixelSize(R.styleable.ExtRecyclerView_rvDividerHeight, 0));

        // item decoration
        rvRecyclerView.addItemDecoration(divider);

        // message no data
        StringUtils.isNotNullOrEmpty(typedArray.getString(R.styleable.ExtRecyclerView_rvNoDataMessage), msg -> tvMessage.setText(msg));
        typedArray.recycle();
    }

    /**
     * @param adapter
     * @return
     */
    public ExtRecyclerView setAdapter(ExtRecyclerViewAdapter<T, VH> adapter) {
        this.mAdapter = adapter;
        return this;
    }

    /**
     * @param numberPerPage
     * @return
     */
    public static void setNumberPerPage(int numberPerPage) {
        ExtRecyclerView.numberPerPage = numberPerPage;
    }

    /**
     * @param hasFixedSize
     * @return
     */
    public ExtRecyclerView hasFixedSize(boolean hasFixedSize) {
        this.hasFixedSize = hasFixedSize;
        return this;
    }

    /**
     * @param layoutManager
     * @return
     */
    public ExtRecyclerView setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    /**
     * @param onExtLoadMoreListener
     * @return
     */
    public ExtRecyclerView setLoadMoreListener(ExtLoadMoreListener onExtLoadMoreListener) {
        this.onExtLoadMoreListener = onExtLoadMoreListener;
        return this;
    }

    /**
     * @param onExtRefreshListener
     * @return
     */
    public ExtRecyclerView setRefreshListener(ExtRefreshListener onExtRefreshListener) {
        this.onExtRefreshListener = onExtRefreshListener;
        return this;
    }

    /**
     * @param loadingLayout
     * @return
     */
    public ExtRecyclerView setLoadingLayout(@LayoutRes int loadingLayout) {
        this.loadingLayout = loadingLayout;
        return this;
    }

    /**
     * build recycler view
     */
    public void build() {
        if (mAdapter == null) throw new NullPointerException();

        // refresh layout
        swSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.e("setRefreshing", "setRefreshing");
            swSwipeRefreshLayout.setRefreshing(false);
            clear();
            Optional.from(onExtRefreshListener).doIfPresent(ExtRefreshListener::onRefresh);
        });

        // number per page
        if (numberPerPage != 0) {
            rvRecyclerView.setItemViewCacheSize(numberPerPage);
        }

        mAdapter.setLoadingLayout(loadingLayout);
        rvRecyclerView.setLayoutManager(layoutManager);
        rvRecyclerView.setHasFixedSize(hasFixedSize);
        rvRecyclerView.setAdapter(mAdapter);

        // update load more
        endlessScrollListener = new ExtRecyclerViewEndlessScrollListener(rvRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page) {
                Log.e("page", "page:: " + page);
                Optional.from(onExtLoadMoreListener).doIfPresent(ExtLoadMoreListener::onLoadMore);
            }
        };
        rvRecyclerView.addOnScrollListener(endlessScrollListener);
    }

    /**
     * display message
     */
    public void displayMessage() {
        tvMessage.setVisibility(mAdapter == null || mAdapter.getItemCount() <= 0 ? VISIBLE : GONE);
    }

    /**
     * show loading
     */
    public void showLoading() {
        if (mAdapter == null)
            throw new NullPointerException();
        mAdapter.showLoading();
        tvMessage.setVisibility(GONE);
        endlessScrollListener.setLoading(true);
    }

    /**
     * hide loading
     */
    public void hideLoading() {
        if (mAdapter == null)
            throw new NullPointerException();
        mAdapter.hideLoading();
        endlessScrollListener.setLoading(false);
    }

    /**
     * clear data
     */
    public void clear() {
        if (mAdapter == null)
            throw new NullPointerException();
        tvMessage.setVisibility(GONE);
        endlessScrollListener.resetState();
        mAdapter.clear();
    }

    /**
     * set items
     *
     * @param its
     */
    public void setItems(List<T> its) {
        if (mAdapter == null)
            throw new NullPointerException();
        mAdapter.setItems(its);
    }

    /**
     * @param position
     * @return
     */
    public T getItem(int position) {
        if (mAdapter == null)
            throw new NullPointerException();
        return mAdapter.getItem(position);
    }

    /**
     * @param item
     */
    public void addItem(T item) {
        if (mAdapter == null)
            throw new NullPointerException();
        mAdapter.addItem(item);
    }

    /**
     * @param index
     * @param item
     */
    public void addItem(int index, T item) {
        if (mAdapter == null)
            throw new NullPointerException();
        mAdapter.addItem(index, item);
    }

    /**
     * @param items
     */
    public void addItems(@NonNull List<T> items) {
        if (mAdapter == null)
            throw new NullPointerException();

        if (CollectionUtils.isNullOrEmpty(items) || items.size() < numberPerPage) {
            endlessScrollListener.setLastPage(true);
        }

        hideLoading();
        mAdapter.addItems(items);
        displayMessage();
    }

    /**
     * @param index
     * @param item
     */
    public void update(int index, T item) {
        if (mAdapter == null)
            throw new NullPointerException();
        mAdapter.update(index, item);
    }

    /**
     * @param item
     */
    public void removeItem(T item) {
        if (mAdapter == null)
            throw new NullPointerException();
        mAdapter.removeItem(item);
        displayMessage();
    }

    /**
     * @param index
     */
    public void removeItem(int index) {
        if (mAdapter == null)
            throw new NullPointerException();
        if (index != -1) mAdapter.removeItem(index);
        displayMessage();
    }

    /**
     * @return
     */
    public ExtRecyclerViewAdapter<T, VH> getAdapter() {
        if (mAdapter == null)
            throw new NullPointerException();
        return mAdapter;
    }

    /**
     * update recycler view
     */
    public void notifyDataSetChanged() {
        if (mAdapter == null)
            throw new NullPointerException();
        mAdapter.notifyDataSetChanged();
        displayMessage();
    }

    public int getItemCount() {
        if (mAdapter == null) throw new NullPointerException();
        return mAdapter.isLoading() ? (mAdapter.getItemCount() - 1) : mAdapter.getItemCount();
    }

    public List<T> getItems() {
        if (mAdapter == null) throw new NullPointerException();
        return mAdapter.getItems();
    }
}
