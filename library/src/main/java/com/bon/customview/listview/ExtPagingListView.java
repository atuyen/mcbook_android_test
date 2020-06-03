package com.bon.customview.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.bon.customview.listview.listener.ExtClickListener;
import com.bon.customview.listview.listener.ExtLoadMoreListener;
import com.bon.customview.listview.listener.ExtLongClickListener;
import com.bon.customview.listview.listener.ExtRefreshListener;
import com.bon.customview.textview.ExtTextView;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;

import java.util.List;

/**
 * Created by Dang on 5/27/2016.
 */
@SuppressWarnings("ALL")
public class ExtPagingListView<T> extends FrameLayout {
    private static final String TAG = ExtPagingListView.class.getSimpleName();

    // view
    private SwipeRefreshLayout rfLayout;
    private ListView lvData;
    private View vLoading;
    private ExtTextView tvMessage;

    // status
    private boolean loading = false;
    private boolean hasMore = true;

    // adapter
    private ExtBaseAdapter extBaseAdapter = null;

    // listener
    private ExtClickListener<T> onExtClickListener = null;
    private ExtLongClickListener<T> onExtLongClickListener = null;
    private ExtLoadMoreListener onExtLoadMoreListener = null;
    private ExtRefreshListener onExtRefreshListener = null;

