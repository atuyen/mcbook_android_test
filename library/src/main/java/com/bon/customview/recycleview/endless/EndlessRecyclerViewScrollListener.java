package com.bon.customview.recycleview.endless;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bon.logger.Logger;

/**
 * Created by Dang on 4/19/2016.
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = EndlessRecyclerViewScrollListener.class.getSimpleName();

    protected static final int DEFAULT_PAGE = 1;
    private int currentPage = DEFAULT_PAGE;

    // The total number of items in the data set after the last load
    private int previousTotalItemCount = 0;
    private boolean loading = true;

    // layout manager
    private LinearLayoutManager mLinearLayoutManager = null;

    /**
     * @param layoutManager
     */
    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLinearLayoutManager = layoutManager;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        try {
            int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = view.getChildCount();
            int totalItemCount = mLinearLayoutManager.getItemCount();

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = 0;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) this.loading = true;
            }

            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            int visibleThreshold = 0;
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount);
                loading = true;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int pageNumber, int totalItemsCount);
}