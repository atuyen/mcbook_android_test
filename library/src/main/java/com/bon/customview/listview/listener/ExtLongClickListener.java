package com.bon.customview.listview.listener;

/**
 * Created by Dang on 6/9/2016.
 */
public interface ExtLongClickListener<T> {
    /**
     * onLongClick
     *
     * @param position
     * @param item
     */
    void onLongClick(int position, T item);
}