    /**
     * @param context
     */
    public ExtPagingListView(Context context) {
        super(context);
        this.initView(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ExtPagingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ExtPagingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initView(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        try {
            View view = inflate(context, R.layout.paging_listview, this);

            // refresh layout
            this.rfLayout = view.findViewById(R.id.rf_layout);
            this.rfLayout.setOnRefreshListener(() -> {
                if (this.onExtRefreshListener != null) {
                    this.onExtRefreshListener.onRefresh();
                }
            });

            // listview
            this.lvData = view.findViewById(R.id.lv_data);
            this.lvData.setFooterDividersEnabled(false);
            this.lvData.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

            //data not found
            this.tvMessage = view.findViewById(R.id.tv_message);

            //attribute config
            if (attrs != null) {
                TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExtPagingListView, 0, 0);
                try {
                    // swipe refresh enable
                    boolean swipeRefreshEnable = typedArray.getBoolean(R.styleable.ExtPagingListView_swipeRefreshEnable, true);
                    if (!swipeRefreshEnable) {
                        this.rfLayout.setEnabled(false);
                        this.rfLayout.setRefreshing(false);
                    }

                    // swipe refresh indicator color
                    int swipeRefreshIndicatorColor = typedArray.getColor(R.styleable.ExtPagingListView_swipeRefreshIndicatorColor,
                            context.getResources().getColor(android.R.color.black));
                    this.rfLayout.setColorSchemeColors(swipeRefreshIndicatorColor);

                    // scroll bar visibility
                    boolean scrollbarVisible = typedArray.getBoolean(R.styleable.ExtPagingListView_scrollbarVisible, true);
                    this.lvData.setVerticalScrollBarEnabled(scrollbarVisible);

                    // divider color
                    int color = typedArray.getColor(R.styleable.ExtPagingListView_dividerColor, 0);
                    this.lvData.setDivider(new ColorDrawable(color));

                    // divider height
                    int dividerHeight = typedArray.getDimensionPixelSize(R.styleable.ExtPagingListView_dividerHeight, 0);
                    this.lvData.setDividerHeight(dividerHeight);

                    // message no data
                    String message = typedArray.getString(R.styleable.ExtPagingListView_noDataMessage);
                    if (!StringUtils.isEmpty(message)) this.tvMessage.setText(message);
                } finally {
                    typedArray.recycle();
                }
            }
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param extBaseAdapter
     */
    public ExtPagingListView init(Context context, ExtBaseAdapter<T> extBaseAdapter) {
        return init(context, extBaseAdapter, null);
    }

    /**
     * @param context
     * @param extBaseAdapter
     * @param loadingView
     */
    @SuppressLint("InflateParams")
    public ExtPagingListView init(Context context, ExtBaseAdapter<T> extBaseAdapter, final View loadingView) {
        try {
            this.extBaseAdapter = extBaseAdapter;
            this.lvData.setAdapter(extBaseAdapter);

            // loading view
            if (loadingView != null) {
                this.vLoading = loadingView;
            } else {
                this.vLoading = LayoutInflater.from(context).inflate(R.layout.paging_item_loading, null);
            }

            // scroll listview
            this.lvData.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    try {
                        if (!hasMore || onExtLoadMoreListener == null) return;
                        int lastVisibleItem = visibleItemCount + firstVisibleItem;
                        if (lastVisibleItem >= totalItemCount && !loading) {
                            onExtLoadMoreListener.onLoadMore();
                        }
                    } catch (Exception e) {
                        Logger.e(TAG, e);
                    }
                }
            });

            // click
            this.lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onExtClickListener != null) {
                        onExtClickListener.onClick(position, extBaseAdapter == null ? null : extBaseAdapter.getItem(position));
                    }
                }
            });

            // long click
            this.lvData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onExtLongClickListener != null) {
                        onExtLongClickListener.onLongClick(position, extBaseAdapter == null ? null : extBaseAdapter.getItem(position));
                    }

                    return false;
                }
            });

            this.displayMessage();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * notification data
     *
     * @param items
     * @param <T>
     */
    public void notifyDataSetChanged(List<T> items) {
        try {
            if (this.extBaseAdapter != null) {
                this.extBaseAdapter.notifyDataSetChanged(items);
            }

            this.displayMessage();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * display message no data
     */
    public ExtPagingListView displayMessage() {
        try {
            if (this.extBaseAdapter == null || this.extBaseAdapter.getCount() <= 0) {
                this.tvMessage.setVisibility(VISIBLE);
            } else {
                this.tvMessage.setVisibility(GONE);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * get base adapter
     *
     * @return
     */
    public ExtBaseAdapter getExtBaseAdapter() {
        if (extBaseAdapter == null) {
            throw new NullPointerException("ExtBaseAdapter can not null!!!");
        }

        return extBaseAdapter;
    }

    /**
     * set adapter
     *
     * @param adapter
     * @param <AD>
     * @return
     */
    public <AD extends ExtBaseAdapter<T>> ExtPagingListView setExtBaseAdapter(AD adapter) {
        try {
            this.extBaseAdapter = adapter;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    public ExtPagingListView setSelection(int position) {
        try {
            this.lvData.setSelection(position);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * @param newItem
     */
    public ExtPagingListView addNewItem(T newItem) {
        try {
            this.extBaseAdapter.addNewItem(newItem);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * @param newItems
     */
    public ExtPagingListView addNewItems(List<T> newItems) {
        try {
            if (newItems == null || newItems.size() <= 0) {
                this.hasMore = false;
            }

            this.extBaseAdapter.addNewItems(newItems);

            // display no data mesage
            this.displayMessage();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * remove item
     *
     * @param item
     */
    public ExtPagingListView removeItem(T item) {
        try {
            this.extBaseAdapter.removeItem(item);

            // display no data mesage
            this.displayMessage();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * remove items
     *
     * @param items
     */
    public ExtPagingListView removeItems(List<T> items) {
        try {
            this.extBaseAdapter.removeItems(items);

            // display no data mesage
            this.displayMessage();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * clear data
     */
    public ExtPagingListView clearItems() {
        try {
            this.hasMore = true;
            this.extBaseAdapter.clearList();

            // display no data mesage
            this.displayMessage();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * start loading
     */
    public ExtPagingListView startLoading() {
        this.startLoading(false);
        return this;
    }

    /**
     * start loading
     */
    public ExtPagingListView startLoading(boolean isLoading) {
        try {
            this.loading = true;
            if (this.rfLayout == null || this.lvData == null || this.vLoading == null) {
                return this;
            }

            // add loading view to footer
            if (!this.rfLayout.isRefreshing() && isLoading) {
                this.lvData.addFooterView(vLoading);
            }

            this.tvMessage.setVisibility(GONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * stop loading
     */
    public ExtPagingListView stopLoading() {
        this.stopLoading(false);
        return this;
    }

    /**
     * stop loading
     */
    public ExtPagingListView stopLoading(boolean isLoading) {
        try {
            this.loading = false;
            if (this.rfLayout == null || this.lvData == null || this.vLoading == null) {
                return this;
            }

            // remove loading view
            if (!this.rfLayout.isRefreshing() && isLoading) {
                this.lvData.removeFooterView(vLoading);
            }

            // disable swipe loading
            this.rfLayout.setRefreshing(false);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * set has more
     *
     * @param hasMore
     */
    public ExtPagingListView hasMore(boolean hasMore) {
        this.hasMore = hasMore;
        return this;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    /**
     * @param onExtLongClickListener
     * @return
     */
    public ExtPagingListView setOnExtLongClickListener(ExtLongClickListener<T> onExtLongClickListener) {
        this.onExtLongClickListener = onExtLongClickListener;
        return this;
    }

    /**
     * @param onExtClickListener
     * @return
     */
    public ExtPagingListView setOnExtClickListener(ExtClickListener<T> onExtClickListener) {
        this.onExtClickListener = onExtClickListener;
        return this;
    }

    /**
     * @param onExtLoadMoreListener
     * @return
     */
    public ExtPagingListView setOnExtLoadMoreListener(ExtLoadMoreListener onExtLoadMoreListener) {
        this.onExtLoadMoreListener = onExtLoadMoreListener;
        return this;
    }

    /**
     * @param onExtRefreshListener
     * @return
     */
    public ExtPagingListView setOnExtRefreshListener(ExtRefreshListener onExtRefreshListener) {
        this.onExtRefreshListener = onExtRefreshListener;
        return this;
    }

    /**
     * set refresh listview
     *
     * @param isEnabled
     * @return
     */
    public ExtPagingListView setEnabledSwipeRefreshing(boolean isEnabled) {
        try {
            this.rfLayout.setEnabled(isEnabled);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    public ExtPagingListView scrollToPosition(int position) {
        try {
            this.lvData.smoothScrollToPosition(position);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * set text message
     *
     * @param message
     * @return
     */
    public ExtPagingListView setMessage(String message) {
        try {
            this.tvMessage.setText(message);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }
}